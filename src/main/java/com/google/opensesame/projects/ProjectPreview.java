package com.google.opensesame.projects;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.opensesame.github.GitHubGetter;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class ProjectPreview {
  public static ProjectPreview fromProperties(Map<String, Object> properties) throws IOException {
    GitHub gitHub = GitHubGetter.getGitHub();
    GHRepository repository = 
        gitHub.getRepository((String) properties.get("repositoryName"));

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