package com.google.opensesame.projects;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.opensesame.github.GitHubGetter;
import com.google.opensesame.user.UserData;
import com.google.opensesame.user.UserEntity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.kohsuke.github.GHRepository;

/**
 * A class containing the data for a project. This is intended to be serialized and sent to
 * the client. A ProjectData object can define as many or as few of the fields as necessary
 * depending on the use case, as undefined values will not be serialized to JSON and will therefore
 * not be sent to the client.
 */
public class ProjectData {    
  private transient GHRepository repository;
  private transient ProjectEntity projectEntity;
  public String name;
  public String description;
  public List<String> topicTags;
  public String primaryLanguage;
  public Integer numMentors;
  public String repositoryId;
  public List<UserData> mentors;

  /**
   * Create a ProjectData object from a ProjectEntity and its associated GitHub repository. 
   * 
   * The main use of this constructor is for testing, where the GitHub repository must be mocked.
   * However, in the case where the GHRepository associated with a ProjectEntity has already been
   * created, it would be preferred to use this constructor to save GitHub API calls by not
   * recreating the GHRepository.
   * @param projectEntity
   * @param repository
   * @throws IllegalArgumentException Throws if the ProjectEntity does not have the same
   *    repository ID as the supplied GitHub repository.
   */
  public ProjectData(
      ProjectEntity projectEntity, GHRepository repository) throws IllegalArgumentException {
    if (!Long.toString(repository.getId()).equals(projectEntity.repositoryId)) {
      throw new IllegalArgumentException(
          "The project entity and its GitHub repository do not share the same repository ID.");
    }

    this.projectEntity = projectEntity;
    this.repository = repository;
    repositoryId = projectEntity.repositoryId;
  }

  /**
   * Create a ProjectData object from a ProjectEntity.
   * @param projectEntity
   * @throws IOException Throws if there was an error retrieving the GitHub repository associated
   *    with this ProjectEntity.
   */
  public ProjectData(ProjectEntity projectEntity) throws IOException {
    this(projectEntity, GitHubGetter.getGitHub().getRepositoryById(projectEntity.repositoryId));
  }

  /**
   * Assigns the name of this project.
   * @return Returns the ProjectData instance with the data assigned.
   */
  public ProjectData withName() {
    name = repository.getName();

    return this;
  }

  /**
   * Creates and assigns the data needed to preview the project on the frontend.
   * @return Returns the ProjectData instance with the data assigned.
   * @throws IOException Throws if there is an error communicating with the GitHub API.
   */
  public ProjectData withPreviewData() throws IOException {
    this.withName();

    description = repository.getDescription();
    topicTags = repository.listTopics();
    primaryLanguage = repository.getLanguage();
    numMentors = projectEntity.mentorIds.size();
    
    return this;
  }

  /**
   * Creates and assigns the data needed to view the full breakout page of a project on the
   * frontend.
   * @return Returns the ProjectData instance with the data assigned.
   * @throws IOException Throws if there is an error communicating with the GitHub API.
   */
  public ProjectData withFullData() throws IOException {
    this.withPreviewData();

    Map<String, UserEntity> userEntities =
        ofy().load().type(UserEntity.class).ids(projectEntity.mentorIds);
    mentors = new ArrayList<UserData>();
    for (UserEntity entity : userEntities.values()) {
      mentors.add(new UserData(entity));
    }

    return this;
  }
}
