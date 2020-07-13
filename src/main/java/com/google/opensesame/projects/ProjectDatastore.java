package com.google.opensesame.projects;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import java.util.ArrayList;
import java.util.List;

public class ProjectDatastore {
  private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  public static final String PROJECT_KIND = "Project";

  /**
   * Adds a project entity to the datastore, or overwrites a project entity if an entity with the
   * same repository ID exists.
   *
   * @param projectEntity
   * @return Returns the key associated with the datastore entity.
   */
  public static Key addProjectEntity(ProjectEntity projectEntity) {
    Entity newProjectEntity = new Entity(PROJECT_KIND, projectEntity.repositoryId);
    projectEntity.fillEntity(newProjectEntity);

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

  /**
   * Get a list of project entities by query and fetch options.
   *
   * @param projectsQuery The datastore query.
   * @param fetchOptions The fetch options for the query.
   * @return Returns a list of project entities from the query.
   */
  public static List<ProjectEntity> getByQuery(Query projectsQuery, FetchOptions fetchOptions) {
    if (!projectsQuery.getKind().equals("Project")) {
      throw new IllegalArgumentException("Query must be made to kind 'Project'.");
    }

    PreparedQuery queryResults = datastore.prepare(projectsQuery);

    List<ProjectEntity> projectEntities = new ArrayList<ProjectEntity>();
    for (Entity datastoreEntity : queryResults.asIterable(fetchOptions)) {
      ProjectEntity newProjectEntity =
          ProjectEntity.fromProperties(datastoreEntity.getProperties());
      projectEntities.add(newProjectEntity);
    }

    return projectEntities;
  }

  /**
   * Get a list of project entities by query.
   *
   * @param projectsQuery The datastore query.
   * @return Returns a list of project entities from the query.
   */
  public static List<ProjectEntity> getByQuery(Query projectsQuery) {
    return getByQuery(projectsQuery, FetchOptions.Builder.withDefaults());
  }
}
