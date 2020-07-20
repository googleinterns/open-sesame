package com.google.opensesame.user;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import java.util.ArrayList;

/**
 * The User Entity is designed to directly interface with datastore using Objectify. It holds the
 * neccesary information to generate a UserObject.
 */
@Entity
public class UserEntity {
  @Id public String userId;
  @Index Boolean isMentor;
  public String email;
  public String gitHubId;
  /** The interests a user indicated during signup */
  public ArrayList<String> interestTags;
  /** Datastore IDs of projects the given user has decided to mentor for. */
  public ArrayList<String> projectIDs = new ArrayList<String>();
  /** Datastore IDs of mentees that have expressed interest in being mentored by this user. */
  public ArrayList<String> menteeIDs = new ArrayList<String>();

  public UserEntity() {}

  /**
   * Create a User entity with information from a UserData Instance
   *
   * @param user
   */
  public UserEntity(UserData userData) {
    this.gitHubId = userData.getGitHubID();
    this.interestTags = userData.getTags();
    this.email = userData.getEmail();
    this.userId = userData.getUserID();
    this.projectIDs = userData.getProjectIDs();
    this.menteeIDs = userData.getMenteeIDs();
  }

  /**
   * Create a User entity with the given information. Use this when instantiating a User that is not
   * a mentor for any project.
   *
   * @param userId
   * @param gitHubId
   * @param interestTags
   * @param email
   */
  public UserEntity(String userId, String gitHubId, ArrayList<String> interestTags, String email) {
    this.userId = userId;
    this.gitHubId = gitHubId;
    this.interestTags = interestTags;
    this.email = email;
  }
  /**
   * Create a user with the given information. Use this to instantiate a User that is a mentor
   * without any mentees.
   *
   * @param userId
   * @param gitHubId
   * @param interestTags
   * @param email
   * @param projectIDs
   */
  public UserEntity(
      String userId,
      String gitHubId,
      ArrayList<String> interestTags,
      String email,
      ArrayList<String> projectIDs) {
    this(userId, gitHubId, interestTags, email);
    this.projectIDs = projectIDs;
  }

  /**
   * Create a User entity with the given information.
   *
   * @param userId
   * @param gitHubId
   * @param interestTags
   * @param email
   * @param projectIDs
   * @param menteeIDs
   */
  public UserEntity(
      String userId,
      String gitHubId,
      ArrayList<String> interestTags,
      String email,
      ArrayList<String> projectIDs,
      ArrayList<String> menteeIDs) {
    this(userId, gitHubId, interestTags, email, projectIDs);
    this.menteeIDs = menteeIDs;
  }

  @OnSave
  protected void setIsMentor() {
    this.isMentor = !projectIDs.isEmpty();
  }

  public boolean isMentor() {
    return !projectIDs.isEmpty();
  }

  /**
   * Add a project Id to the list of project Ids associated with this instance of the mentor object.
   * Remember to store this change in datastore with a personEntity.
   */
  public void addProject(String projectID) {
    projectIDs.add(projectID);
  }

  /**
   * Add a mentee Id to the list of mentee Ids associated with this instance of the mentor object.
   * Remember to store this change in datastore with a personEntity.
   */
  public void addMentee(String menteeID) {
    projectIDs.add(menteeID);
  }
}
