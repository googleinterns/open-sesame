package com.google.opensesame.projects;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.kohsuke.github.GHRepository;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public class ProjectTagTest {
  private static GHRepository mockRepository;

  private static final List<String> MOCK_REPO_TOPICS =
      Arrays.asList(new String[] {"tutorial", "examples"});
  private static final String MOCK_REPO_PRIMARY_LANGUAGE = "javascript";

  @BeforeClass
  public static void setUp() throws IOException {
    mockRepository = Mockito.mock(GHRepository.class);
    Mockito.when(mockRepository.listTopics()).thenReturn(MOCK_REPO_TOPICS);
    Mockito.when(mockRepository.getLanguage()).thenReturn(MOCK_REPO_PRIMARY_LANGUAGE);
  }

  @Test
  public void populatesTopics() throws IOException {
    // Ensures that the ProjectTags are properly populated from the GitHub repository topics.
    List<ProjectTag> projectTags = ProjectTag.fromRepository(mockRepository);

    for (String repoTopic : MOCK_REPO_TOPICS) {
      Assert.assertTrue(
          projectTags.stream()
              .anyMatch(
                  (tag) -> {
                    return tag.getTagText().equals(repoTopic);
                  }));
    }
  }

  @Test
  public void populatesPrimaryLanguage() throws IOException {
    // Ensures that a ProjectTag exists for the primary language of the repository.
    List<ProjectTag> projectTags = ProjectTag.fromRepository(mockRepository);

    Assert.assertTrue(
        projectTags.stream()
            .anyMatch(
                (tag) -> {
                  return tag.getTagText().equals(MOCK_REPO_PRIMARY_LANGUAGE);
                }));
  }
}
