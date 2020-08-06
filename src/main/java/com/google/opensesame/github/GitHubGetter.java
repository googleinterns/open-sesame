package com.google.opensesame.github;

import com.google.opensesame.util.RateLimitExceededException;
import java.io.IOException;
import java.net.HttpURLConnection;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.RateLimitHandler;

public final class GitHubGetter {
  private static GitHub gitHub = null;

  /**
   * Gets the GitHub API interface or builds one if there is not one available. If credentials are
   * available in the system environment variables, the builder will connect to the GitHub API using
   * those. Otherwise, it will connect anonymously.
   *
   * <p>In AppEngine, the credentials can be set in appengine-web.xml.
   *
   * @return Returns the GitHub API interface.
   * @throws IOException Throws IOException if there is an error building the GitHub interface.
   */
  public static GitHub getGitHub() throws IOException {
    if (gitHub == null) {
      String clientId = System.getenv("GITHUB_CLIENT_ID");
      String clientSecret = System.getenv("GITHUB_CLIENT_SECRET");
      gitHub = buildGitHubInterface(clientId, clientSecret);
    }

    return gitHub;
  }

  /**
   * Builds the GitHub interface from credentials or with an anonymous connection if the credentials
   * are invalid.
   *
   * @throws IOException Throws IOException if there is an error building the GitHub interface.
   */
  public static GitHub buildGitHubInterface(String clientId, String clientSecret)
      throws IOException {
    GitHubRateLimitHandler rateLimitHandler = new GitHubRateLimitHandler();

    if (clientId == null || clientId.isEmpty() || clientSecret == null || clientSecret.isEmpty()) {
      System.err.println(
          "GitHub authorization is not set. Please refer to the project README to"
              + " configure this. Using unauthorized GitHub API for now.");

      return new GitHubBuilder().withRateLimitHandler(rateLimitHandler).build();
    }

    GitHub authenticatedGitHub =
        new GitHubBuilder()
            .withRateLimitHandler(rateLimitHandler)
            .withPassword(clientId, clientSecret)
            .build();

    try {
      authenticatedGitHub.checkApiUrlValidity();
      return authenticatedGitHub;
    } catch (IOException e) {
      System.err.println(
          "Invalid GitHub credentials. Please refer to the project README to"
              + " configure this. Using unauthorized GitHub API for now.");

      return new GitHubBuilder().withRateLimitHandler(rateLimitHandler).build();
    }
  }

  /**
   * Sets the GitHub API interface to the provided instance so that all of the following calls to
   * getGitHub() will return this instance. This is intended to be used for mocking the GitHub API
   * interface while testing and should not be elsewhere.
   *
   * @param mockGitHub
   */
  public static void setGitHubForTests(GitHub mockGitHub) {
    gitHub = mockGitHub;
  }

  /**
   * Removes the mocked GitHub API interface so that the following calls to getGitHub() will no
   * longer return the provided instance. This is intended to be used for mocking the GitHub API
   * interface while testing and should not be elsewhere.
   */
  public static void removeGitHubMock() {
    gitHub = null;
  }

  /**
   * Causes requests made to the GitHub API to fail with a RateLimitExceededException when the API
   * rate limit has been exceeded. Without this handler, requests to the API would hang until the
   * limit has been refreshed.
   */
  private static class GitHubRateLimitHandler extends RateLimitHandler {

    @Override
    public void onError(IOException e, HttpURLConnection uc) throws RateLimitExceededException {
      throw new RateLimitExceededException();
    }
  }
}
