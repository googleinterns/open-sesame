package com.google.opensesame.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.opensesame.auth.AuthServlet;
import com.google.opensesame.github.GitHubGetter;
import com.google.opensesame.projects.ProjectEntity;
import com.google.opensesame.user.UserData;
import com.google.opensesame.user.UserEntity;
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

@WebServlet("/mentors")
public class MentorsServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<UserEntity> mentorEntities = ofy().load().type(UserEntity.class).list();
    ArrayList<UserData> mentors = new ArrayList<UserData>();
    for (UserEntity entity : mentorEntities) {
      if (entity.isMentor()) {
        mentors.add(new UserData(entity));
      }
    }
    String jsonMentors = new Gson().toJson(mentors);
    response.setContentType("application/json;");
    response.getWriter().println(jsonMentors);
  }

  // Add a mock sami object to the datastore
  public void addMockMentor() throws ServletException, IOException {
    GHRepository testRepo;
    GitHub gitHub = GitHubGetter.getGitHub();
    try {
      testRepo = gitHub.getRepository("googleinterns/step22-2020");
    } catch (Exception e) {
      System.out.println("COULDN'T FIND REPO");
      return;
    }
    Long testRepoID = testRepo.getId();
    ProjectEntity testProject = ProjectEntity.fromRepositoryIdOrNew(testRepoID.toString());
    String id = "mock_id";
    if (!testProject.mentorIds.contains(id)) {
      testProject.mentorIds.add(id);
    }
    ofy().save().entity(testProject);
    ArrayList<String> interests = new ArrayList<String>();
    interests.add("skateboarding");
    ArrayList<String> mentees = new ArrayList<String>();
    ArrayList<String> projects = new ArrayList<String>();
    projects.add(testRepoID.toString());
    UserEntity mockMentor =
        new UserEntity(id, "Sami-2000", interests, "samialves@google.com", projects, mentees);
    ofy().save().entity(mockMentor);
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
    ProjectEntity newProject = ProjectEntity.fromRepositoryIdOrNew(inputRepoID.toString());
    newProject.mentorIds.add(userID);
    ofy().save().entity(newProject).now();

    UserEntity user;
    try {
      user = ofy().load().type(UserEntity.class).id(userID).now();
    } catch (Exception e) {
      error(response, "You must sign up for opensesame first.", 401, "User not registered.");
      return;
    }

    if (!user.projectIds.contains(Long.toString(inputRepoID))) {
      user.projectIds.add(Long.toString(inputRepoID));
    }

    ofy().save().entity(user);

    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson("success"));
    return;
  }
}
