package com.google.opensesame.projects;

import com.google.opensesame.util.ServletValidationException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

/**
 * A Datastore filter stored in the form used by Objectify when loading entities from the Datastore.
 *
 * <p>See here for how Objectify conducts filtering on a query:
 * https://www.javadoc.io/static/com.googlecode.objectify/objectify/6.0.6/com/googlecode/objectify/cmd/Query.html#filter(java.lang.String,java.lang.Object)
 */
class QueryFilter {
  /**
   * Parses a QueryFilter from a string.
   *
   * <p>QueryFilter strings should be in the format "fieldName <comparator> comparisonValue".
   * Examples:
   *
   * <blockquote>
   *
   * <pre>
   * "numMentors >= 2"
   * "numInterestedUsers = 0"
   * </pre>
   *
   * </blockquote>
   *
   * @param QueryFilterString The filter query string.
   * @return Returns the QueryFilter parsed from the string.
   * @throws IOException
   * @throws ServletValidationException Throws if there is an error parsing the query filter string.
   */
  public static QueryFilter fromString(String QueryFilterString)
      throws IOException, ServletValidationException {
    String filterQueryRegex = "^[A-Za-z]+ (>|>=|!=|=|<|<=) [^\\s]+$";
    if (!QueryFilterString.matches(filterQueryRegex)) {
      throw new ServletValidationException(
          "Invalid filter query.",
          "Unable to query for projects.",
          HttpServletResponse.SC_BAD_REQUEST);
    }

    String[] splitFilterRequest = QueryFilterString.split(" ");
    String filterFieldName = splitFilterRequest[0];
    String comparator = splitFilterRequest[1];
    String comparisonValueString = splitFilterRequest[2];

    Field filterField = getFilterFieldFromName(filterFieldName);

    Object comparisonValueObject =
        getComparisonValueObjectFromString(comparisonValueString, filterField);

    return new QueryFilter(filterFieldName + " " + comparator, comparisonValueObject);
  }

  /**
   * Gets the Field for the filter by its name.
   *
   * @param filterFieldName The name of the field.
   * @return Returns the field.
   * @throws ServletValidationException Throws if the filter field name is invalid.
   */
  private static Field getFilterFieldFromName(String filterFieldName)
      throws ServletValidationException {
    Optional<Field> filterField =
        ProjectQuery.queryableFields.stream()
            .filter((field) -> field.getName().equals(filterFieldName))
            .findFirst();
    if (!filterField.isPresent()) {
      throw new ServletValidationException(
          "Cannot filter by the field '" + filterFieldName + "'.",
          "Unable to query for projects.",
          HttpServletResponse.SC_BAD_REQUEST);
    }

    return filterField.get();
  }

  /**
   * Converts the filter comparison value string into an object of the filter field's type that is
   * constructed with the comparison value string.
   *
   * <p>The filter field type must have a constructor that takes a single string as an argument for
   * this construction.
   *
   * @param comparisonValueString A string representing the value of the comparison value object.
   * @param filterField The field that this filter is filtering by.
   * @return Returns the comparison value object created from the comparison value string.
   * @throws ServletValidationException Throws if there is an error converting the comparison value
   *     string into an object.
   */
  private static Object getComparisonValueObjectFromString(
      String comparisonValueString, Field filterField) throws ServletValidationException {
    Object comparisonValueObject;
    try {
      comparisonValueObject =
          filterField.getType().getConstructor(String.class).newInstance(comparisonValueString);
    } catch (Exception e) {
      // TODO(Richie): Handle the exceptions more specifically.
      throw new ServletValidationException(
          "Cannot parse the comparison value '"
              + comparisonValueString
              + "' for the field '"
              + filterField.getName()
              + "'.",
          "Unable to query for projects.",
          HttpServletResponse.SC_BAD_REQUEST);
    }

    return comparisonValueObject;
  }

  public final String condition;
  public final Object comparisonObject;

  public QueryFilter(String condition, Object comparisonObject) {
    this.condition = condition;
    this.comparisonObject = comparisonObject;
  }
}
