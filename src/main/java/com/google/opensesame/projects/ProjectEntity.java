package com.google.opensesame.projects;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.opensesame.util.ErrorResponse;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.annotation.OnSave;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** The class used to interact with the project entities in data storage. */
@Entity
public class ProjectEntity {
  public static final String PROJECT_ID_PARAM = "projectId";

  /**
   * Queries for ProjectEntities in the datastore based on a servlet request.
   *
   * @param request The servlet request defining the query.
   * @param response The servlet response to send errors to.
   * @return Returns a collection of ProjectEntities from the query or null if the query was
   *     invalid.
   * @throws IOException
   */
  public static Collection<ProjectEntity> queryFromRequest(
      HttpServletRequest request, HttpServletResponse response) throws IOException {
    String[] projectIds = request.getParameterValues(PROJECT_ID_PARAM);
    Collection<ProjectEntity> projectEntities;
    if (projectIds != null) {
      projectEntities = ofy().load().type(ProjectEntity.class).ids(projectIds).values();

      if (projectIds.length != projectEntities.size()) {
        ErrorResponse.sendJsonError(
            response,
            "One or more of the supplied IDs is invalid and those projects could not be found.",
            HttpServletResponse.SC_NOT_FOUND,
            "One or more of the requested projects does not exist. Please try again later.");
        return null;
      }
    } else {
      // TODO(Richie): Add parameters for ordering and filtering the query.
      // TODO(Richie): Add pagination support.
      // Get all projects in default ordering if no IDs are specified.
      projectEntities = ofy().load().type(ProjectEntity.class).order("-numMentors").list();
    }

    return projectEntities;
  }

  /**
   * Get a project entity with a specified repository ID from the datastore or create a new one if
   * one does not exist in the datastore.
   *
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
}
