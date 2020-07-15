package com.google.opensesame.servlets;

import com.google.appengine.api.datastore.Entity;
import java.io.IOException;
import java.util.ArrayList;

// TODO: Pivot away from builder if it stays redundant by the next PR
public class PersonBuilder {
  public static String ENTITY_NAME = "User";
  public static String GITHUB_ID_FIELD = "github-id";
  public static String TAG_LIST_FIELD = "tags";

  private String gitHubID;
  private ArrayList<String> interestTags = new ArrayList<String>();
  private ArrayList<String> projectIDs = new ArrayList<String>();
  private String description;
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

  public PersonBuilder projectIDs(ArrayList<String> projectIDs) {
    this.projectIDs = projectIDs;
    return this;
  }

  /**
   * Create a PersonObject instance with information from the current builder.
   *
   * @return PersonObject that corresponds with feilkds from this PersonBuilder instance
   * @throws IOException
   */
  public PersonObject buildPersonObject() throws IOException {
    return new PersonObject(gitHubID, interestTags);
  }

  /**
   * Convert an entity retrieved from Datastore into a PersonObject.
   *
   * @param personEntity PersonObject.ENTITY_NAME entity
   * @return PersonObject that corresponds to the entity retrieved from datastore
   * @throws IOException
   */
  public PersonObject buildPersonObject(Entity personEntity) throws IOException {
    String entityGitHubID = (String) personEntity.getProperty(GITHUB_ID_FIELD);
    this.gitHubID(entityGitHubID);
    ArrayList<String> entityTagList = (ArrayList<String>) personEntity.getProperty(TAG_LIST_FIELD);
    this.interestTags(entityTagList);
    return this.buildPersonObject();
  }

  /**
   * Create a MentorObject instance with information from the current builder.
   *
   * @return MentorObject that corresponds with feilds from this PersonBuilder instance
   * @throws IOException
   */
  public MentorObject buildMentor() throws IOException {
    return new MentorObject(gitHubID, interestTags, projectIDs);
  }

  /**
   * build an entity with information about a person.
   *
   * @return entity containing information from the personbuilder
   */
  public Entity buildPersonEntity() {
    Entity personEntity = new Entity(ENTITY_NAME, this.gitHubID);
    personEntity.setProperty(GITHUB_ID_FIELD, this.gitHubID);
    personEntity.setProperty(TAG_LIST_FIELD, this.interestTags);
    return personEntity;
  }
}
