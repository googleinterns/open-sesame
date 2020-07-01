package com.google.opensesame.github;

import java.io.IOException;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

public final class GitHubGetter {
  private static GitHub gitHub = null;

  /**
   * Gets the GitHub API interface or builds one if there is not one available. If credentials are
   * available in the system environment variables, the builder will connect to the GitHub API using
   * those. Otherwise, it will connect anonymously.
   *
   * <p>In AppEngine, the credentials can be set in appengine-web.xml
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
    if (clientId == null || clientId.isEmpty() || clientSecret == null || clientSecret.isEmpty()) {
      System.err.println(
          "GitHub authorization is not set. Please refer to the project README to"
              + " configure this. Using unauthorized GitHub API for now.");
      return GitHub.connectAnonymously();
    }

    GitHub authenticatedGitHub = new GitHubBuilder().withPassword(clientId, clientSecret).build();

    try {
      authenticatedGitHub.checkApiUrlValidity();
      return authenticatedGitHub;
    } catch (IOException e) {
      System.err.println(
          "Invalid GitHub credentials. Please refer to the project README to"
              + " configure this. Using unauthorized GitHub API for now.");
      return GitHub.connectAnonymously();
    }
  }
}
