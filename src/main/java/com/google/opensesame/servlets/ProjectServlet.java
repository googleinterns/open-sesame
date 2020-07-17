package com.google.opensesame.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.gson.Gson;
import com.google.opensesame.projects.ProjectData;
import com.google.opensesame.projects.ProjectEntity;
import com.google.opensesame.util.ErrorResponse;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/project")
public class ProjectServlet extends HttpServlet {

  private final Gson gson = new Gson();

  private static final String PROJECT_ID_PARAM = "projectId";

  /**
   * @return Returns the request parameter associated with the inputted name, or returns the default
   *     value if the specified parameter is not defined.
   */
  public static String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String projectId = getParameter(request, PROJECT_ID_PARAM, null);
    if (projectId == null) {
      ErrorResponse.sendJsonError(
          response,
          "Must supply project ID.",
          HttpServletResponse.SC_BAD_REQUEST,
          "Failed to retrieve project data.");
      return;
    }

    ProjectEntity projectEntity = ofy().load().type(ProjectEntity.class).id(projectId).now();
    if (projectEntity == null) {
      ErrorResponse.sendJsonError(
          response,
          "A project with the specified project ID '" + projectId + "' does not exist.",
          HttpServletResponse.SC_BAD_REQUEST,
          "Failed to retrieve project data.");
      return;
    }

    ProjectData projectData = ProjectData.fromProjectEntity(projectEntity);
    String projectDataJson = gson.toJson(projectData);
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json;");
    response.getWriter().println(projectDataJson);
  }
}
