package com.google.opensesame.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.opensesame.auth.AuthServlet;
import com.google.opensesame.github.GitHubGetter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
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
    ArrayList<MentorObject> mentors = new ArrayList<MentorObject>();
    ArrayList<String> projects = new ArrayList<String>();
    projects.add("OpenSesame");

    ArrayList<String> ObiSkills = new ArrayList<String>();
    ObiSkills.add("Meme god");
    ObiSkills.add("HTML wrangler");
    MentorObject Obi =
        new PersonBuilder()
            .name("Obi")
            .gitHubID("Obinnabii")
            .description("Obi is awesome.")
            .interestTags(ObiSkills)
            .projectIDs(projects)
            .buildMentor();
    mentors.add(Obi);

    ArrayList<String> SamiSkills = new ArrayList<String>();
    SamiSkills.add("Stone carver");
    SamiSkills.add("Bootstrap convert");
    MentorObject Sami =
        new PersonBuilder()
            .name("Sami")
            .gitHubID("Sami-2000")
            .description("Sami is fun.")
            .interestTags(SamiSkills)
            .projectIDs(projects)
            .buildMentor();
    mentors.add(Sami);

    ArrayList<String> RichiSkills = new ArrayList<String>();
    RichiSkills.add("Minecraft boss");
    RichiSkills.add("React wizard");
    MentorObject Richi =
        new PersonBuilder()
            .name("Richi")
            .gitHubID("Richie78321")
            .description("Richi is cool.")
            .interestTags(RichiSkills)
            .projectIDs(projects)
            .buildMentor();
    mentors.add(Richi);

    String jsonMentors = new Gson().toJson(mentors);
    response.setContentType("application/json;");
    response.getWriter().println(jsonMentors);
  }

  //This function prints an error message to the mentor form page if invalid input is found.
  public void formError(HttpServletRequest request, HttpServletResponse response, String error)  
      throws ServletException, IOException {
    response.setContentType("application/json");
    response.getWriter().println(new Gson().toJson(error));
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
    } catch(Exception e) {
      System.out.println("Couldn't find repo\n");
      formError(request, response, "COULDN'T FIND REPO");
      return;
    }

    //TODO: Send Richie the GHRepository ID and person ID to add to projects database
    //      Send to a function in the ProjectEntity.java class

    String userID = AuthServlet.getAuthorizedUser().getUserId();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    try {
      Entity personEntity = datastore.get(KeyFactory.stringToKey(userID));
    } catch (EntityNotFoundException e) {
      // TODO: Handle this exception (which should never occur if datastore is working properly)
    }

    // TODO: Add a mentor entity as a child of the user entity.

    response.setContentType("application/json");
    response.getWriter().println(new Gson().toJson("success"));
  }
}
