package com.google.opensesame.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.gson.Gson;
import com.google.opensesame.user.UserData;
import com.google.opensesame.user.UserEntity;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/mentor_breakout")
public class MentorBreakoutServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String userID = request.getParameter("mentorID");
    UserEntity mentor;
    try {
      mentor = ofy().load().type(UserEntity.class).id(userID).now();
    } catch (Exception e) {
      MentorsServlet.error(response, "Mentor not found.", 400, "Mentor not found");
      return;
    }
    if (!mentor.isMentor()) {
      MentorsServlet.error(
          response, "Requested user is not a mentor.", 400, "Non-mentor requested");
    }

    UserData mentorData = new UserData(mentor);
    String jsonMentor = new Gson().toJson(mentorData);
    response.setContentType("application/json;");
    response.getWriter().println(jsonMentor);
  }
}
