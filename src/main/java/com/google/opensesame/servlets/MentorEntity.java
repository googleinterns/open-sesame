package com.google.opensesame.servlets;

import com.googlecode.objectify.annotation.Subclass;
import java.util.ArrayList;

@Subclass(index = true)
public class MentorEntity extends PersonEntity {
  ArrayList<String> projectIDs;
  ArrayList<String> menteeIDs;

  private MentorEntity() {
    super();
  }

  public MentorEntity(UserObject user) {
    super(user);
    this.projectIDs = user.getProjectIDs();
    this.menteeIDs = user.getMenteeIDs();
  }

  public MentorEntity(
      PersonEntity person, ArrayList<String> projectIDs, ArrayList<String> menteeIDs) {
    super(person.id, person.gitHubId, person.interestTags, person.email);
    this.projectIDs = projectIDs;
    this.menteeIDs = menteeIDs;
  }
}
