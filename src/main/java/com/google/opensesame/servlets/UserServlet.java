package com.google.opensesame.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
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

  private DatastoreService datastore;

  @Override
  // Instantiate datastore
  public void init() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  @Override
  // Get a specific user. Return the currently signed-in user if no userID is
  // supplied.
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String userID = request.getParameter("userID");
    if (userID == null) {
      try {
        userID = AuthServlet.getAuthorizedUser().getUserId();
      } catch (NullPointerException e) {
        ErrorResponse.sendJsonError(
            response,
            "UserID not Supplied and user not logged in",
            HttpServletResponse.SC_FORBIDDEN,
            "You are not logged in");
        return;
      }
    }
    UserEntity userEntity = ofy().load().type(UserEntity.class).id(userID).now();
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
  // Send a user to datastore. Update the current information about the user if one exists.
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String userGitHubAuthToken = request.getParameter("gitHubAuthToken");
    if (userGitHubAuthToken == null) {
      ErrorResponse.sendJsonError(
          response,
          "GitHub OAuth token was never supplied",
          HttpServletResponse.SC_UNAUTHORIZED,
          "User could not be authenticated by GitHub, please try again");
      return;
    }
    String[] interestTagsParam = request.getParameterValues("interestTags");
    if (interestTagsParam == null) {
      interestTagsParam = new String[] {};
    }
    ArrayList<String> userTags = new ArrayList<>(Arrays.asList(interestTagsParam));

    User user = AuthServlet.getAuthorizedUser();
    if (user == null) {
      ErrorResponse.sendJsonError(
          response,
          "User not logged in",
          HttpServletResponse.SC_FORBIDDEN,
          "You are not logged in");
      return;
    }

    String userID = user.getUserId();

    // Get User information from GitHub using the Oath token.
    GHMyself userGHMyself;
    try {
      userGHMyself = GitHub.connectUsingOAuth(userGitHubAuthToken).getMyself();
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
    // Build and send the User's datastore entity
    ofy().save().entity(new UserEntity(userID, userGHMyself.getLogin(), userTags, user.getEmail()));
  }

  // TODO: use function in other servlets.
  /**
   * Query datastore for entities. Comparisons are done with field coming first. For example field
   * EQUAL value. Look at
   * https://cloud.google.com/appengine/docs/standard/java/javadoc/com/google/appengine/api/datastore/Query.FilterOperator
   * for more information on the operator
   *
   * @param EntityName the type of entity to be queried for
   * @param field the field that is being used to query
   * @param value the object that will be compared to @param field
   * @param operator the type of comparison to be made.
   * @return {PreparedQuery} a Datastore prepared query.
   */
  public PreparedQuery queryInDatabase(
      String entityName, String field, Object value, FilterOperator operator) {
    Filter userFilter = new FilterPredicate(field, operator, value);
    return datastore.prepare(new Query(entityName).setFilter(userFilter));
  }

  // TODO: make function to get users
}
