package com.google.opensesame.servlets;

import java.util.ArrayList;

public class PersonBuilder {
  private String name;
  private String gitHubID;
  private String description = "";
  private ArrayList<String> interestTags = new ArrayList<String>();

  public PersonBuilder() {}

  public PersonObject buildPerson() {
    return new PersonObject(name, gitHubID, description, interestTags);
  }

  public PersonBuilder name(String name) {
    this.name = name;
    return this;
  }

  public PersonBuilder gitHubID(String gitHubID) {
    this.gitHubID = gitHubID;
    return this;
  }

  public PersonBuilder description(String description) {
    this.description = description;
    return this;
  }

  public PersonBuilder interestTags(ArrayList<String> interestTags) {
    this.interestTags = interestTags;
    return this;
  }
}
