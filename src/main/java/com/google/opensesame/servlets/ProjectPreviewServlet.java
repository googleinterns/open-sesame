package com.google.opensesame.servlets;

import com.google.gson.Gson;
import com.google.opensesame.projects.ProjectData;
import com.google.opensesame.projects.ProjectEntity;
import com.google.opensesame.projects.ProjectQuery;
import com.google.opensesame.util.ErrorResponse;
import com.google.opensesame.util.ServletValidationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/projects/preview")
public class ProjectPreviewServlet extends HttpServlet {
  /**
   * Creates project preview data from a ProjectEntity from datastore.
   *
   * @param projectEntity The ProjectEntity from datastore.
   * @return Returns the project preview data.
   * @throws IOException
   */
  public static ProjectData createProjectPreviewData(ProjectEntity projectEntity)
      throws IOException {
    ProjectData projectData = new ProjectData(projectEntity);
    projectData.getName();
    projectData.getDescription();
    projectData.getTopicTags();
    projectData.getPrimaryLanguage();
    projectData.getNumMentors();
    projectData.getRepositoryId();

    return projectData;
  }

  private static Gson gson = new Gson();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Collection<ProjectEntity> projectEntities;
    try {
      projectEntities = ProjectQuery.queryFromRequest(request);
    } catch (ServletValidationException e) {
      ErrorResponse.sendJsonError(response, e);
      return;
    }

    if (projectEntities != null) {
      List<ProjectData> projectData = new ArrayList<ProjectData>();
      for (ProjectEntity projectEntity : projectEntities) {
        projectData.add(createProjectPreviewData(projectEntity));
      }

      String projectDataJson = gson.toJson(projectData);
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json;");
      response.getWriter().println(projectDataJson);
    }
  }
}
