package com.google.opensesame.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.gson.Gson;
import com.google.opensesame.projects.ProjectData;
import com.google.opensesame.projects.ProjectEntity;
import com.google.opensesame.util.ErrorResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/projects")
public class ProjectServlet extends HttpServlet {

  private final Gson gson = new Gson();

  private static final String PROJECT_ID_PARAM = "projectId";
  private static final String FULL_DATA_PARAM = "fullData";
  private static final String PREVIEW_DATA_PARAM = "previewData";

  /**
   * Returns a boolean parameter from a request. If the parameter does not exist, its value is
   * assumed to be false, but if the value does exist and it is not a valid boolean it returns
   * null.
   * @param request The request to get the parameter from.
   * @param parameter The name of the boolean parameter.
   * @return Returns the value of the parameter, the assumed value, or null if the value is not a
   *    valid boolean.
   */
  private static Boolean booleanFromRequest(HttpServletRequest request, String parameter) {
    String parameterValue = request.getParameter(parameter);
    if (parameterValue == null) {
      return Boolean.valueOf(false);
    }

    if (parameterValue.equalsIgnoreCase("true") || parameterValue.equalsIgnoreCase("false")) {
      return Boolean.valueOf(parameterValue);
    } else {
      return null;
    }
  }

  /**
   * Creates a collection of ProjectData from a collection of ProjectEntities.
   * @param projectEntities
   * @param getFullData
   * @param getPreviewData
   * @return Returns the created collection of ProjectData.
   * @throws IOException Throws if there was an error retrieving the GitHub repository associated
   *    with a ProjectEntity.
   */
  private static Collection<ProjectData> createProjectData(
      Collection<ProjectEntity> projectEntities, 
      boolean getFullData, 
      boolean getPreviewData) throws IOException {
    List<ProjectData> projectData = new ArrayList<ProjectData>();
    for (ProjectEntity projectEntity : projectEntities) {
      ProjectData newProjectData = new ProjectData(projectEntity);
      // TODO(Richie): Is there a better way to do this than an if-else block?
      if (getFullData) {
        newProjectData.withFullData();
      } else if (getPreviewData) {
        newProjectData.withPreviewData();
      } else {
        newProjectData.withName();
      }

      projectData.add(newProjectData);
    }

    return projectData;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Boolean getFullData = booleanFromRequest(request, FULL_DATA_PARAM);
    if (getFullData == null) {
      ErrorResponse.sendJsonError(
          response, 
          "Invalid boolean value for the " + FULL_DATA_PARAM + " parameter",
          HttpServletResponse.SC_BAD_REQUEST,
          "Failed to interpret the request. Please try again later.");
    }
    Boolean getPreviewData = booleanFromRequest(request, PREVIEW_DATA_PARAM);
    if (getPreviewData == null) {
      ErrorResponse.sendJsonError(
        response, 
        "Invalid boolean value for the " + PREVIEW_DATA_PARAM + " parameter",
        HttpServletResponse.SC_BAD_REQUEST,
        "Failed to interpret the request. Please try again later.");
    }

    String[] projectIds = request.getParameterValues(PROJECT_ID_PARAM);
    Collection<ProjectEntity> projectEntities;
    if (projectIds != null) {
      // Get projects by ID if IDs were specified.
      projectEntities = ofy().load().type(ProjectEntity.class).ids(projectIds).values();

      if (projectIds.length != projectEntities.size()) {
        ErrorResponse.sendJsonError(
          response, 
          "One or more of the supplied IDs is invalid and those projects could not be found.",
          HttpServletResponse.SC_NOT_FOUND,
          "One or more of the requested projects does not exist. Please try again later.");
      }
    } else {
      // TODO(Richie): Add parameters for ordering and filtering the query.
      // TODO(Richie): Add pagination support.
      // Get all projects in default ordering if no IDs are specified.
      projectEntities = ofy().load().type(ProjectEntity.class).order("-numMentors").list();
    }

    String projectDataJson = 
        gson.toJson(createProjectData(projectEntities, getFullData, getPreviewData));
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json;");
    response.getWriter().println(projectDataJson);
  }
}
