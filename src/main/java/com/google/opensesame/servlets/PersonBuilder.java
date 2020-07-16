package com.google.opensesame.servlets;

import java.io.IOException;
import java.util.ArrayList;

// TODO: ReEvaluate the usefulness of a Builder after MVP presentation.
public class PersonBuilder {
  public static String ENTITY_NAME = "User";
  public static String GITHUB_ID_FIELD = "github-id";
  public static String TAG_LIST_FIELD = "tags";
  public static String EMAIL_FEILD = "email";

  private ArrayList<String> interestTags = new ArrayList<String>();
  private ArrayList<Long> projectIDs = new ArrayList<Long>();
  private String description;
  private String email;
  private String gitHubID;
  private String userID;
  private String name;

  public PersonBuilder() {}

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

  public PersonBuilder projectIDs(ArrayList<Long> projectIDs) {
    this.projectIDs = projectIDs;
    return this;
  }

  public PersonBuilder email(String email) {
    this.email = email;
    return this;
  }

  public PersonBuilder userID(String userID) {
    this.userID = userID;
    return this;
  }

  /**
   * Create a PersonObject instance with information from the current builder.
   *
   * @return PersonObject that corresponds with feilkds from this PersonBuilder instance
   * @throws IOException
   */
  public PersonObject buildPerson() throws IOException {
    return new PersonObject(userID, gitHubID, interestTags, email);
  }

  /**
   * Fill PersonBuilder information from an entity. This will work on PersonEntities and
   * MentorEntities
   *
   * @param personEntity PersonEntity instance with user infromation
   * @return PersonObject that corresponds to the entity retrieved from datastore
   * @throws IOException
   */
  public PersonBuilder fromEntity(PersonEntity personEntity) throws IOException {
    this.userID(personEntity.id);
    this.gitHubID(personEntity.gitHubId);
    this.interestTags(personEntity.interestTags);
    this.email(personEntity.email);
    if (personEntity instanceof MentorEntity) {
      MentorEntity mentorEntity = (MentorEntity) personEntity;
      this.projectIDs(mentorEntity.projectIDs);
    }
    return this;
  }

  /**
   * Create a MentorObject instance with information from the current builder.
   *
   * @return MentorObject that corresponds with feilds from this PersonBuilder instance
   * @throws IOException
   */
  public MentorObject buildMentor() throws IOException {
    return new MentorObject(userID, gitHubID, interestTags, projectIDs, email);
  }
}
