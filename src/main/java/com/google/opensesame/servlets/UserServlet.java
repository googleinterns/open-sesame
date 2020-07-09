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
import java.util.stream.Collectors;
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
  // get a specific user. return null if not found. TODO: User Validation
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String userGithub = request.getParameter("githubID");
    Key userKey = KeyFactory.createKey(PersonObject.ENTITY_NAME, userGithub);

    PersonObject userObject;

    try {
      userObject = toPersonObject(datastore.get(userKey));
    } catch (Exception e) {
      sendRawTextError(
          response, HttpServletResponse.SC_BAD_REQUEST, "Person does not exist in Datastore");
      userObject = null;
    }

    String jsonPerson = new Gson().toJson(userObject);
    response.setContentType("application/json;");
    response.getWriter().println(jsonPerson);
  }

  @Override
  // Send a user to datastore. Update the current information about the user if one exists.
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String userGitHubID = request.getParameter("gitHubID");
    if (userGitHubID.isBlank()) {
      sendRawTextError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
      return;
    }
    ArrayList<String> userTags =
        (ArrayList<String>) Arrays.asList(request.getParameterValues("tags"));

    sendData(new PersonBuilder().gitHubID(userGitHubID).interestTags(userTags).buildPerson());
  }

  // TODO: Add the person entity creation to the PersonObject/make it it's own class
  /**
   * Send a person entity to datastore with information about a given PersonObject @param person.
   *
   * @param person a person object
   */
  public void sendData(PersonObject person) {
    Entity personEntity = new Entity(PersonObject.ENTITY_NAME, person.getGitHubID());
    personEntity.setProperty(PersonObject.GITHUB_ID_FIELD, person.getGitHubID());
    personEntity.setProperty(PersonObject.TAG_LIST_FIELD, person.getTags());
    datastore.put(personEntity);
  }

  // TODO: use function in other servlets.
  /**
   * Query datastore for entities. comparisons are done with @param field coming first. For
   * example @param field EQUAL @param thingToBeComparedTo. Look at
   * https://cloud.google.com/appengine/docs/standard/java/javadoc/com/google/appengine/api/datastore/Query.FilterOperator
   * for more information on the @param operator
   *
   * @param EntityName the type of entity to be queried for
   * @param field the field that is being used to query
   * @param thingToBeCompareTo the object that will be compared to @param field
   * @param operator the type of comparison to be made.
   * @return
   */
  public PreparedQuery queryInDatabase(
      Entity EntityName, String field, Object thingToBeCompareTo, FilterOperator operator) {

    Filter userFilter = new FilterPredicate(field, operator, thingToBeCompareTo);

    return datastore.prepare(new Query(PersonObject.ENTITY_NAME).setFilter(userFilter));
  }

  /**
   * Convert an PersonObject.ENTITY_NAME entity retrieved from datastore into the person type.
   *
   * @param personEntity PersonObject.ENTITY_NAME entity
   * @return PersonObject that corresponds to the entity retrieved from datastore
   */
  public PersonObject toPersonObject(Entity personEntity) {
    PersonBuilder userBuilder = new PersonBuilder();

    String entityGitHubID = (String) personEntity.getProperty(PersonObject.GITHUB_ID_FIELD);
    userBuilder.gitHubID(entityGitHubID);
    ArrayList<String> entityTagList =
        (ArrayList<String>)
            Arrays.asList(personEntity.getProperty(PersonObject.TAG_LIST_FIELD)).stream()
                .map(tag -> (String) tag)
                .collect(Collectors.toList());
    userBuilder.interestTags(entityTagList);
    return userBuilder.buildPerson();
  }

  // TODO: make this conform with Richie's error handling
  /**
   * Sends an error to the client as raw text instead of the default HTML page.
   *
   * @param response the response from a given fetch
   * @param errorCode the error code to display when the error is flagged
   * @param errorMsg the error message to display when the error is flagged
   * @throws IOException
   */
  private void sendRawTextError(HttpServletResponse response, int errorCode, String errorMsg)
      throws IOException {
    response.setStatus(errorCode);
    response.setContentType("text/html;");
    response.getWriter().println(errorMsg);
  }
}
