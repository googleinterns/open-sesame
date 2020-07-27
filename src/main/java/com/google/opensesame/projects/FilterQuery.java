package com.google.opensesame.projects;

import com.google.opensesame.util.ErrorResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

class FilterQuery {
  public static final String FILTER_QUERY_REGEX = "^[A-Za-z]+ (>|>=|!=|=|<|<=) .+$";

  /**
   * Parses a FilterQuery from a string. 
   * 
   * FilterQuery strings should be in the format "fieldName >= value".
   * Examples:
   * <blockquote>
   * "numMentors >= 2"
   * "numInterestedUsers = 0"
   * </blockquote>
   * @param filterQueryString The filter query string.
   * @param response The servlet response to send errors to.
   * @return Returns the FilterQuery or null if there was an error parsing the string.
   * @throws IOException
   */
  public static FilterQuery fromString(
      String filterQueryString, HttpServletResponse response) throws IOException {
    if (!filterQueryString.matches(FILTER_QUERY_REGEX)) {
      ErrorResponse.sendJsonError(
          response,
          "Invalid filter query.",
          HttpServletResponse.SC_BAD_REQUEST,
          "Unable to query for projects. Please try again later.");
      return null;
    }

    String[] splitFilterRequest = filterQueryString.split(" ");
    String filterFieldName = splitFilterRequest[0];
    Optional<Field> filterField = ProjectQuery.queryableFields.stream()
        .filter((field) -> field.getName().equals(filterFieldName))
        .findFirst();
    if (!filterField.isPresent()) {
      ErrorResponse.sendJsonError(
          response,
          "Cannot filter by the field '" + filterFieldName + "'.",
          HttpServletResponse.SC_BAD_REQUEST,
          "Unable to query for projects. Please try again later.");
      return null;
    }

    String comparisonValue = splitFilterRequest[2];
    Object comparisonObject;
    try {
      comparisonObject = 
          filterField.get().getType().getConstructor(String.class).newInstance(comparisonValue);
    } catch (Exception e) {
      // TODO(Richie): Handle the exceptions more specifically.
      ErrorResponse.sendJsonError(
          response,
          "Cannot parse the comparison value '"
              + comparisonValue + "' for the field '" + filterFieldName + "'.",
          HttpServletResponse.SC_BAD_REQUEST,
          "Unable to query for projects. Please try again later.");
      return null;
    }

    String comparator = splitFilterRequest[1];
    return new FilterQuery(filterFieldName + " " + comparator, comparisonObject);
  }
  
  public final String condition;
  public final Object comparisonObject;

  private FilterQuery(String condition, Object comparisonObject) {
    this.condition = condition;
    this.comparisonObject = comparisonObject;
  }
}
