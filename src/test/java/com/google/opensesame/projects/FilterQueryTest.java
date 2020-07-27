package com.google.opensesame.projects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;

@RunWith(JUnit4.class)
public class FilterQueryTest {
  private HttpServletResponse mockResponse;
  private StringWriter responseStringWriter;

  @Before
  public void mockResponseSetUp() throws IOException {
    mockResponse = mock(HttpServletResponse.class);
    responseStringWriter = new StringWriter();
    when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseStringWriter));
  }

  @Test
  public void parseFilterQuery() throws IOException {
    FilterQuery filterQuery = 
        FilterQuery.fromString("numMentors > 123", mockResponse);

    assertNotNull(filterQuery);
    assertEquals("numMentors >", filterQuery.condition);
    assertEquals(123, filterQuery.comparisonObject);
  }

  @Test
  public void handlesInvalidString1() throws IOException {
    FilterQuery filterQuery = 
        FilterQuery.fromString("numMentors> 123", mockResponse);

    assertNull(filterQuery);
    verify(mockResponse, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Test
  public void handlesInvalidString2() throws IOException {
    FilterQuery filterQuery = 
        FilterQuery.fromString("numMentors => 123", mockResponse);

    assertNull(filterQuery);
    verify(mockResponse, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Test
  public void handlesInvalidField() throws IOException {
    FilterQuery filterQuery = 
        FilterQuery.fromString("notAFieldInProjectEntity > 123", mockResponse);

    assertNull(filterQuery);
    verify(mockResponse, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Test
  public void handlesInvalidComparisonValue() throws IOException {
    FilterQuery filterQuery = 
        FilterQuery.fromString("numMentors > wontConvertToNumMentorsFieldType", mockResponse);

    assertNull(filterQuery);
    verify(mockResponse, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
  }
}