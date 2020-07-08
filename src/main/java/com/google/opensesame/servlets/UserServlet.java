package com.google.opensesame.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

  // Dummy list of people @TODO deprecate this
  private List<PersonObject> people;

  @Override
  // Instantiate the dummy list of people
  public void init() {
    people = new ArrayList<PersonObject>();
    ArrayList<String> projectIDs = new ArrayList<String>();
    projectIDs.add("OpenSesame");

    ArrayList<String> ObiSkills = new ArrayList<String>();
    ObiSkills.add("Meme god");
    ObiSkills.add("HTML wrangler");
    PersonObject Obi =
        new PersonBuilder()
            .name("Obi")
            .gitHubID("Obinnabii")
            .description("Obi is awesome.")
            .interestTags(ObiSkills)
            .projectIDs(projectIDs)
            .buildPerson();
    people.add(Obi);

    ArrayList<String> SamiSkills = new ArrayList<String>();
    SamiSkills.add("Stone carver");
    SamiSkills.add("Bootstrap convert");
    PersonObject Sami =
        new PersonBuilder()
            .name("Sami")
            .gitHubID("Sami-2000")
            .description("Sami is fun.")
            .interestTags(SamiSkills)
            .projectIDs(projectIDs)
            .buildPerson();
    people.add(Sami);

    ArrayList<String> RichiSkills = new ArrayList<String>();
    RichiSkills.add("Minecraft boss");
    RichiSkills.add("React wizard");
    PersonObject Richi =
        new PersonBuilder()
            .name("Richi")
            .gitHubID("Richie78321")
            .description("Richi is cool.")
            .interestTags(RichiSkills)
            .projectIDs(projectIDs)
            .buildPerson();
    people.add(Richi);
  }

  @Override
  // get a specific user. return null if not found.
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String userString = request.getParameter("user");

    PersonObject result =
        people.stream()
            .filter(person -> userString.equals(person.getGitHubID()))
            .findAny()
            .orElse(null);
    if (result == null) {
      sendRawTextError(
          response, HttpServletResponse.SC_BAD_REQUEST, "invalid user or user does not exist");
    }

    String jsonPerson = new Gson().toJson(result);
    response.setContentType("application/json;");
    response.getWriter().println(jsonPerson);
  }

  /** Sends an error to the client as raw text instead of the default HTML page. */
  private void sendRawTextError(HttpServletResponse response, int errorCode, String errorMsg)
      throws IOException {
    response.setStatus(errorCode);
    response.setContentType("text/html;");
    response.getWriter().println(errorMsg);
  }
}
