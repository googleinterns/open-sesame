package com.google.opensesame.projects;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.opensesame.util.ServletValidationException;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Assert;
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
            "273537467",
            Arrays.asList("Mentor 1", "Mentor 2", "Mentor 3"),
            Arrays.asList("User 1", "User 2"));
    secondMockProject =
        new ProjectEntity(
            "45717250", Arrays.asList("Mentor 1", "Mentor 2"), Arrays.asList("User 1"));
    thirdMockProject = new ProjectEntity("20580498", Arrays.asList("Mentor 1"), Arrays.asList());
    ofy().save().entities(firstMockProject, secondMockProject, thirdMockProject).now();
  }

  @After
  public void objectifyEndSession() {
    session.close();
    helper.tearDown();
  }

  @Test
  public void queryByIdsShouldReturnProjectsWithThoseIds()
      throws IOException, ServletValidationException {
    // Expect to receive the ProjectEntities with the corresponding repository IDs.

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameterValues(ProjectEntity.PROJECT_ID_PARAM))
        .thenReturn(new String[] {firstMockProject.repositoryId, secondMockProject.repositoryId});

    Collection<ProjectEntity> queryResult = ProjectQuery.queryFromRequest(request);
    Collection<ProjectEntity> expected = Arrays.asList(firstMockProject, secondMockProject);

    assertTrue(queryResult.size() == expected.size() && queryResult.containsAll(expected));
  }

  @Test
  public void queryWithNoIdsShouldReturnEmpty() throws IOException, ServletValidationException {
    // Expect to receive no ProjectEntities because the list of IDs was defined but empty.

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameterValues(ProjectEntity.PROJECT_ID_PARAM)).thenReturn(new String[] {});

    Collection<ProjectEntity> queryResult = ProjectQuery.queryFromRequest(request);
    assertTrue(queryResult.size() == 0);
  }

  @Test
  public void queryWithInvalidIdShouldRaiseError() throws IOException {
    // Expect to receive an error response of 404 (resource not found) because one or more IDs
    // could not be found within the Datastore.

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameterValues(ProjectEntity.PROJECT_ID_PARAM))
        .thenReturn(new String[] {firstMockProject.repositoryId, "invalidID"});

    ServletValidationException validationException =
        Assert.assertThrows(
            ServletValidationException.class, () -> ProjectQuery.queryFromRequest(request));
    assertEquals(HttpServletResponse.SC_NOT_FOUND, validationException.getStatusCode());
  }

  @Test
  public void queryWithoutIdsShouldReturnAllProjects()
      throws IOException, ServletValidationException {
    // Expect to receive all ProjectEntities because no list of IDs was supplied.

    HttpServletRequest request = mock(HttpServletRequest.class);

    Collection<ProjectEntity> queryResult = ProjectQuery.queryFromRequest(request);
    Collection<ProjectEntity> expected =
        Arrays.asList(firstMockProject, secondMockProject, thirdMockProject);

    assertTrue(queryResult.size() == expected.size() && queryResult.containsAll(expected));
  }

  @Test
  public void queryWithFilterShouldReturnFilteredResult()
      throws IOException, ServletValidationException {
    // Expect to receive only the ProjectEntities with two or more mentors.

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameterValues("filter")).thenReturn(new String[] {"numMentors >= 2"});

    Collection<ProjectEntity> queryResult = ProjectQuery.queryFromRequest(request);
    Collection<ProjectEntity> expected = Arrays.asList(firstMockProject, secondMockProject);

    assertTrue(queryResult.size() == expected.size() && queryResult.containsAll(expected));
  }

  @Test
  public void queryWithMultipleFiltersShouldReturnIntersectionOfResults()
      throws IOException, ServletValidationException {
    // Expect to receive only the ProjectEntity with two or more mentors and two or more
    // interested users.

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameterValues("filter"))
        .thenReturn(new String[] {"numMentors >= 2", "numInterestedUsers > 1"});

    Collection<ProjectEntity> queryResult = ProjectQuery.queryFromRequest(request);
    Collection<ProjectEntity> expected = Arrays.asList(firstMockProject);

    assertTrue(queryResult.size() == expected.size() && queryResult.containsAll(expected));
  }
}
