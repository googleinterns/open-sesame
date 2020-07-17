package com.google.opensesame.servlets;

import com.googlecode.objectify.annotation.Subclass;
import java.util.ArrayList;


/**
 * The Mentor Entity is designed to directly interface with datastore using Objectify. It holds the
 * neccesary information to generate an UserObject that has chosen to mentor for a project.
 */
@Subclass(index = true)
public class MentorEntity extends PersonEntity {
  /** Datastore IDs of projects the given user has decided to mentor for. */
  ArrayList<String> projectIDs;
  /** Datastore IDs of mentees that have expressed interest in being mentored by this user. */
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
