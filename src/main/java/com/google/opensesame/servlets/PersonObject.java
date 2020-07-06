package com.google.opensesame.servlets;

import java.util.ArrayList;

public class PersonObject {
  private String name;
  private String description;
  private ArrayList<String> interestTags;
  private String gitHubID;

  public PersonObject(
      String name, String gitHubID, String description, ArrayList<String> interestTags) {
    this.name = name;
    this.gitHubID = gitHubID;
    this.description = description;
    this.interestTags = interestTags;
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

  public String getGitHubID() {
    return gitHubID;
  }
}
