package com.google.opensesame.servlets;

import com.google.opensesame.github.GitHubGetter;
import java.io.IOException;
import java.util.ArrayList;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

/** A class containing basic user data. This is intended to be serialized and sent to the client. */
public class PersonObject {
  private final ArrayList<String> interestTags;
  private final String bio;
  private final String email;
  private final String gitHubID;
  private final String gitHubURL;
  private final String image;
  private final String location;
  private final String name;

  /**
   * Creates a PersonObject with the given parameters
   *
   * @param gitHubID
   * @param interestTags
   * @param email
   * @throws IOException
   */
  public PersonObject(String gitHubID, ArrayList<String> interestTags, String email)
      throws IOException {
    this.gitHubID = gitHubID;
    this.interestTags = interestTags;
    this.email = email;

    // GitHub query TODO: alternate ways of getting this information.
    final GitHub gitHub = GitHubGetter.getGitHub();
    final GHUser userGitAccount = gitHub.getUser(gitHubID);
    this.bio = userGitAccount.getBio();
    this.image = userGitAccount.getAvatarUrl();
    this.location = userGitAccount.getLocation();
    this.name = userGitAccount.getName();
    this.gitHubURL = userGitAccount.getHtmlUrl().toString();
  }

  /** @return The name of a user. */
  public String getName() {
    return this.name;
  }

  /** @return The bio of a user from GitHub. */
  public String getBio() {
    return this.bio;
  }

  /** @return The tags associated with a user. */
  public ArrayList<String> getTags() {
    return this.interestTags;
  }

  /** @return the GitHub ID of a user. */
  public String getGitHubID() {
    return this.gitHubID;
  }

  /** @return the image associated with a user. */
  public String getImage() {
    return this.image;
  }

  /** @return the location associated with a user according to GitHub. */
  public String getLocation() {
    return this.location;
  }

  /** @return the email associated with a user according to GitHub. */
  public String getEmail() {
    return this.email;
  }

  /** @return the URL of the current User page on GitHub. */
  public String getGitHubURL() {
    return this.gitHubURL;
  }
}
