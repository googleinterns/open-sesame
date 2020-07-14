package com.google.opensesame.servlets;

import com.googlecode.objectify.annotation.Subclass;
import java.util.ArrayList;

@Subclass(index = true)
public class MentorEntity extends PersonEntity {
  ArrayList<String> projectIDs;
  ArrayList<String> menteeIDs;

  public MentorEntity(
      PersonEntity person, ArrayList<String> projectIDs, ArrayList<String> menteeIDs) {
    super(person.id, person.interestTags, person.email);
    this.projectIDs = projectIDs;
    this.menteeIDs = menteeIDs;
  }
}
