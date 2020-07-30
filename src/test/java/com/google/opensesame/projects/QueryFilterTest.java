package com.google.opensesame.projects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.opensesame.util.ServletValidationException;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;

@RunWith(JUnit4.class)
public class QueryFilterTest {
  @Test
  public void parseQueryFilter() throws IOException, ServletValidationException {
    QueryFilter queryFilter = QueryFilter.fromString("numMentors > 123");

    assertNotNull(queryFilter);
    assertEquals("numMentors >", queryFilter.condition);
    assertEquals(123, queryFilter.comparisonObject);
  }

  @Test
  public void handlesInvalidString1() throws IOException {
    ServletValidationException validationException = Assert.assertThrows(
        ServletValidationException.class, 
        () -> QueryFilter.fromString("numMentors> 123"));

    assertEquals(HttpServletResponse.SC_BAD_REQUEST, validationException.getStatusCode());
  }

  @Test
  public void handlesInvalidString2() throws IOException {
    ServletValidationException validationException = Assert.assertThrows(
        ServletValidationException.class, 
        () -> QueryFilter.fromString("numMentors => 123"));

    assertEquals(HttpServletResponse.SC_BAD_REQUEST, validationException.getStatusCode());
  }

  @Test
  public void handlesInvalidField() throws IOException {
    ServletValidationException validationException = Assert.assertThrows(
        ServletValidationException.class, 
        () -> QueryFilter.fromString("notAFieldInProjectEntity > 123"));

    assertEquals(HttpServletResponse.SC_BAD_REQUEST, validationException.getStatusCode());
  }

  @Test
  public void handlesInvalidComparisonValue() throws IOException {
    ServletValidationException validationException = Assert.assertThrows(
        ServletValidationException.class, 
        () -> QueryFilter.fromString("numMentors > wontConvertToNumMentorsFieldType"));

    assertEquals(HttpServletResponse.SC_BAD_REQUEST, validationException.getStatusCode());
  }
}
