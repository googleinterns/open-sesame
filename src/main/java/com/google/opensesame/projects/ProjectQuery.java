package com.google.opensesame.projects;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.opensesame.util.ErrorResponse;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.cmd.Query;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Helper functions for querying the projects Datastore. */
public class ProjectQuery {
  public static final String FILTER_QUERY_PARAM = "filter";

  /**
   * The fields of ProjectEntity that have the @Index Objectify annotation and can be used for
   * sorting and filtering in Datastore.
   */
  public static List<Field> queryableFields;

  static {
    Field[] fields = ProjectEntity.class.getDeclaredFields();
    queryableFields = new ArrayList<Field>();
    for (Field field : fields) {
      if (field.isAnnotationPresent(Index.class)) {
        queryableFields.add(field);
      }
    }
  }

  /**
   * Gets the query filters from a servlet request. Returns null if any errors are encountered while
   * parsing the query filters. See {@link com.google.opensesame.projects.QueryFilter.fromString}
   * for more information on how QueryFilters are parsed.
   *
   * @param HttpServletRequest The request to get the filter queries from.
   * @param HttpServletResponse The servlet response to send errors to.
   * @return Returns the list of QueryFilters or null if an error was encountered while parsing.
   */
  private static List<QueryFilter> getQueryFiltersFromRequest(
      HttpServletRequest request, HttpServletResponse response) throws IOException {
    String[] filterRequests = request.getParameterValues(FILTER_QUERY_PARAM);
    if (filterRequests == null) {
      return Arrays.asList();
    }

    List<QueryFilter> queryFilters = new ArrayList<QueryFilter>();
    for (String filterRequest : filterRequests) {
      QueryFilter queryFilter = QueryFilter.fromString(filterRequest, response);
      if (queryFilter == null) {
        return null;
      }
      queryFilters.add(queryFilter);
    }

    return queryFilters;
  }

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
    String[] projectIds = request.getParameterValues(ProjectEntity.PROJECT_ID_PARAM);
    Collection<ProjectEntity> projectEntities;
    if (projectIds != null) {
      projectEntities = ofy().load().type(ProjectEntity.class).ids(projectIds).values();

      if (projectIds.length != projectEntities.size()) {
        ErrorResponse.sendJsonError(
            response,
            "One or more of the supplied IDs is invalid and those projects could not be found.",
            HttpServletResponse.SC_NOT_FOUND,
            "One or more of the requested projects does not exist.");
        return null;
      }
    } else {
      // TODO(Richie): Add pagination support.
      // TODO(Richie): Add ordering support.
      Query<ProjectEntity> projectEntityQuery =
          ofy().load().type(ProjectEntity.class).order("-numMentors");

      List<QueryFilter> queryFilters = getQueryFiltersFromRequest(request, response);
      if (queryFilters == null) {
        return null;
      }

      for (QueryFilter queryFilter : queryFilters) {
        projectEntityQuery =
            projectEntityQuery.filter(queryFilter.condition, queryFilter.comparisonObject);
      }

      projectEntities = projectEntityQuery.list();
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
}
