package com.google.opensesame.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.opensesame.auth.AuthServlet;
import com.google.opensesame.github.GitHubGetter;
import com.google.opensesame.projects.ProjectEntity;
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
    Query<MentorEntity> mentors = ofy().load().type(MentorEntity.class);
    ArrayList<MentorObject> mentors = new ArrayList<MentorObject>();
    for (MentorEntity mentor : mentors) {
      mentors.add(fromMentorEntity(mentor));
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

    String inputRepoID = inputRepo.getId();
    ProjectEntity newProject = getByRepositoryIdOrNew(inputRepoID);
    if (!newProject.mentorIds.contains(userID)) {
      newProject.mentorIds.add(userID);
      newProject.save();
    }

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    // This section is commented out until we have populated datastore to refer to.
    PersonEntity user;
    try {
      user = ofy().load().type(PersonEntity.class).id(userID);
    } catch (Exception e) {
      error(response, "You must sign up for opensesame first.", 401, "User not registered.");
      return;
    }

    if (user instanceof MentorEntity) {
      MentorEntity mentor = (MentoryEntity) user;
      mentor.projectIDs.add(inputRepoID);
      ofy().save().entity(mentor);
    } else {
      ArrayList<String> projects = new ArrayList<String>();
      projects.add(inputRepoID);
      ArrayList<String> mentees = new ArrayList<String>();
      MentorEntity mentor = new MentorEntity(user, projects, mentees);
      ofy().save().entity(mentor);
    }

    // TODO: Add a mentor entity as a child of the user entity.
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson("success"));
    return;
  }
}
