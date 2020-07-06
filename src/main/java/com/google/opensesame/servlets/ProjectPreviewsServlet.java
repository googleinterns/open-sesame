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
    ArrayList<Entity> testProjectEntities = new ArrayList<Entity>();
    Entity firstTestProjectEntity = new Entity("Project");
    firstTestProjectEntity.setProperty("repositoryName", "tensorflow/tensorflow");
    firstTestProjectEntity.setProperty("numMentors", 5);
    testProjectEntities.add(firstTestProjectEntity);

    Entity secondTestProjectEntity = new Entity("Project");
    secondTestProjectEntity.setProperty("repositoryName", "kubernetes/kubernetes");
    secondTestProjectEntity.setProperty("numMentors", 1);
    testProjectEntities.add(secondTestProjectEntity);

    ArrayList<ProjectPreview> projectPreviews = new ArrayList<ProjectPreview>();
    for (Entity projectEntity : testProjectEntities) {
      projectPreviews.add(ProjectPreview.fromProperties(projectEntity.getProperties()));
    }

    String projectPreviewsJson = gson.toJson(projectPreviews);
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json;");
    response.getWriter().println(projectPreviewsJson);
  }
}
