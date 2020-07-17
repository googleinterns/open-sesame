package com.google.opensesame.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.opensesame.auth.AuthServlet;
import com.google.opensesame.github.GitHubGetter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import static com.googlecode.objectify.ObjectifyService.ofy;

@WebServlet("/mentors")
public class MentorsServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ArrayList<String> interests = new ArrayList<String>();
    interests.add("skateboarding");   
    ArrayList<String> mentees = new ArrayList<String>();
    mentees.add("Richie");
    ArrayList<String> projects = new ArrayList<String>();
    mentees.add("OpenSesame");
    UserEntity mockMentor = new UserEntity("mock_id", "Sami-2000", interests, "samialves@google.com", projects, mentees);
    ofy().save().entity(mockMentor);

   
    List<UserEntity> mentorEntities = ofy().load().type(UserEntity.class).list();
    ArrayList<UserData> mentors = new ArrayList<UserData>();
    for (UserEntity entity : mentorEntities) {
      if(entity.isMentor) {
        mentors.add(UserData(entity));
      }
    }

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
    GHRepository inputRepo;
    try {
      inputRepo = gitHub.getRepository(repoName);
    } catch (Exception e) {
      error(response, "Repo not found: Please enter a valid repo url.", 400, "Repo not found");
      return;
    }

    String userID;
    try {
      userID = AuthServlet.getAuthorizedUser().getUserId();
    } catch (Exception e) {
      error(response, "You must be logged in.", 401, "User not logged in");
      return;
    }

    Long inputRepoID = inputRepo.getId();

    // Commented out until Richie's implementation of these functions is merged
    ProjectEntity newProject = fromRepositoryIdOrNew(inputRepoID);
    ofy().save().entity(newProject);
    
    UserEntity user;
    try {
      user = ofy().load().type(UserEntity.class).id(userID).now();
    } catch (Exception e) {
      error(response, "You must sign up for opensesame first.", 401, "User not registered.");
      return;
    }
    
    if (!user.projectIDs.contains(Long.toString(inputRepoID))) {
      user.projectIDs.add(Long.toString(inputRepoID));
    }

    ofy().save().entity(user);

    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson("success"));
    return;
  }
}
