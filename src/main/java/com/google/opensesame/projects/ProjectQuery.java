package com.google.opensesame.projects;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.opensesame.util.ServletValidationException;
import com.googlecode.objectify.annotation.Index;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Helper functions for querying the projects Datastore. */
public class ProjectQuery {
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
   * Gets the query filters from a servlet request.
   *
   * @param HttpServletRequest The request to get the filter queries from.
   * @return Returns the list of QueryFilters.
   * @throws IOException
   * @throws ServletValidationException Throws if any of the query filter strings in the request are
   *     invalid. See {@link com.google.opensesame.projects.QueryFilter.fromString}
   *     for more information on how QueryFilters are parsed.
   */
  private static List<QueryFilter> getQueryFiltersFromRequest(HttpServletRequest request)
      throws IOException, ServletValidationException {
    String[] filterRequests = request.getParameterValues("filter");
    if (filterRequests == null) {
      return Arrays.asList();
    }

    List<QueryFilter> queryFilters = new ArrayList<QueryFilter>();
    for (String filterRequest : filterRequests) {
      QueryFilter queryFilter = QueryFilter.fromString(filterRequest);
      queryFilters.add(queryFilter);
    }

    return queryFilters;
  }

  /**
   * Queries for ProjectEntities in the datastore based on a servlet request.
   *
   * @param request The servlet request defining the query.
   * @return Returns a collection of ProjectEntities from the query.
   * @throws IOException
   * @throws ServletValidationException Throws if the query request is invalid.
   */
  public static Collection<ProjectEntity> queryFromRequest(HttpServletRequest request)
      throws IOException, ServletValidationException {
    String[] projectIds = request.getParameterValues(ProjectEntity.PROJECT_ID_PARAM);
    
    if (projectIds != null) {
      return getProjectEntitiesByIds(projectIds);
    } else {
      // TODO(Richie): Add pagination support.
      // TODO(Richie): Add ordering support.
      List<QueryFilter> queryFilters = getQueryFiltersFromRequest(request);
      return getProjectEntitiesByQueryFilters(queryFilters);
    }
  }

  /**
   * Gets the ProjectEntities in the intersection of a list of QueryFilters.
   * @param queryFilters The list of QueryFilters.
   * @return Returns a collection of ProjectEntities in the intersection of the list of
   *     QueryFilters.
   */
  public static Collection<ProjectEntity> getProjectEntitiesByQueryFilters(
      List<QueryFilter> queryFilters) {
    if (queryFilters.size() == 0) {
      return ofy().load().type(ProjectEntity.class).list();
    }
    
    List<ProjectEntity> intermediateIntersection = null;
    for (QueryFilter queryFilter : queryFilters) {
      List<ProjectEntity> filteredProjects =
          ofy()
              .load()
              .type(ProjectEntity.class)
              .filter(queryFilter.condition, queryFilter.comparisonObject)
              .list();

      if (intermediateIntersection != null) {
        intermediateIntersection =
            filteredProjects.stream()
                .filter(intermediateIntersection::contains)
                .collect(Collectors.toList());
      } else {
        intermediateIntersection = filteredProjects;
      }
    }

    return intermediateIntersection;
  }

  /**
   * Gets ProjectEntities from a list of project entity IDs and validates that all project entities
   * are found.
   * @param projectEntityIds The IDs of the ProjectEntities.
   * @return Returns a collection of ProjectEntities with the provided IDs.
   * @throws ServletValidationException Throws if not all ProjectEntities with the specified IDs
   *     could be found.
   */
  public static Collection<ProjectEntity> getProjectEntitiesByIds(String[] projectEntityIds)
      throws ServletValidationException {
    Collection<ProjectEntity> projectEntities =
        ofy().load().type(ProjectEntity.class).ids(projectEntityIds).values();

    if (projectEntityIds.length != projectEntities.size()) {
      throw new ServletValidationException(
          "One or more of the supplied IDs is invalid and those projects could not be found.",
          "One or more of the requested projects does not exist.",
          HttpServletResponse.SC_NOT_FOUND);
    }

    return projectEntities;
  }
}
