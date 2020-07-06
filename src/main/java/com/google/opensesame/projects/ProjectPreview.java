package com.google.opensesame.projects;

import com.google.opensesame.github.GitHubGetter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class ProjectPreview {
  /**
   * Creates a ProjectPreview object from a set of project properties.
   *
   * @param properties The properties of a project entity. This can come directly from a datastore
   *     entity, as datastore entities inherit a getProperty() method from PropertyContainer:
   *     https://cloud.google.com/appengine/docs/standard/java/javadoc/com/google/appengine/api/datastore/Entity
   * @return Returns the created ProjectPreview.
   * @throws IOException Throws when there is an error communicating with the GitHub API.
   */
  public static ProjectPreview fromProperties(Map<String, Object> properties) throws IOException {
    GitHub gitHub = GitHubGetter.getGitHub();
    GHRepository repository = gitHub.getRepository((String) properties.get("repositoryName"));

    return fromPropertiesAndRepository(properties, repository);
  }

  /**
   * Creates a ProjectPreview object from a set of project properties and it's associated GitHub
   * repository.
   *
   * <p><<<<<<< HEAD
   *
   * @param properties The properties of a project entity. This can come directly from a datastore
   *     entity, as datastore entities inherit a getProperty() method from PropertyContainer:
   *     https://cloud.google.com/appengine/docs/standard/java/javadoc/com/google/appengine/api/datastore/Entity
   *     =======
   * @param properties The properties of a project. This will most likely come from a datastore
   *     entity. >>>>>>> 7ca3f2ef3ec1a4d9060825148bbd0eab664fbf30
   * @param repository The GitHub repository associated with the project.
   * @return Returns the created ProjectPreview
   * @throws IOException Throws when there is an error communicating with the GitHub API.
   */
  public static ProjectPreview fromPropertiesAndRepository(
      Map<String, Object> properties, GHRepository repository) throws IOException {
    return new ProjectPreview(
        repository.getName(),
        repository.getDescription(),
        repository.listTopics(),
        repository.getLanguage(),
        (int) properties.get("numMentors"),
        repository);
  }

  private final String name;
  private final String description;
  private final List<String> topicTags;
  private final String primaryLanguage;
  private final int numMentors;
  private final transient GHRepository repository;

  /**
   * Creates a new ProjectPreview object.
   *
   * @param name
   * @param description
   * @param topicTags
   * @param numMentors
   * @param repository
   */
  public ProjectPreview(
      String name,
      String description,
      List<String> topicTags,
      String primaryLanguage,
      int numMentors,
      GHRepository repository) {
    this.name = name;
    this.description = description;
    this.topicTags = topicTags;
    this.primaryLanguage = primaryLanguage;
    this.numMentors = numMentors;
    this.repository = repository;
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

  public GHRepository getRepository() {
    return repository;
  }
}
