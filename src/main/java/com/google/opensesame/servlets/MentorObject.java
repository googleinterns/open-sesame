package com.google.opensesame.servlets;

import java.util.ArrayList;

public class MentorObject extends PersonObject {
  private ArrayList<String> projectIDs;

  public MentorObject(String nameInput, String gitHubIDInput) {
    this(nameInput, gitHubIDInput, "", new ArrayList<String>(), new ArrayList<String>());  
  }

  public MentorObject(String nameInput, String gitHubIDInput, String descriptionInput) {
    this(nameInput, gitHubIDInput, descriptionInput, new ArrayList<String>(), new ArrayList<String>());
  }

  public MentorObject(String nameInput, String gitHubIDInput, String descriptionInput, ArrayList<String> tagInput) {
    this(nameInput, gitHubIDInput, descriptionInput, tagInput, new ArrayList<String>());
  }

  public MentorObject(String nameInput, String gitHubIDInput, String descriptionInput, ArrayList<String> tagInput, ArrayList<String> projectIDInputs) {
    super(nameInput, gitHubIDInput, descriptionInput, tagInput);
    projectIDs = projectIDInputs;
  }

  public ArrayList<String> getProjects() {
    return projectIDs;
  }

  public void addProject(String projectIDInput) {
    projectIDs.add(projectIDInput);
  }
}
