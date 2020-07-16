package com.google.opensesame.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MentorObject extends PersonObject {
  private ArrayList<Long> projectIDs;

  public MentorObject(
      String userID,
      String gitHubID,
      ArrayList<String> interestTags,
      ArrayList<Long> projectIDs,
      String email)
      throws IOException {
    super(userID, gitHubID, interestTags, email);
    this.projectIDs = projectIDs;
  }

  public List<Long> getProjectIDs() {
    return Collections.unmodifiableList(projectIDs);
  }

  public void addProject(Long projectID) {
    projectIDs.add(projectID);
  }
}
