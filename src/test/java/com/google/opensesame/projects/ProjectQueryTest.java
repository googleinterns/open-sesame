package com.google.opensesame.projects;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ProjectQueryTest {
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private Closeable session;

  private ProjectEntity firstMockProject;
  private ProjectEntity secondMockProject;
  private ProjectEntity thirdMockProject;

  private HttpServletResponse mockResponse;
  private StringWriter responseStringWriter;

  @BeforeClass
  public static void objectifyClassSetup() {
    ObjectifyService.setFactory(new ObjectifyFactory());
    ObjectifyService.register(ProjectEntity.class);
  }

  @Before
  public void objectifyStartSession() throws IOException {
    session = ObjectifyService.begin();
    helper.setUp();
  }

  @Before
  public void mockProjectSetUp() {
    firstMockProject =
        new ProjectEntity(
            "273537467", Arrays.asList("Mentor 1", "Mentor 2", "Mentor 3"), Arrays.asList());
    secondMockProject =
        new ProjectEntity("45717250", Arrays.asList("Mentor 1", "Mentor 2"), Arrays.asList());
    thirdMockProject = new ProjectEntity("20580498", Arrays.asList("Mentor 1"), Arrays.asList());
    ofy().save().entities(firstMockProject, secondMockProject, thirdMockProject).now();
  }

  @Before
  public void mockResponseSetUp() throws IOException {
    mockResponse = mock(HttpServletResponse.class);
    responseStringWriter = new StringWriter();
    when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseStringWriter));
  }

  @After
  public void objectifyEndSession() {
    session.close();
    helper.tearDown();
  }

  @Test
  public void queryByIdsShouldReturnProjectsWithThoseIds() throws IOException {
    // Expect to receive the ProjectEntities with the corresponding repository IDs.

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameterValues(ProjectEntity.PROJECT_ID_PARAM))
        .thenReturn(new String[] {firstMockProject.repositoryId, secondMockProject.repositoryId});

    Collection<ProjectEntity> queryResult = ProjectQuery.queryFromRequest(request, mockResponse);
    Collection<ProjectEntity> expected = Arrays.asList(firstMockProject, secondMockProject);

    assertNotNull(queryResult);
    assertTrue(queryResult.size() == expected.size() && queryResult.containsAll(expected));
    verify(mockResponse, never()).setStatus(anyInt());
  }

  @Test
  public void queryWithNoIdsShouldReturnEmpty() throws IOException {
    // Expect to receive no ProjectEntities because the list of IDs was defined but empty.

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameterValues(ProjectEntity.PROJECT_ID_PARAM)).thenReturn(new String[] {});

    Collection<ProjectEntity> queryResult = ProjectQuery.queryFromRequest(request, mockResponse);
    assertNotNull(queryResult);
    assertTrue(queryResult.size() == 0);
    verify(mockResponse, never()).setStatus(anyInt());
  }

  @Test
  public void queryWithInvalidIdShouldRaiseError() throws IOException {
    // Expect to receive an error response of 404 (resource not found) because one or more IDs
    // could not be found within the Datastore.

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameterValues(ProjectEntity.PROJECT_ID_PARAM))
        .thenReturn(new String[] {firstMockProject.repositoryId, "invalidID"});

    Collection<ProjectEntity> queryResult = ProjectQuery.queryFromRequest(request, mockResponse);
    assertNull(queryResult);
    verify(mockResponse, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
  }

  @Test
  public void queryWithoutIdsShouldReturnAllProjects() throws IOException {
    // Expect to receive all ProjectEntities in order of descending number of mentors because no
    // list of IDs was supplied.

    HttpServletRequest request = mock(HttpServletRequest.class);

    Collection<ProjectEntity> queryResult = ProjectQuery.queryFromRequest(request, mockResponse);
    ProjectEntity[] expectedQueryResult =
        new ProjectEntity[] {firstMockProject, secondMockProject, thirdMockProject};
    assertNotNull(queryResult);
    assertArrayEquals(expectedQueryResult, queryResult.toArray());
  }
}
