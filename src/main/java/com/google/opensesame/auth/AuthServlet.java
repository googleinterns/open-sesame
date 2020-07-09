package com.google.opensesame.auth;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.opensesame.util.ErrorResponse;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
  public static final String AUTH_URL_REDIRECT = "/";

  public Gson gson = new Gson();

  /**
   * Gets the currently authorized user. Returns null if no user is currently authorized.
   *
   * @return Returns the currently authorized user or null if there is no user currently authorized.
   */
  public static User getAuthorizedUser() {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      return null;
    }

    return userService.getCurrentUser();
  }

  /**
   * @return Returns the request parameter associated with the inputted name,
   * or returns the default value if the specified parameter is not defined.
   */
  public static String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  /**
   * Responds with the URLs for auth login and logout, along with information about the currently
   * authorized user if a user is logged in.
   *
   * @param request
   * @param response
   * @throws IOException
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    JsonObject responseObject = new JsonObject();
    addAuthData(responseObject);

    String responseJson = gson.toJson(responseObject);
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json;");
    response.getWriter().println(responseJson);
  }

  private void addAuthData(JsonObject responseObject) {
    UserService userService = UserServiceFactory.getUserService();

    responseObject.addProperty("loginUrl", userService.createLoginURL(AUTH_URL_REDIRECT));
    responseObject.addProperty("logoutUrl", userService.createLogoutURL(AUTH_URL_REDIRECT));

    if (userService.isUserLoggedIn()) {
      responseObject.addProperty("authorized", true);

      // User is logged in; add user data to the response
      JsonObject userData = new JsonObject();
      User currentUser = userService.getCurrentUser();
      userData.addProperty("email", currentUser.getEmail());
      userData.addProperty("id", currentUser.getUserId());

      responseObject.add("user", userData);

      // TODO : Add ability to check that a PersonObject exists in datastore with the user's ID.
      // For now, will assume to be false. This can be implemented when datastore support is added
      // for the PersonObject
      // Checking the datastore for an existing PersonObject is mildly inefficient. This is a side
      // effect of having to use the Users API on top of GitHub OAuth. Because there are two steps
      // in signing up, this check ensures that the user has completed both authentication steps.
      // If in the future OAuth can be used for backend authentication (through something like the
      // Firebase Auth Admin SDK), this check can be removed (along with all Users API code).
      responseObject.addProperty("hasProfile", false);
    } else {
      responseObject.addProperty("authorized", false);
      responseObject.addProperty("hasProfile", false);
    }
  }
}
