package com.google.opensesame.user;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.opensesame.auth.AuthServlet;
import com.google.opensesame.github.GitHubGetter;
import com.google.opensesame.projects.ProjectData;
import com.google.opensesame.projects.ProjectEntity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

/**
 * A fully fleshed out User of OpenSesame containing information sourced from GitHub. To conserve
 * API calls, It would be best to only build UserData where neccesary. The main use of this class is
 * to send data to the frontend as JSON with Gson. It is essentially a serialization tool.
 */
public class UserData {
  private Boolean isMentor;
  private final ArrayList<ProjectData> projects = new ArrayList<ProjectData>();
  private final ArrayList<String> interestTags;
  private final String bio;
  private final String gitHubID;
  private final String gitHubURL;
  private final String image;
  private final String location;
  private final String userID;
  private String email;
  private String name;

  /**
   * Create a UserData from a given UserEntity
   *
   * <p>Note: Emails will only ever be stored in a UserObject if the current user is authorized with
   * the AuthServlet. This is to avoid unauthorised users from contacting our mentors. We want to
   * avoid email spam as much as possible and we do not see the need to share user emails
   * unathorized personnel.
   *
   * <p>Note: If a user's name is not defined on GitHub, this datastructure will set the user's
   * GitHub login ID as the user's name. i.e {@code if (name == null);} {@code name = gitHubID}
   *
   * @param userEntity
   * @throws IOException
   */
  public UserData(UserEntity userEntity) throws IOException {
    gitHubID = userEntity.gitHubId;
    interestTags = userEntity.interestTags;
    userID = userEntity.userId;
    email = userEntity.email;
    // Only ever send email address when a user is authorized
    if (AuthServlet.getAuthorizedUser() == null) {
      email = null;
    }
    isMentor = userEntity.isMentor;

    // GitHub query
    loadProjects(userEntity.projectIds);
    final GitHub gitHub = GitHubGetter.getGitHub();
    final GHUser userGitAccount = gitHub.getUser(gitHubID);
    bio = userGitAccount.getBio();
    image = userGitAccount.getAvatarUrl();
    location = userGitAccount.getLocation();
    name = userGitAccount.getName();
    if (name == null) {
      name = gitHubID;
    }
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

  /** @return a list of of projects a user is involved in. */
  public ArrayList<ProjectData> getProjects() {
    return projects;
  }

  /**
   * Add the {@code ProjectData} associated with the projects in {@code projectIds} to {@code
   * this.projects}.
   *
   * @param projectIds
   */
  private void loadProjects(ArrayList<String> projectIds) throws IOException {
    Map<String, ProjectEntity> projectEntities =
        ofy().load().type(ProjectEntity.class).ids(projectIds);
    for (ProjectEntity entity : projectEntities.values()) {
      ProjectData curProject = new ProjectData(entity);
      curProject.getName();
      projects.add(curProject);
    }
  }

  /**
   * @param {UserData} user2 the user to compare to
   * @return an integer 'compatibility score' which represents how many shared interests two users
   *     have.
   */
  public int compatibility(UserData user2) {
    Set<String> commonInterests = interestTags.stream()
        .distinct()
        .filter(user2.interestTags::contains)
        .collect(Collectors.toSet());
    return commonInterests.size();
  }
}
