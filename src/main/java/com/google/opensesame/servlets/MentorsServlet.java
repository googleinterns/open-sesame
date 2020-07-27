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
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    addMockMentor(response); // TODO: remove by production
    List<UserEntity> mentorEntities = ofy().load().type(UserEntity.class).list();
    ArrayList<UserData> mentors = new ArrayList<UserData>();
    for (UserEntity entity : mentorEntities) {
      if (entity.isMentor()) {
        UserData mentorData = new UserData(entity);
        mentors.add(mentorData);
      }
    }
    String jsonMentors = new Gson().toJson(mentors);
    response.setContentType("application/json;");
    response.getWriter().println(jsonMentors);
  }

  // Function to add a mock mentor to the database to use for formatting, testing, etc.
  /* Note: on the dev server we only have access to datastore entities added from our own
  computers. This function lets anyone demo a universal mock mentor. */
  public void addMockMentor(HttpServletResponse response) throws ServletException, IOException {
    GHRepository testRepo;
    GitHub gitHub = GitHubGetter.getGitHub();
    try {
      testRepo = gitHub.getRepository("googleinterns/step22-2020");
    } catch (Exception e) {
      error(response, "Failed to access test repo.", 400, "Test repo not found");
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
    ArrayList<String> projects = new ArrayList<String>();
    projects.add(testRepoID.toString());
    UserEntity mockMentor =
        new UserEntity(id, "Sami-2000", interests, "samialves@google.com", projects);
    ofy().save().entity(mockMentor);
  }

  // Send an error to response.
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
    ProjectEntity newProject = ProjectEntity.fromRepositoryIdOrNew(inputRepoID.toString());
    if (!newProject.mentorIds.contains(userID)) {
      newProject.mentorIds.add(userID);
    }
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
