package com.google.opensesame.projects;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.junit.Assert.assertEquals;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import java.io.IOException;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProjectEntityTest {
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private Closeable session;

  private ProjectEntity mockProject;

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
    mockProject =
        new ProjectEntity(
            "273537467", Arrays.asList("Mentor 1", "Mentor 2", "Mentor 3"), Arrays.asList());
    ofy().save().entity(mockProject).now();
  }

  @After
  public void objectifyEndSession() {
    session.close();
    helper.tearDown();
  }

  @Test
  public void fromExistingRepositoryIdShouldReturnExistingProject() {
    // Expect the function to return an existing ProjectEntity from the Datastore because an entity
    // with the supplied ID already exists.

    ProjectEntity projectEntity = ProjectEntity.fromRepositoryIdOrNew(mockProject.repositoryId);

    assertEquals(mockProject, projectEntity);
  }

  @Test
  public void fromNewRepositoryIdShouldReturnNewProject() {
    // Expect the function to return a new ProjectEntity because no entity with that supplied ID
    // exists in the Datastore.

    String newId = "newId";
    ProjectEntity projectEntity = ProjectEntity.fromRepositoryIdOrNew(newId);

    assertEquals(newId, projectEntity.repositoryId);
  }
}
