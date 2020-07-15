package com.google.opensesame.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;
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

  public PersonEntity(PersonObject person) {
    this.gitHubId = person.getGitHubID();
    this.interestTags = person.getTags();
    this.email = person.getEmail();
  }

  public PersonEntity(String id, String gitHubId, ArrayList<String> interestTags, String email) {
    this.id = id;
    this.gitHubId = gitHubId;
    this.interestTags = interestTags;
    this.email = email;
  }
}
