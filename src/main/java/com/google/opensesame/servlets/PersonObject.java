package com.google.opensesame.servlets;

import java.util.ArrayList;

public class PersonObject {
  public static String ENTITY_NAME = "User";
  public static String GITHUB_ID_FIELD = "github-id";
  public static String TAG_LIST_FIELD = "tags";

  private String name;
  private String description;
  private ArrayList<String> interestTags;
  private String gitHubID;
  private String email;

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

  public String getEmail() {
    return email;
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
