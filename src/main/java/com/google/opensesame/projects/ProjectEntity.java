package com.google.opensesame.projects;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.annotation.OnSave;
import java.util.ArrayList;
import java.util.List;

/** The class used to interact with the project entities in data storage. */
@Entity
public class ProjectEntity {
  /**
   * Get a project entity with a specified repository ID from the datastore or
   * create a new one if one does not exist in the datastore.
   * @param repositoryId The repository ID of the project.
   * @return Returns the project entity from datastore or a new project entity.
   */
  public static ProjectEntity fromRepositoryIdOrNew(String repositoryId) {
    ProjectEntity projectEntity = ofy().load().type(ProjectEntity.class).id(repositoryId).now();
    if (projectEntity == null) {
      projectEntity = 
          new ProjectEntity(repositoryId, new ArrayList<String>(), new ArrayList<String>());
    }

    return projectEntity;
  }

  @Id public String repositoryId;
  public List<String> mentorIds;
  public List<String> interestedUserIds;
  @Index protected int numMentors;
  @Index protected int numInterestedUsers;

  /**
   * No-arg constructor for Objectify:
   * https://github.com/objectify/objectify/wiki/Entities#the-basics
   */
  protected ProjectEntity() {}

  /**
   * Creates a new ProjectEntity object.
   *
   * @param repositoryId
   * @param mentorIds
   * @param interestedUserIds
   */
  public ProjectEntity(
      String repositoryId, List<String> mentorIds, List<String> interestedUserIds) {
    this.repositoryId = repositoryId;
    this.mentorIds = mentorIds;
    this.interestedUserIds = interestedUserIds;

    // Empty lists are represented as null in datastore, so create empty lists if null:
    // https://cloud.google.com/appengine/docs/standard/java/datastore/entity-property-reference#Using_an_empty_list
    if (this.mentorIds == null) {
      this.mentorIds = new ArrayList<String>();
    }
    if (this.interestedUserIds == null) {
      this.interestedUserIds = new ArrayList<String>();
    }
  }

  /** Compute the lengths of mentor and interested user lists before the project entity is saved. */
  @OnSave
  protected void computeListLengths() {
    numMentors = mentorIds.size();
    numInterestedUsers = interestedUserIds.size();
  }

  @OnLoad
  protected void checkNullLists() {
    if (this.mentorIds == null) {
      this.mentorIds = new ArrayList<String>();
    }
    if (this.interestedUserIds == null) {
      this.interestedUserIds = new ArrayList<String>();
    }
  }

  public int getNumMentors() {
    return this.mentorIds.size();
  }

  public int getNumInterestedUsers() {
    return this.interestedUserIds.size();
  }
}
