package com.google.opensesame.servlets;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.users.User;
import com.google.gson.Gson;
import com.google.opensesame.github.GitHubGetter;
import com.google.opensesame.auth.AuthServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
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
  public void formError(HttpServletRequest request, HttpServletResponse response, String error)  throws ServletException, IOException {
    System.out.println("form error!");
    response.setContentType("application/json");
    response.getWriter().println(new Gson().toJson(error));
    /*String message = "<ul><li class='ml-3 display-4 font-weight-bold text-danger'>"   + error + "</li></ul>";
    response.setContentType("text/html");
    PrintWriter pw = response.getWriter();
    pw.print(message);
    RequestDispatcher rd = request.getRequestDispatcher("mentor_form.html");
    rd.include(request, response);
    pw.close();*/
    return;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("request is " + request);
    String repoUrl = request.getParameter("inputRepo");
    System.out.println("repo is " + repoUrl);
    /*String urlPattern = "https://github.com/";
    Pattern regEx = Pattern.compile(urlPattern);
    Matcher inputMatcher = regEx.matcher(repoUrl);
    if (!inputMatcher.find()) {
      //Input is not in the format of a GitHub repo link.
      formError(request, response, "ENTER A VALID REPO LINK");
      return;
    }*/

    String repoName = repoUrl.replaceFirst("https://github.com/","");
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
    /*try { 
      Entity personEntity = datastore.get(KeyFactory.stringToKey(userID)); 
    } catch (Exception e) {
      formError(request, response, "COULDNT FIND U IN DATASTORE");
      return;
      //TODO: Handle this exception (which should never occur if datastore is working properly)
    }*/

    //TODO: Add a mentor entity as a child of the user entity.

    response.setContentType("application/json");
    response.getWriter().println(new Gson().toJson("success"));
  }
}
