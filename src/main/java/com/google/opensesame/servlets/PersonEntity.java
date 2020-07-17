package com.google.opensesame.servlets;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import java.util.ArrayList;

/** 
 * The Person Entity is designed to directly interface with datastore using 
 * Objectify. It holds the neccesary information to generate an UserObject that
 * before the user has chosen to be a mentor for any project.
 */
@Entity
public class PersonEntity {
  @Id String id;
  /** The interests a user indicated during signup */
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
