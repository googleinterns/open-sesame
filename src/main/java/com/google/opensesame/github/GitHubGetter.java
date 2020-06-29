package com.google.opensesame.github;

import java.io.IOException;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

public final class GitHubGetter {
  private static GitHub gitHub = null;

  /**
   * Gets the GitHub API interface or builds one if there is not one available.
   * @return Returns the GitHub API interface.
   * @throws IOException Throws IOException if there is an error building the GitHub interface.
   */
  public static GitHub getGitHub() throws IOException {
    if (gitHub == null) {
      buildGitHubInterface();
    }

    return gitHub;
  }

  /**
   * Builds the GitHub interface from environment variables.
   * 
   * With AppEngine, the environment variables are set in the appengine-web.xml file.
   * @throws IOException Throws IOException if there is an error building the GitHub interface.
   */
  private static void buildGitHubInterface() throws IOException {
    String clientID = System.getenv("GITHUB_CLIENT_ID").trim();
    String clientSecret = System.getenv("GITHUB_CLIENT_SECRET").trim();

    if (clientID == null || clientID.isEmpty() || clientSecret == null || clientSecret.isEmpty()) {
      System.err.println("GitHub authorization is not set. Please refer to the project README to" 
          + " configure this. Using unauthorized GitHub API for now.");
      gitHub = GitHub.connectAnonymously();
      return;
    }

    gitHub = new GitHubBuilder().withPassword(clientID, clientSecret).build();
    
    try {
      gitHub.checkApiUrlValidity();
    } catch (IOException e) {
      System.err.println("Invalid GitHub credentials. Please refer to the project README to" 
      + " configure this. Using unauthorized GitHub API for now.");
      gitHub = GitHub.connectAnonymously();
    }
  }
}
