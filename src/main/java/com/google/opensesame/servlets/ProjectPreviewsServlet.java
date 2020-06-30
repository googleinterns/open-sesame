package com.google.opensesame.servlets;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.google.opensesame.projects.ProjectPreview;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/project-previews")
public class ProjectPreviewsServlet extends HttpServlet {

  private final Gson gson = new Gson();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Add validation and database querying here.
    Entity testEntity = new Entity("Project");
    testEntity.setProperty("repositoryName", "tensorflow/tensorflow");
    testEntity.setProperty("numMentors", 5);

    ArrayList<ProjectPreview> projectPreviews = new ArrayList<ProjectPreview>();
    projectPreviews.add(ProjectPreview.fromProperties(testEntity.getProperties()));

    String projectPreviewsJson = gson.toJson(projectPreviews);
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json;");
    response.getWriter().println(projectPreviewsJson);
  }
}
