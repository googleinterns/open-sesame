package com.google.opensesame.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MentorObject extends PersonObject {
  private ArrayList<String> projectIDs;

  public MentorObject(
      String gitHubID, ArrayList<String> interestTags, ArrayList<String> projectIDs, String email)
      throws IOException {
    super(gitHubID, interestTags, email);
    this.projectIDs = projectIDs;
  }

  public List<String> getProjectIDs() {
    return Collections.unmodifiableList(projectIDs);
  }

  public void addProject(String projectID) {
    projectIDs.add(projectID);
  }
}
