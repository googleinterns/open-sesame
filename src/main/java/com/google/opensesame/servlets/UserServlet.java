package com.google.opensesame.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.search.query.QueryParser.query_return;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
  // get a specific user. return null if not found.
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String userGithub = request.getParameter("githubID");
    Key userKey = 
    KeyFactory.createKey(PersonObject.ENTITY_NAME, userGithub)

    PersonObject userObject;

    try{
      userObject = toPersonObject(datastore.get(userKey));
    } catch (Exception e) {
      sendRawTextError(response, 
      HttpServletResponse.SC_BAD_REQUEST, 
      "Person does not exist in Datastore");
      userObject = null;
    }

    String jsonPerson = new Gson().toJson(userObject);
    response.setContentType("application/json;");
    response.getWriter().println(jsonPerson);
  }

  @Override
  // get a specific user. return null if not found.
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String userGitHubID = request.getParameter("gitHubID");
    ArrayList<String> userTags = 
        (ArrayList<String>) Arrays.asList(request.getParameterValues("tags"));

   sendData(new PersonBuilder()
            .gitHubID(userGitHubID)
            .interestTags(userTags)
            .buildPerson());

  }

  public void sendData( PersonObject person ){
    Key key = 
        KeyFactory.createKey(PersonObject.ENTITY_NAME, person.getGitHubID());
    Entity personEntity = new Entity(PersonObject.ENTITY_NAME, key);
    personEntity.setProperty(PersonObject.GITHUB_ID_FIELD,person.getGitHubID());
    personEntity.setProperty(PersonObject.TAG_LIST_FIELD, person.getTags());
    datastore.put(personEntity);
  }

  public ArrayList<PersonObject>  queryInDatabase( String userGithub ) {
    ArrayList<PersonObject> users = new ArrayList<PersonObject>();

    Filter userFilterByGitHubID = 
        new FilterPredicate(PersonObject.GITHUB_ID_FIELD, 
            FilterOperator.EQUAL, 
            userGithub);
    
    Query queryByGitHubID = new Query(PersonObject.ENTITY_NAME)
        .setFilter(userFilterByGitHubID);
    PreparedQuery results = datastore.prepare(queryByGitHubID); 

    for (Entity personEntity : results.asIterable()) {
      PersonObject user = toPersonObject(personEntity);
      users.add(user);
    }

    return users;
  }

  public PersonObject toPersonObject (Entity personEntity) {
    PersonBuilder userBuilder = new PersonBuilder();

    String entityGitHubID = (String) personEntity.getProperty(PersonObject.GITHUB_ID_FIELD);
    userBuilder.gitHubID(entityGitHubID);
    ArrayList<String> entityTagList = (ArrayList<String>) Arrays.asList(
        personEntity.getProperty(PersonObject.TAG_LIST_FIELD))
        .stream()
        .map(tag -> (String) tag)
        .collect(Collectors.toList());
    userBuilder.interestTags(entityTagList);
    return userBuilder.buildPerson();
  }


  /**
   * Sends an error to the client as raw text instead of the default HTML page.
   */
  private void sendRawTextError(HttpServletResponse response, int errorCode, String errorMsg) 
      throws IOException {
    response.setStatus(errorCode);
    response.setContentType("text/html;");
    response.getWriter().println(errorMsg);
  }
}
