package com.google.opensesame.servlets;

import com.google.appengine.api.datastore.Entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PersonBuilder {
  private String name;
  private String gitHubID;
  private String description = "";
  private ArrayList<String> interestTags = new ArrayList<String>();
  private ArrayList<String> projectIDs = new ArrayList<String>();

  public PersonBuilder() {}

  public PersonObject buildPerson() throws IOException {
    return new PersonObject(gitHubID, interestTags);
  }

  public MentorObject buildMentor() throws IOException {
    return new MentorObject(gitHubID, interestTags, projectIDs);
  }

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
   * Convert an entity retrieved from Datastore into the Person type.
   *
   * @param personEntity PersonObject.ENTITY_NAME entity
   * @return PersonObject that corresponds to the entity retrieved from datastore
   * @throws IOException
   */
  public PersonObject userFromEntity(Entity personEntity) throws IOException {
    PersonBuilder userBuilder = new PersonBuilder();

    String entityGitHubID = (String) personEntity.getProperty(PersonObject.GITHUB_ID_FIELD);
    userBuilder.gitHubID(entityGitHubID);
    ArrayList<String> entityTagList =
        (ArrayList<String>)
            Arrays.asList(personEntity.getProperty(PersonObject.TAG_LIST_FIELD)).stream()
                .map(tag -> (String) tag)
                .collect(Collectors.toList());
    userBuilder.interestTags(entityTagList);
    return userBuilder.buildPerson();
  }
}
