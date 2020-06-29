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
    mentors.add(new MentorObject("Obi", "Obinnabii", "Obi is awesome.", ObiSkills, projects));


    ArrayList<String> SamiSkills = new ArrayList<String>();
    SamiSkills.add("Stone carver");
    SamiSkills.add("Bootstrap convert");
    mentors.add(new MentorObject("Sami", "Sami-2000", "Sami is fun.", SamiSkills, projects));

    ArrayList<String> RichiSkills = new ArrayList<String>();
    RichiSkills.add("Minecraft boss");
    RichiSkills.add("React wizard");
    mentors.add(new MentorObject("Richi", "Richie78321", "Richi is cool.", RichiSkills, projects));
    
    String jsonMentors = new Gson().toJson(mentors);
    response.setContentType("application/json;");
    response.getWriter().println(jsonMentors);
  }
}
