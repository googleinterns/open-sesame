package com.google.opensesame.servlets;

import java.util.ArrayList;
import java.util.unmodifiableList;

public class MentorObject extends PersonObject {
  private ArrayList<String> projectIDs;

<<<<<<< HEAD
  public MentorObject(String name, String gitHubID, String description, ArrayList<String> interestTags, ArrayList<String> projectIDs) {
    super(name, gitHubID, description, interestTags);
    this.projectIDs = projectIDs;
  }

  public List<String> getProjectIDs() {
    return Collections.unmodifiableList(projectIDs);
  }

  public void addProject(String projectID) {
    projectIDs.add(projectID);
=======
  public MentorObject(String nameInput, String gitHubIDInput) {
    this(nameInput, gitHubIDInput, "", new ArrayList<String>(), new ArrayList<String>());
  }

  public MentorObject(String nameInput, String gitHubIDInput, String descriptionInput) {
    this(
        nameInput,
        gitHubIDInput,
        descriptionInput,
        new ArrayList<String>(),
        new ArrayList<String>());
  }

  public MentorObject(
      String nameInput, String gitHubIDInput, String descriptionInput, ArrayList<String> tagInput) {
    this(nameInput, gitHubIDInput, descriptionInput, tagInput, new ArrayList<String>());
  }

  public MentorObject(
      String nameInput,
      String gitHubIDInput,
      String descriptionInput,
      ArrayList<String> tagInput,
      ArrayList<String> projectIDInputs) {
    super(nameInput, gitHubIDInput, descriptionInput, tagInput);
    projectIDs = projectIDInputs;
  }

  public ArrayList<String> getProjects() {
    return projectIDs;
  }

  public void addProject(String projectIDInput) {
    projectIDs.add(projectIDInput);
>>>>>>> cfc4caf944167dc735648558cead0996d8c35bf9
  }
}
