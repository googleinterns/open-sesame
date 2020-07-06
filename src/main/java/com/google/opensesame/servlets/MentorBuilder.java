package com.google.opensesame.servlets;

import java.util.ArrayList;

public class MentorBuilder {
  private String name;
  private String gitHubID;
  private String description = "";
  private ArrayList<String> interestTags = new ArrayList<String>();
  private ArrayList<String> projectIDs = new ArrayList<String>();

  public MentorBuilder() {}

  public MentorObject buildMentor() {
    return new MentorObject(name, gitHubID, description, interestTags, projectIDs);
  }

  public MentorBuilder name(String name) {
    this.name = name;
    return this;
  }

  public MentorBuilder gitHubID(String gitHubID) {
    this.gitHubID = gitHubID;
    return this;
  }

  public MentorBuilder description(String description) {
    this.description = description;
    return this;
  }

  public MentorBuilder interestTags(ArrayList<String> interestTags) {
    this.interestTags = interestTags;
    return this;
  }

  public MentorBuilder projectIDs(ArrayList<String> projectIDs) {
    this.projectIDs = projectIDs;
    return this;
  }
}
