package com.google.opensesame.github;

import java.io.IOException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;

@RunWith(JUnit4.class)
public class GitHubGetterTest {
  @Test
  public void handlesEmptyCredentials() throws IOException {
    GitHubGetter.buildGitHubInterface("", "").checkApiUrlValidity();
  }

  @Test
  public void handlesInvalidCredentials() throws IOException {
    GitHubGetter.buildGitHubInterface("invalidClientId", "invalidClientSecret")
        .checkApiUrlValidity();
  }
}