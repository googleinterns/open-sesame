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
   * @param properties The properties of a project. This will most likely come from a datastore
   *     entity.
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
   * @param properties The properties of a project. This will most likely come from a datastore
   *     entity.
   * @param repository The GitHub repository associated with the project.
   * @return Returns the created ProjectPreview
   * @throws IOException Throws when there is an error communicating with the GitHub API.
   */
  public static ProjectPreview fromPropertiesAndRepository(
      Map<String, Object> properties, GHRepository repository) throws IOException {
    return new ProjectPreview(
        repository.getName(),
        repository.getDescription(),
        ProjectTag.fromRepository(repository),
        (int) properties.get("numMentors"),
        repository);
  }

  private final String name;
  private final String shortDescription;
  private final List<ProjectTag> projectTags;
  private final int numMentors;
  private final transient GHRepository repository;

  /**
   * Creates a new ProjectPreview object.
   *
   * @param name
   * @param shortDescription
   * @param projectTags
   * @param numMentors
   * @param repository
   */
  public ProjectPreview(
      String name,
      String shortDescription,
      List<ProjectTag> projectTags,
      int numMentors,
      GHRepository repository) {
    this.name = name;
    this.shortDescription = shortDescription;
    this.projectTags = projectTags;
    this.numMentors = numMentors;
    this.repository = repository;
  }

  public String getName() {
    return name;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public List<ProjectTag> getProjectTags() {
    return projectTags;
  }

  public int getNumMentors() {
    return numMentors;
  }

  public GHRepository getRepository() {
    return repository;
  }
}
