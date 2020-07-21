package com.google.opensesame.user;

import com.google.opensesame.auth.AuthServlet;
import com.google.opensesame.github.GitHubGetter;
import java.io.IOException;
import java.util.ArrayList;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

/**
 * A fully fleshed out User of OpenSesame containing information sourced from GitHub. To conserve
 * API calls, It would be best to only build UserData where neccesary. The main use of this class is
 * to send data to the frontend as a JSON with Gson. It is essentially a serialization tool.
 */
public class UserData {
  private Boolean isMentor;
  private final ArrayList<String> interestTags;
  private final ArrayList<String> menteeIds;
  private final ArrayList<String> projectIds;
  private final String bio;
  private final String gitHubID;
  private final String gitHubURL;
  private final String image;
  private final String location;
  private final String name;
  private final String userID;
  private String email;

  /**
   * Create a UserData from a given UserEntity
   *
   * Note: Emails will only ever be stored in a UserObject if the current user is authorized with
   * the AuthServlet.
   *
   * @param userEntity
   * @throws IOException
   */
  public UserData(UserEntity userEntity) throws IOException {
    gitHubID = userEntity.gitHubId;
    interestTags = userEntity.interestTags;
    userID = userEntity.userId;
    email = userEntity.email;
    // Only ever send email stuff when a user is authorized
    if (AuthServlet.getAuthorizedUser() == null) {
      email = null;
    }
    projectIds = userEntity.projectIds;
    menteeIds = userEntity.menteeIds;
    isMentor = userEntity.isMentor;

    // GitHub query TODO: make this more testable by extracting GitHub.
    final GitHub gitHub = GitHubGetter.getGitHub();
    final GHUser userGitAccount = gitHub.getUser(gitHubID);
    bio = userGitAccount.getBio();
    image = userGitAccount.getAvatarUrl();
    location = userGitAccount.getLocation();
    name = userGitAccount.getName();
    gitHubURL = userGitAccount.getHtmlUrl().toString();
  }

  /** @return true if user is a mentor for a project, false otherwise. */
  public Boolean isMentor() {
    return isMentor;
  }

  /** @return The name of a user. */
  public String getName() {
    return name;
  }

  /** @return The bio of a user from GitHub. */
  public String getBio() {
    return bio;
  }

  /** @return The tags associated with a user. */
  public ArrayList<String> getTags() {
    return interestTags;
  }

  /** @return the GitHub ID of a user. */
  public String getGitHubID() {
    return gitHubID;
  }

  /** @return the image associated with a user. */
  public String getImage() {
    return image;
  }

  /** @return the location associated with a user according to GitHub. */
  public String getLocation() {
    return location;
  }

  /** @return the email associated with a user according to GitHub. */
  public String getEmail() {
    return email;
  }

  /** @return the URL of the current User page on GitHub. */
  public String getGitHubURL() {
    return gitHubURL;
  }

  /** @return the URL of the current User page on GitHub. */
  public String getUserID() {
    return userID;
  }

  /** @return a list of ids of projects a user is involved in. */
  public ArrayList<String> getProjectIDs() {
    return projectIds;
  }

  /** @return a list of ids of mentees a user is currently mentoring. */
  public ArrayList<String> getMenteeIDs() {
    return menteeIds;
  }

  /**
   * Add a project to the list of projects associated with this instance of UserData. Remember to
   * store this change in datastore with a UserEntity.
   */
  public void addProject(String projectId) {
    projectIds.add(projectId);
    isMentor = true;
  }

  /**
   * Add a project to the list of projects associated with this instance of UserData. Remember to
   * store this change in datastore with a UserEntity.
   */
  public void addMentee(String menteeId) {
    projectIds.add(menteeId);
  }
}
