package com.google.opensesame.servlets;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.opensesame.projects.ProjectDatastore;
import com.google.opensesame.projects.ProjectEntity;
import com.google.opensesame.projects.ProjectPreviewData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/project-previews")
public class ProjectPreviewsServlet extends HttpServlet {

  private final Gson gson = new Gson();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {    
    Query projectsQuery = new Query("Project")
        .addSort(ProjectEntity.NUM_MENTORS_KEY, SortDirection.DESCENDING);
    
    List<ProjectEntity> projectEntities = ProjectDatastore.getByQuery(projectsQuery);
    List<ProjectPreviewData> projectPreviews = new ArrayList<ProjectPreviewData>();
    for (ProjectEntity projectEntity : projectEntities) {
      projectPreviews.add(ProjectPreviewData.fromProjectEntity(projectEntity));
    }

    String projectPreviewsJson = gson.toJson(projectPreviews);
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json;");
    response.getWriter().println(projectPreviewsJson);
  }
}
