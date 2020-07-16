package com.google.opensesame.servlets;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import java.util.ArrayList;

@Entity
public class PersonEntity {
  @Id String id;
  ArrayList<String> interestTags;
  String email;
  String gitHubId;

  public PersonEntity() {}

  public PersonEntity(UserObject user) {
    this.gitHubId = user.getGitHubID();
    this.interestTags = user.getTags();
    this.email = user.getEmail();
    this.id = user.getUserID();
  }

  public PersonEntity(String id, String gitHubId, ArrayList<String> interestTags, String email) {
    this.id = id;
    this.gitHubId = gitHubId;
    this.interestTags = interestTags;
    this.email = email;
  }
}
