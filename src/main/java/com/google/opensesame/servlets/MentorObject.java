package com.google.opensesame.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MentorObject extends PersonObject {
  private ArrayList<Long> projectIDs;
  private ArrayList<String> menteeIDs;

  public MentorObject(
      String userID,
      String gitHubID,
      ArrayList<String> interestTags,
      ArrayList<String> menteeIDs,
      ArrayList<Long> projectIDs,
      String email)
      throws IOException {
    super(userID, gitHubID, interestTags, email);
    this.projectIDs = projectIDs;
    this.menteeIDs = menteeIDs;
  }

  public List<Long> getProjectIDs() {
    return Collections.unmodifiableList(projectIDs);
  }

  public List<String> getMenteeIDs() {
    return Collections.unmodifiableList(menteeIDs);
  }

  public void addProject(Long projectID) {
    projectIDs.add(projectID);
  }

  public void addMentee(Long menteeID) {
    projectIDs.add(menteeID);
  }
}
