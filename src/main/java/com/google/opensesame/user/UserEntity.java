package com.google.opensesame.user;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * The User Entity is designed to directly interface with datastore using Objectify. It holds the
 * neccesary information to generate a UserData.
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
  public ArrayList<String> projectIds = new ArrayList<String>();

  public UserEntity() {}

  /**
   * Create a User entity with information from a UserData Instance
   *
   * @param user
   */
  public UserEntity(UserData userData) {
    gitHubId = userData.getGitHubID();
    interestTags = userData.getTags();
    email = userData.getEmail();
    userId = userData.getUserID();
    projectIds =
        new ArrayList<String>(
            userData.getProjects().stream()
                .map(projectData -> projectData.getRepositoryId())
                .collect(Collectors.toList()));
  }

  // TODO: Cut down the number of constructors.
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
   * @param projectIds
   */
  public UserEntity(
      String userId,
      String gitHubId,
      ArrayList<String> interestTags,
      String email,
      ArrayList<String> projectIds) {
    this(userId, gitHubId, interestTags, email);
    this.projectIds = projectIds;
  }

  @OnSave
  protected void setIsMentor() {
    isMentor = isMentor();
  }

  /** @return true if user is a mentor for a project, false otherwise. */
  public boolean isMentor() {
    return !projectIds.isEmpty();
  }

  /**
   * Add a project Id to the list of project Ids associated with this instance of the UserEntity.
   * Remember to store this change in datastore.
   */
  public void addProject(String projectId) {
    projectIds.add(projectId);
  }
}
