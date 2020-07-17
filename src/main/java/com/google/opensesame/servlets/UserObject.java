package com.google.opensesame.servlets;

import com.google.opensesame.github.GitHubGetter;
import java.io.IOException;
import java.util.ArrayList;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

/**
 * A fully fleshed out User of OpenSesame containing information sourced from
 * GitHub. To conserve API calls, It would be best to only build a UserObject
 * with the UserBuilder where neccesary.
 */
public class UserObject {
  private final ArrayList<String> projectIDs;
  private final ArrayList<String> menteeIDs;
  private final ArrayList<String> interestTags;
  private final String bio;
  private final String email;
  private final String gitHubID;
  private final String gitHubURL;
  private final String image;
  private final String location;
  private final String name;
  private final String userID;
  private final Boolean isMentor;

  public UserObject(
      String userID,
      String gitHubID,
      ArrayList<String> interestTags,
      ArrayList<String> menteeIDs,
      ArrayList<String> projectIDs,
      String email)
      throws IOException {
    this.gitHubID = gitHubID;
    this.interestTags = interestTags;
    this.email = email;
    this.userID = userID;
    this.projectIDs = projectIDs;
    this.menteeIDs = menteeIDs;
    this.isMentor = projectIDs.isEmpty();

    // GitHub query TODO: alternate ways of getting this information.
    final GitHub gitHub = GitHubGetter.getGitHub();
    final GHUser userGitAccount = gitHub.getUser(gitHubID);
    this.bio = userGitAccount.getBio();
    this.image = userGitAccount.getAvatarUrl();
    this.location = userGitAccount.getLocation();
    this.name = userGitAccount.getName();
    this.gitHubURL = userGitAccount.getHtmlUrl().toString();
  }
  /** @return true if user is a mentor for a project, false otherwise. */
  public Boolean isMentor() {
    return this.isMentor;
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

  /** @return the URL of the current User page on GitHub. */
  public String getUserID() {
    return this.userID;
  }

  /** @return a list of ids of projects a user is involved in. */
  public ArrayList<String> getProjectIDs() {
    return this.projectIDs;
  }

  /** @return a list of ids of mentees a user is currently mentoring. */
  public ArrayList<String> getMenteeIDs() {
    return this.menteeIDs;
  }

  /**
   * Add a project to the list of projects associated with this instance of the mentor object.
   * Remember to store this change in datastore with a personEntity.
   */
  public void addProject(String projectID) {
    projectIDs.add(projectID);
  }

  /**
   * Add a project to the list of projects associated with this instance of the mentor object.
   * Remember to store this change in datastore with a personEntity.
   */
  public void addMentee(String menteeID) {
    projectIDs.add(menteeID);
  }
}
