package com.google.opensesame.projects;

import com.google.opensesame.github.GitHubGetter;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/** The class used to interact with the project entities in data storage. */
@Entity
public class ProjectEntity {
  /**
   * Creates a new ProjectEntity from a GitHub repository name.
   *
   * @param repositoryName
   * @return Returns the new ProjectEntity or null if the repository name is invalid.
   * @throws IOException
   */
  public static ProjectEntity fromRepositoryName(String repositoryName) throws IOException {
    GitHub gitHub = GitHubGetter.getGitHub();
    GHRepository repository;
    try {
      repository = gitHub.getRepository(repositoryName);
    } catch (IOException e) {
      return null;
    }

    return new ProjectEntity(
        String.valueOf(repository.getId()), new ArrayList<String>(), new ArrayList<String>());
  }

  @Id public String repositoryId;
  public List<String> mentorIds;
  public List<String> interestedUserIds;
  @Index protected int numMentors;
  @Index protected int numInterestedUsers;

  /**
   * No-arg constructor for Objectify:
   * https://github.com/objectify/objectify/wiki/Entities#the-basics
   */
  protected ProjectEntity() {}
  
  /**
   * Creates a new ProjectEntity object.
   *
   * @param repositoryId
   * @param mentorIds
   * @param interestedUserIds
   */
  public ProjectEntity(
      String repositoryId, List<String> mentorIds, List<String> interestedUserIds) {
    this.repositoryId = repositoryId;
    this.mentorIds = mentorIds;
    this.interestedUserIds = interestedUserIds;
  }

  /**
   * Compute the lengths of mentor and interested user lists before the
   * project entity is saved.
   */
  @OnSave protected void computeListLengths() {
    numMentors = mentorIds.size();
    numInterestedUsers = interestedUserIds.size();
  }
  
  public int getNumMentors() {
    return this.mentorIds.size();
  }

  public int getNumInterestedUsers() {
    return this.interestedUserIds.size();
  }
}
