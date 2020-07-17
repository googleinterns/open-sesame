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
  @Id String userId;
  @Index Boolean isMentor;
  String email;
  String gitHubId;
  /** The interests a user indicated during signup */
  ArrayList<String> interestTags;
  /** Datastore IDs of projects the given user has decided to mentor for. */
  ArrayList<String> projectIDs = new ArrayList<String>();
  /** Datastore IDs of mentees that have expressed interest in being mentored by this user. */
  ArrayList<String> menteeIDs = new ArrayList<String>();

  public UserEntity() {}

  public UserEntity(UserData user) {
    this.gitHubId = user.getGitHubID();
    this.interestTags = user.getTags();
    this.email = user.getEmail();
    this.userId = user.getUserID();
    this.projectIDs = user.getProjectIDs();
    this.menteeIDs = user.getMenteeIDs();
  }

  public UserEntity(String userId, String gitHubId, ArrayList<String> interestTags, String email) {
    this.userId = userId;
    this.gitHubId = gitHubId;
    this.interestTags = interestTags;
    this.email = email;
  }

  public UserEntity(
      String userId,
      String gitHubId,
      ArrayList<String> interestTags,
      String email,
      ArrayList<String> projectIDs,
      ArrayList<String> menteeIDs) {
    this(userId, gitHubId, interestTags, email);
    this.projectIDs = projectIDs;
    this.menteeIDs = menteeIDs;
  }

  @OnSave
  public void setIsMentor() {
    this.isMentor = projectIDs.isEmpty();
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
