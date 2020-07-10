package com.google.opensesame.projects;

import com.google.appengine.api.datastore.PropertyContainer;
import com.google.opensesame.github.GitHubGetter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
  }

  public int getNumMentors() {
    return this.mentorIds.size();
  }

  public int getNumInterestedUsers() {
    return this.interestedUserIds.size();
  }

  /**
   * Creates a map of properties from this ProjectEntity object.
   *
   * @return Returns the map of properties.
   */
  public Map<String, Object> createProperties() {
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put(REPOSITORY_ID_KEY, repositoryId);
    properties.put(MENTOR_IDS_KEY, mentorIds);
    properties.put(INTERESTED_USER_IDS_KEY, interestedUserIds);
    properties.put(NUM_MENTORS_KEY, getNumMentors());
    properties.put(NUM_INTERESTED_USERS_KEY, getNumInterestedUsers());

    return properties;
  }

  /**
   * Adds the properties of this ProjectEntity object to a PropertyContainer, which will most likely
   * be a datastore entity, which is a child of PropertyContainer:
   * https://cloud.google.com/appengine/docs/standard/java/javadoc/com/google/appengine/api/datastore/Entity
   *
   * @param propertyContainer The property container to add this ProjectEntity's properties to.
   */
  public void fillPropertyContainer(PropertyContainer propertyContainer) {
    Map<String, Object> projectEntityProperties = createProperties();
    projectEntityProperties.forEach(
        (String key, Object value) -> {
          propertyContainer.setProperty(key, value);
        });
  }
}
