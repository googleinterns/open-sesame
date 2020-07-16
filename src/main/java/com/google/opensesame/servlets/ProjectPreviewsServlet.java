package com.google.opensesame.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.opensesame.projects.ProjectEntity;
import com.google.opensesame.projects.ProjectPreviewData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    List<ProjectEntity> projectEntities =
        ofy().load().type(ProjectEntity.class).order("-numMentors").list();

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
