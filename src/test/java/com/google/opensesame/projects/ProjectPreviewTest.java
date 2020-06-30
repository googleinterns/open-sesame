package com.google.opensesame.projects;

import com.google.opensesame.github.GitHubGetter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.kohsuke.github.GitHub;

@RunWith(JUnit4.class)
public class ProjectPreviewTest {
  private static Map<String, Object> projectProperties;
  private static GitHub gitHub;

  private static final String TEST_REPO_NAME = "octocat/Hello-World";
  private static final int TEST_REPO_NUM_MENTORS = 5;

  private static int GITHUB_LIMIT_REMAINING;

  @Before
  public void setUp() throws IOException {
    gitHub = GitHubGetter.getGitHub();
    GITHUB_LIMIT_REMAINING = gitHub.rateLimit().getRemaining();

    projectProperties = new HashMap<String, Object>();
    projectProperties.put("repositoryName", TEST_REPO_NAME);
    projectProperties.put("numMentors", TEST_REPO_NUM_MENTORS);
  }

  @After
  public void getUsedAPICalls() throws IOException {
    int nowRemaining = gitHub.rateLimit().getRemaining();
    int callsUsed = GITHUB_LIMIT_REMAINING - nowRemaining;
    System.out.println(
        "Test used " + callsUsed + " GitHub API calls; " + nowRemaining + " remaining.");
  }

  @Test
  public void populatesRepositoryInfo() throws IOException {
    ProjectPreview projectPreview = ProjectPreview.fromProperties(projectProperties);

    Assert.assertEquals("My first repository on GitHub!", projectPreview.getShortDescription());
    Assert.assertEquals("Hello-World", projectPreview.getName());
  }
}
