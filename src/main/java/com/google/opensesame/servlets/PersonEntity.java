package com.google.opensesame.servlets;

import com.google.appengine.api.datastore.Entity;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import java.util.ArrayList;

@Entity
public class PersonEntity {
  @Id String id;
  ArrayList<String> interestTags;
  String email;

  public PersonEntity(PersonObject person) {
    this.id = person.getGitHubID();
    this.interestTags = person.getTags();
    this.email = person.getEmail();
  }

  public PersonEntity(String gitHubID, ArrayList<String> interestTags, String email) {
    this.id = gitHubID;
    this.interestTags = interestTags;
    this.email = email;
  }
}
