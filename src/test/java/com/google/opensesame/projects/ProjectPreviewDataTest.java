package com.google.opensesame.projects;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.kohsuke.github.GHRepository;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public class ProjectPreviewDataTest {
  private static Map<String, Object> projectProperties;
  private static GHRepository mockRepository;

  private static final String MOCK_REPO_NAME = "Hello-World";
  private static final String MOCK_REPO_DESCRIPTION = "This is a test description.";
  private static final List<String> MOCK_REPO_TOPICS =
      Arrays.asList(new String[] {"tutorial", "examples"});
  private static final String MOCK_REPO_PRIMARY_LANGUAGE = "javascript";
  private static final int MOCK_PROJECT_NUM_MENTORS = 5;

  @BeforeClass
  public static void setUp() throws IOException {
    mockRepository = Mockito.mock(GHRepository.class);
    Mockito.when(mockRepository.getName()).thenReturn(MOCK_REPO_NAME);
    Mockito.when(mockRepository.getDescription()).thenReturn(MOCK_REPO_DESCRIPTION);
    Mockito.when(mockRepository.listTopics()).thenReturn(MOCK_REPO_TOPICS);
    Mockito.when(mockRepository.getLanguage()).thenReturn(MOCK_REPO_PRIMARY_LANGUAGE);

    projectProperties = new HashMap<String, Object>();
    projectProperties.put("numMentors", MOCK_PROJECT_NUM_MENTORS);
  }

  @Test
  public void populatesRepositoryInfo() throws IOException {
    // Ensures that the project preview data is properly populated from the GitHub repository and
    // datastore entity properties.
    ProjectPreviewData projectPreview =
        ProjectPreviewData.fromPropertiesAndRepository(projectProperties, mockRepository);

    Assert.assertEquals(MOCK_REPO_NAME, projectPreview.getName());
    Assert.assertEquals(MOCK_REPO_DESCRIPTION, projectPreview.getDescription());
    Assert.assertEquals(MOCK_PROJECT_NUM_MENTORS, projectPreview.getNumMentors());
    Assert.assertEquals(MOCK_REPO_PRIMARY_LANGUAGE, projectPreview.getPrimaryLanguage());

    for (String repoTopic : MOCK_REPO_TOPICS) {
      Assert.assertTrue(
          projectPreview.getTopicTags().stream()
              .anyMatch(
                  (tag) -> {
                    return tag.equals(repoTopic);
                  }));
    }
  }
}
