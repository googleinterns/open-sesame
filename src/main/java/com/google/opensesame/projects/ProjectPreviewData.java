package com.google.opensesame.projects;

import com.google.opensesame.github.GitHubGetter;
import java.io.IOException;
import java.util.List;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * A class containing basic project data for previewing a project. This is intended to be serialized
 * and sent to the client.
 */
public class ProjectPreviewData {
  /**
   * Creates project preview data from a ProjectEntity.
   *
   * @param projectEntity A project entity from datastore or manually created.
   * @return Returns the created ProjectPreviewData.
   * @throws IOException Throws when there is an error communicating with the GitHub API.
   */
  public static ProjectPreviewData fromProjectEntity(ProjectEntity projectEntity)
      throws IOException {
    GitHub gitHub = GitHubGetter.getGitHub();
    GHRepository repository = gitHub.getRepositoryById(projectEntity.repositoryId);

    return fromProjectEntity(projectEntity, repository);
  }

  /**
   * Creates project preview data from a ProjectEntity and the GitHub repository.
   *
   * @param projectEntity A project entity from datastore or manually created.
   * @param repository The GitHub repository associated with the project.
   * @return Returns the created ProjectPreviewData.
   * @throws IOException Throws when there is an error communicating with the GitHub API.
   */
  public static ProjectPreviewData fromProjectEntity(
      ProjectEntity projectEntity, GHRepository repository) throws IOException {
    return new ProjectPreviewData(
        repository.getName(),
        repository.getDescription(),
        repository.listTopics(),
        repository.getLanguage(),
        (int) projectEntity.getNumMentors(),
        projectEntity.repositoryId);
  }

  private final String name;
  private final String description;
  private final List<String> topicTags;
  private final String primaryLanguage;
  private final int numMentors;
  private final String repositoryId;

  /**
   * Creates a new ProjectPreview object.
   *
   * @param name
   * @param description
   * @param topicTags
   * @param numMentors
   * @param repository
   */
  public ProjectPreviewData(
      String name,
      String description,
      List<String> topicTags,
      String primaryLanguage,
      int numMentors,
      String repositoryId) {
    this.name = name;
    this.description = description;
    this.topicTags = topicTags;
    this.primaryLanguage = primaryLanguage;
    this.numMentors = numMentors;
    this.repositoryId = repositoryId;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public List<String> getTopicTags() {
    return topicTags;
  }

  public String getPrimaryLanguage() {
    return primaryLanguage;
  }

  public int getNumMentors() {
    return numMentors;
  }

  public String getRepositoryId() {
    return repositoryId;
  }
}
