package com.google.opensesame.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

  private DatastoreService datastore;

  @Override
  // Instantiate datastore
  public void init() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  @Override
  // Get a specific user. Return null if not found. TODO: User Validation
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String userGithub = request.getParameter("githubID");
    Key userKey = KeyFactory.createKey(PersonBuilder.ENTITY_NAME, userGithub);
    System.out.println(userKey);

    PersonObject userObject;    
    try {
      userObject = new PersonBuilder().buildPersonObject(datastore.get(userKey));
      System.out.println(userObject);
    } catch (Exception e) {
      e.printStackTrace();
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
      return;
    }

    String jsonPerson = new Gson().toJson(userObject);
    response.setContentType("application/json;");
    response.getWriter().println(jsonPerson);
  }

  @Override
  // Send a user to datastore. Update the current information about the user if one exists.
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String userGitHubID = request.getParameter("githubID");
    if (userGitHubID.trim().isEmpty()) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
      return;
    }
    ArrayList<String> userTags = new ArrayList<>(
        Arrays.asList(request.getParameterValues("tags")));

    Entity personEntity =
        new PersonBuilder().gitHubID(userGitHubID).interestTags(userTags).buildPersonEntity();
    datastore.put(personEntity);
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

  // TODO: make error handling conform with Richie's error handling
}
