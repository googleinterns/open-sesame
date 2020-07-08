package com.google.opensesame.projects;

import com.google.opensesame.github.GitHubGetter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * A class containing basic project data for previewing a project. This is intended to be serialized
 * and sent to the client.
 */
public class ProjectPreviewData {
  /**
   * Creates a ProjectPreview object from a set of project properties.
   *
   * @param properties The properties of a project entity. This can come directly from a datastore
   *     entity, as datastore entities inherit a getProperty() method from PropertyContainer:
   *     https://cloud.google.com/appengine/docs/standard/java/javadoc/com/google/appengine/api/datastore/Entity
   * @return Returns the created ProjectPreview.
   * @throws IOException Throws when there is an error communicating with the GitHub API.
   */
  public static ProjectPreviewData fromProperties(Map<String, Object> properties)
      throws IOException {
    GitHub gitHub = GitHubGetter.getGitHub();
    GHRepository repository =
        gitHub.getRepositoryById((String) properties.get(ProjectEntity.REPOSITORY_ID_KEY));

    return fromPropertiesAndRepository(properties, repository);
  }

  /**
   * Creates a ProjectPreview object from a set of project properties and its associated GitHub
   * repository.
   *
   * @param properties The properties of a project entity. This can come directly from a datastore
   *     entity, as datastore entities inherit a getProperty() method from PropertyContainer:
   *     https://cloud.google.com/appengine/docs/standard/java/javadoc/com/google/appengine/api/datastore/Entity
   * @param repository The GitHub repository associated with the project.
   * @return Returns the created ProjectPreview
   * @throws IOException Throws when there is an error communicating with the GitHub API.
   */
  public static ProjectPreviewData fromPropertiesAndRepository(
      Map<String, Object> properties, GHRepository repository) throws IOException {
    return new ProjectPreviewData(
        repository.getName(),
        repository.getDescription(),
        repository.listTopics(),
        repository.getLanguage(),
        (int) properties.get(ProjectEntity.NUM_MENTORS_KEY),
        (String) properties.get(ProjectEntity.REPOSITORY_ID_KEY));
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
