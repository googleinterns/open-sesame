package com.google.opensesame.projects;

import java.io.IOException;
import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.opensesame.github.GitHubGetter;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class ProjectPreview {
  public static ProjectPreview fromEntity(Entity projectEntity) throws IOException {
    GitHub gitHub = GitHubGetter.getGitHub();
    GHRepository repository = 
        gitHub.getRepository((String) projectEntity.getProperty("repositoryName"));

    return new ProjectPreview(
        repository.getName(),
        repository.getDescription(),
        ProjectTag.fromRepository(repository),
        (int) projectEntity.getProperty("numMentors"),
        repository); 
  }

  private final String title;
  private final String shortDescription;
  private final List<ProjectTag> projectTags;
  private final int numMentors;
  private final transient GHRepository repository;
  
  public ProjectPreview(
      String title, 
      String shortDescription, 
      List<ProjectTag> projectTags, 
      int numMentors, 
      GHRepository repository) {
    this.title = title;
    this.shortDescription = shortDescription;
    this.projectTags = projectTags;
    this.numMentors = numMentors;
    this.repository = repository;
  }

  public String getTitle() {
    return title;
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