package com.google.opensesame.projects;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class ProjectDatastore {
  private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  public static final String PROJECT_KIND = "Project";

  /**
   * Adds a project entity to the datastore, or overwrites a project entity if an entity with the
   * same repository ID exists.
   *
   * @param projectEntity
   * @return Returns the key associated with the datastore entity;
   */
  public static Key addProjectEntity(ProjectEntity projectEntity) {
    Entity newProjectEntity = new Entity(PROJECT_KIND, projectEntity.repositoryId);
    projectEntity.fillPropertyContainer(newProjectEntity);

    return datastore.put(newProjectEntity);
  }

  /**
   * Gets a project entity by its repository ID.
   *
   * @param repositoryId The repository ID to query for. The repository ID is the most precise way
   *     to query for a project entity, as this is the ID associated with each datastore entity.
   * @return Returns the ProjectEntity retrieved from datastore or null if there is none.
   */
  public static ProjectEntity getByRepositoryId(String repositoryId) {
    Key projectEntityKey = KeyFactory.createKey(PROJECT_KIND, repositoryId);

    Entity datastoreEntity;
    try {
      datastoreEntity = datastore.get(projectEntityKey);
    } catch (EntityNotFoundException e) {
      return null;
    }

    return ProjectEntity.fromProperties(datastoreEntity.getProperties());
  }
}
