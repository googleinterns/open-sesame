package com.google.opensesame.projects;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.opensesame.github.GitHubGetter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/** The class used to interact with the project entities in data storage. */
public class ProjectEntity {
  public static final String REPOSITORY_ID_KEY = "repositoryId";
  public static final String NUM_MENTORS_KEY = "numMentors";
  public static final String NUM_INTERESTED_USERS_KEY = "numInterestedUsers";
  public static final String MENTOR_IDS_KEY = "mentorIds";
  public static final String INTERESTED_USER_IDS_KEY = "interestedUserIds";

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

  /**
   * Creates a ProjectEntity from the properties of an existing entity in datastore. Assumes that
   * that all of the required keys are populated in the properties.
   *
   * @param properties The properties of an existing datastore entity.
   * @return Returns the new ProjectEntity.
   */
  public static ProjectEntity fromProperties(Map<String, Object> properties) {
    // Warning is suppressed because it is a precondition that the properties are from a
    // project entity.
    @SuppressWarnings({"unchecked"})
    ProjectEntity newEntity =
        new ProjectEntity(
            (String) properties.get(REPOSITORY_ID_KEY),
            (List<String>) properties.get(MENTOR_IDS_KEY),
            (List<String>) properties.get(INTERESTED_USER_IDS_KEY));
    return newEntity;
  }

  public final String repositoryId;
  public List<String> mentorIds;
  public List<String> interestedUserIds;

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

    // Empty lists are represented as null in datastore, so create empty lists if null:
    // https://cloud.google.com/appengine/docs/standard/java/datastore/entity-property-reference#Using_an_empty_list
    if (this.mentorIds == null) {
      this.mentorIds = new ArrayList<String>();
    }
    if (this.interestedUserIds == null) {
      this.interestedUserIds = new ArrayList<String>();
    }
  }

  public int getNumMentors() {
    return this.mentorIds.size();
  }

  public int getNumInterestedUsers() {
    return this.interestedUserIds.size();
  }
  
  /**
   * Adds the properties of this ProjectEntity object to an Entity.
   * @param entity The Datastore Entity to add this ProjectEntity's properties to.
   */
  public void fillEntity(Entity entity) {
    entity.setProperty(REPOSITORY_ID_KEY, repositoryId);
    entity.setProperty(MENTOR_IDS_KEY, mentorIds);
    entity.setProperty(INTERESTED_USER_IDS_KEY, interestedUserIds);
    entity.setProperty(NUM_MENTORS_KEY, getNumMentors());
    entity.setProperty(NUM_INTERESTED_USERS_KEY, getNumInterestedUsers());
  }

  /**
   * Convenience method for saving the project entity to the datastore.
   * @return Returns the key associated with the datastore entry.
   */
  public Key save() {
    return ProjectDatastore.addProjectEntity(this);
  }
}
