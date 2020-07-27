package com.google.opensesame.projects;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.opensesame.util.ErrorResponse;
import com.googlecode.objectify.annotation.Index;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProjectQuery {
  public static final String FILTER_QUERY_PARAM = "filter";
  public static final String FILTER_QUERY_REGEX = "^[A-Za-z]+ (>|>=|!=|=|<|<=) .+$";

  /**
   * The names of ProjectEntity fields that have the @Index Objectify annotation and can be
   * sorted and filtered by in Datastore.
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
   * Gets the filter queries from a servlet request. Returns null if any errors are encountered
   * while parsing the filter queries.
   * See {@link com.google.opensesame.projects.FilterQuery.fromString} for more information on how
   * FilterQueries are parsed.
   * @param HttpServletRequest The request to get the filter queries from.
   * @param HttpServletResponse The servlet response to send errors to.
   * @return Returns the list of FilterQueries or null if an error was encountered while parsing.
   */
  private static List<FilterQuery> filterQueriesFromRequest(
      HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    String[] filterRequests = request.getParameterValues(FILTER_QUERY_PARAM);
    if (filterRequests == null) {
      return Arrays.asList();
    }

    List<FilterQuery> filterQueries = new ArrayList<FilterQuery>();
    for (String filterRequest : filterRequests) {
      filterQueries.add(FilterQuery.fromString(filterRequest, response));
    }

    return filterQueries;
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
            "One or more of the requested projects does not exist. Please try again later.");
        return null;
      }
    } else {
      // TODO(Richie): Add pagination support.
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
}