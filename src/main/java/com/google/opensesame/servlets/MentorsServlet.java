package com.google.opensesame.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    MentorObject Obi = new MentorBuilder()
                 .name("Obi")
                 .gitHubID("Obinnabii")
                 .description("Obi is awesome.")
                 .interestTags(ObiSkills);
                 .projectIDs(projects);
                 .buildMentor();
    mentors.add(Obi);


    ArrayList<String> SamiSkills = new ArrayList<String>();
    SamiSkills.add("Stone carver");
    SamiSkills.add("Bootstrap convert");
    MentorObject Sami = new MentorBuilder()
                 .name("Sami")
                 .gitHubID("Sami-2000")
                 .description("Sami is fun.")
                 .interestTags(SamiSkills);
                 .projectIDs(projects);
                 .buildMentor();
    mentors.add(Sami);

    ArrayList<String> RichiSkills = new ArrayList<String>();
    RichiSkills.add("Minecraft boss");
    RichiSkills.add("React wizard");
    MentorObject Richi = new MentorBuilder()
                 .name("Richi")
                 .gitHubID("Richie78321")
                 .description("Richi is cool.")
                 .interestTags(RichiSkills);
                 .projectIDs(projects);
                 .buildMentor();
    mentors.add(Richi);
    
    String jsonMentors = new Gson().toJson(mentors);
    response.setContentType("application/json;");
    response.getWriter().println(jsonMentors);
  }
}
