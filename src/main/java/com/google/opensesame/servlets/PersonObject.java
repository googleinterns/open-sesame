package com.google.opensesame.servlets;

import java.util.ArrayList;

public class PersonObject {
  private String name;
  private String description;
  private ArrayList<String> interestTags;
  private String gitHubID;

  public PersonObject(String nameInput, String gitHubIDInput) {
    this(nameInput, gitHubIDInput, "", new ArrayList<String>());
  }

  public PersonObject(String nameInput, String gitHubIDInput, String descriptionInput) {
    this(nameInput, gitHubIDInput, descriptionInput, new ArrayList<String>());
  }

  public PersonObject(
      String nameInput, String gitHubIDInput, String descriptionInput, ArrayList<String> tagInput) {
    name = nameInput;
    gitHubID = gitHubIDInput;
    description = descriptionInput;
    interestTags = tagInput;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public ArrayList<String> getTags() {
    return interestTags;
  }

  public String getGitID() {
    return gitHubID;
  }
}
