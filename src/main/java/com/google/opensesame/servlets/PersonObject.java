package com.google.opensesame.servlets;


import com.google.opensesame.github.GitHubGetter;

import java.io.IOException;

import java.util.ArrayList;

import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

/**
 * A class containing basic user data. This is intended to be serialized
 * and sent to the client.
 */
public class PersonObject {
  public static String ENTITY_NAME = "User";
  public static String GITHUB_ID_FIELD = "github-id";
  public static String TAG_LIST_FIELD = "tags";

  private final ArrayList<String> interestTags;
  private final String bio;
  private final String email;
  private final String gitHubID;
  private final String image;
  private final String location;
  private final String name;
  private final String gitHubURL;

  /**
   * Creates a PersonObject with the given parameters 
   * @param name
   * @param gitHubID
   * @param bio
   * @param interestTags
   * @throws IOException
   */
  public PersonObject(String gitHubID, ArrayList<String> interestTags)
      throws IOException {
    this.gitHubID = gitHubID;
    this.interestTags = interestTags;

    //GitHub query
    final GitHub gitHub = GitHubGetter.getGitHub();
    final GHUser userGitAccount = gitHub.getUser(gitHubID);
    this.bio = userGitAccount.getBio();
    this.email = userGitAccount.getEmail();
    this.image = userGitAccount.getAvatarUrl();
    this.location = userGitAccount.getLocation();
    this.name = userGitAccount.getName();
    this.gitHubURL = userGitAccount.getHtmlUrl().toString();
  }


/**
 * @return The name of a user.
 */
  public String getName() {
    return this.name;
  }


/**
 * @return The bio of a user from GitHub.
 */
  public String getBio() {
    return this.bio;
  }


/**
 * @return The tags associated with a user.
 */
  public ArrayList<String> getTags() {
    return this.interestTags;
  }


/**
 * @return the GitHub ID of a user.
 */
  public String getGitHubID() {
    return this.gitHubID;
  }


/**
 * @return the image associated with a user.
 */
public String getImage() {
  return this.image;
}

/**
 * @return the location associated with a user according to GitHub.
 */
public String getLocation() {
  return this.location;
}

/**
 * @return the email associated with a user according to GitHub.
 */
public String getEmail() {
  return this.email;
}

/**
 * @return the URL of the current User page on GitHub.
 */
public String getGitHubURL() {
  return this.gitHubURL;
}
}
