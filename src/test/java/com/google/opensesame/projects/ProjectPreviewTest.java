package com.google.opensesame.projects;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.kohsuke.github.GHRepository;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public class ProjectPreviewTest {
  private static Map<String, Object> projectProperties;
  private static GHRepository mockRepository;

  private static final String MOCK_REPO_NAME = "Hello-World";
  private static final String MOCK_REPO_DESCRIPTION = "This is a test description.";
  private static final int MOCK_REPO_NUM_MENTORS = 5;

  @BeforeClass
  public static void setUp() throws IOException {
    mockRepository = Mockito.mock(GHRepository.class);
    Mockito.when(mockRepository.getName()).thenReturn(MOCK_REPO_NAME);
    Mockito.when(mockRepository.getDescription()).thenReturn(MOCK_REPO_DESCRIPTION);

    projectProperties = new HashMap<String, Object>();
    projectProperties.put("numMentors", MOCK_REPO_NUM_MENTORS);
  }

  @Test
  public void populatesRepositoryInfo() throws IOException {
    // Ensures that the project preview data is properly populated from the GitHub repository and
    // datastore entity properties.
    ProjectPreview projectPreview =
        ProjectPreview.fromPropertiesAndRepository(projectProperties, mockRepository);

    Assert.assertEquals(MOCK_REPO_NAME, projectPreview.getName());
    Assert.assertEquals(MOCK_REPO_DESCRIPTION, projectPreview.getDescription());
    Assert.assertEquals(MOCK_REPO_NUM_MENTORS, projectPreview.getNumMentors());
  }
}
