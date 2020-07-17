package com.google.opensesame.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.opensesame.auth.AuthServlet;
import com.google.opensesame.github.GitHubGetter;
import com.google.opensesame.user.UserData;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

@WebServlet("/mentors")
public class MentorsServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ArrayList<UserData> mentors = new ArrayList<UserData>();
    ArrayList<String> projects = new ArrayList<String>();
    projects.add("OpenSesame");

    ArrayList<String> ObiSkills = new ArrayList<String>();
    ObiSkills.add("Meme god");
    ObiSkills.add("HTML wrangler");
    UserData Obi = null;
    mentors.add(Obi);

    ArrayList<String> SamiSkills = new ArrayList<String>();
    SamiSkills.add("Stone carver");
    SamiSkills.add("Bootstrap convert");
    UserData Sami = null;
    mentors.add(Sami);

    ArrayList<String> RichiSkills = new ArrayList<String>();
    RichiSkills.add("Minecraft boss");
    RichiSkills.add("React wizard");
    UserData Richi = null;
    mentors.add(Richi);

    String jsonMentors = new Gson().toJson(mentors);
    response.setContentType("application/json;");
    response.getWriter().println(jsonMentors);
  }

  // This function sends an error if invalid input is found.
  public void error(
      HttpServletResponse response, String errorMessage, int statusCode, String userMessage)
      throws ServletException, IOException {
    JsonObject responseObject = new JsonObject();
    responseObject.addProperty("message", errorMessage);
    responseObject.addProperty("statusCode", statusCode);
    responseObject.addProperty("userMessage", userMessage);
    String responseJson = new Gson().toJson(responseObject);
    response.setStatus(statusCode);
    response.setContentType("application/json;");
    response.getWriter().println(responseJson);
    return;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String repoUrl = request.getParameter("inputRepo");
    String repoName = repoUrl.replaceFirst("https://github.com/", "");
    GitHub gitHub = GitHubGetter.getGitHub();
    try {
      GHRepository inputRepo = gitHub.getRepository(repoName);
    } catch (Exception e) {
      error(response, "Repo not found: Please enter a valid repo url.", 400, "Repo not found");
      return;
    }

    // TODO: Send Richie the GHRepository ID and person ID to add to projects database
    //      Send to a function in the ProjectEntity.java class

    String userID = AuthServlet.getAuthorizedUser().getUserId();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    // This section is commented out until we have populated datastore to refer to.
    /*try {
      Entity personEntity = datastore.get(KeyFactory.stringToKey(userID));
    } catch (EntityNotFoundException e) {
      error(response, "You must be logged in.", 401, "User not logged in.");
      // TODO: Handle this exception (which should never occur if datastore is working properly)
    }*/

    // TODO: Add a mentor entity as a child of the user entity.
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson("success"));
    return;
  }
}
