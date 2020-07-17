package com.google.opensesame.servlets;

import java.io.IOException;
import java.util.ArrayList;

// TODO: ReEvaluate the usefulness of a Builder after MVP presentation.
public class UserBuilder {
  private ArrayList<String> interestTags = new ArrayList<String>();
  private ArrayList<String> menteeIDs = new ArrayList<String>();
  private ArrayList<String> projectIDs = new ArrayList<String>();
  private String bio;
  private String email;
  private String gitHubID;
  private String userID;
  private String name;

  public UserBuilder() {}

  public UserBuilder name(String name) {
    this.name = name;
    return this;
  }

  public UserBuilder gitHubID(String gitHubID) {
    this.gitHubID = gitHubID;
    return this;
  }

  public UserBuilder bio(String bio) {
    this.bio = bio;
    return this;
  }

  public UserBuilder interestTags(ArrayList<String> interestTags) {
    this.interestTags = interestTags;
    return this;
  }

  public UserBuilder menteeIDs(ArrayList<String> interestTags) {
    this.interestTags = interestTags;
    return this;
  }

  public UserBuilder projectIDs(ArrayList<String> projectIDs) {
    this.projectIDs = projectIDs;
    return this;
  }

  public UserBuilder email(String email) {
    this.email = email;
    return this;
  }

  public UserBuilder userID(String userID) {
    this.userID = userID;
    return this;
  }

  /**
   * Create a PersonObject instance with information from the current builder.
   *
   * @return PersonObject that corresponds with feilkds from this UserBuilder instance
   * @throws IOException
   */
  public UserObject buildUser() throws IOException {
    return new UserObject(userID, gitHubID, interestTags, menteeIDs, projectIDs, email);
  }

  /**
   * Fill UserBuilder information from an entity. This will work on PersonEntities and
   * MentorEntities
   *
   * @param personEntity PersonEntity instance with user infromation
   * @return PersonObject that corresponds to the entity retrieved from datastore
   * @throws IOException
   */
  public UserBuilder fromEntity(PersonEntity personEntity) throws IOException {
    this.userID(personEntity.id);
    this.gitHubID(personEntity.gitHubId);
    this.interestTags(personEntity.interestTags);
    this.email(personEntity.email);
    if (personEntity instanceof MentorEntity) {
      MentorEntity mentorEntity = (MentorEntity) personEntity;
      this.projectIDs(mentorEntity.projectIDs);
      this.menteeIDs(mentorEntity.menteeIDs);
    }
    return this;
  }
}
