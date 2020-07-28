package com.google.opensesame.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.appengine.api.users.User;
import com.google.gson.Gson;
import com.google.opensesame.auth.AuthServlet;
import com.google.opensesame.user.UserData;
import com.google.opensesame.user.UserEntity;
import com.google.opensesame.util.ErrorResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.kohsuke.github.GHMyself;
import org.kohsuke.github.GitHub;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

  @Override
  // Get a user by their userId. Return the currently signed-in user if no
  // userId is supplied.
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String userId = request.getParameter("userID");
    if (userId == null) {
      User currentUser = AuthServlet.getAuthorizedUser();
      if (currentUser == null) {
        ErrorResponse.sendJsonError(
            response,
            "UserID not Supplied and user not logged in",
            HttpServletResponse.SC_FORBIDDEN,
            "You are not logged in");
        return;
      } else {
        userId = currentUser.getUserId();
      }
    }
    UserEntity userEntity = ofy().load().type(UserEntity.class).id(userId).now();
    if (userEntity == null) {
      ErrorResponse.sendJsonError(
          response,
          "User retrieved from Datastore was null",
          HttpServletResponse.SC_NOT_FOUND,
          "User does not exist.");
      return;
    }

    UserData userObject = new UserData(userEntity);
    String jsonPerson = new Gson().toJson(userObject);
    response.setContentType("application/json;");
    response.getWriter().println(jsonPerson);
  }

  @Override
  // Send a user to datastore. Update the current information about the user if
  // one exists.
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String gitHubAuthToken = request.getParameter("gitHubAuthToken");
    if (gitHubAuthToken == null) {
      ErrorResponse.sendJsonError(
          response,
          "GitHub OAuth token was never supplied",
          HttpServletResponse.SC_UNAUTHORIZED,
          "User could not be authenticated by GitHub, please try again");
      return;
    }

    User user = AuthServlet.getAuthorizedUser();
    if (user == null) {
      ErrorResponse.sendJsonError(
          response,
          "User not logged in",
          HttpServletResponse.SC_FORBIDDEN,
          "You are not logged in");
      return;
    }

    String[] interestTags = request.getParameterValues("interestTags");
    if (interestTags == null) {
      interestTags = new String[] {};
    }

    // Get User information from GitHub using the Oath token.
    GHMyself userGHMyself;
    try {
      userGHMyself = GitHub.connectUsingOAuth(gitHubAuthToken).getMyself();
    } catch (Exception e) {
      ErrorResponse.sendJsonError(
          response,
          e.getMessage()
              + e.getStackTrace()
              + "Could not get authenticated User with the given token",
          HttpServletResponse.SC_UNAUTHORIZED,
          "User could not be authenticated by GitHub, please try again");
      return;
    }

    String userId = user.getUserId();
    // Build and save the user's datastore entity
    ofy()
        .save()
        .entity(
            new UserEntity(
                userId,
                userGHMyself.getLogin(),
                new ArrayList<String>(Arrays.asList(interestTags)),
                user.getEmail()))
        .now();
  }
}
