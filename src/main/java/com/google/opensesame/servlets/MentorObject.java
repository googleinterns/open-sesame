package com.google.opensesame.servlets;

import java.util.ArrayList;
import java.util.unmodifiableList;

public class MentorObject extends PersonObject {
  private ArrayList<String> projectIDs;

  public MentorObject(String name, String gitHubID, String description, ArrayList<String> interestTags, ArrayList<String> projectIDs) {
    super(name, gitHubID, description, interestTags);
    this.projectIDs = projectIDs;
  }

  public List<String> getProjectIDs() {
    return Collections.unmodifiableList(projectIDs);
  }

  public void addProject(String projectID) {
    projectIDs.add(projectID);
  }
}
