package com.google.opensesame.projects;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.opensesame.github.GitHubGetter;
import com.google.opensesame.user.UserData;
import com.google.opensesame.user.UserEntity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHFileNotFoundException;
import org.kohsuke.github.GHRepository;

/**
 * A read-only utility class for converting a ProjectEntity datastore object into a POJO for JSON
 * serialization.
 */
public class ProjectData {
  private transient GHRepository repository;
  private transient ProjectEntity projectEntity;
  private String name = null;
  private String description = null;
  private List<String> topicTags = null;
  private String primaryLanguage = null;
  private Integer numMentors = null;
  private String repositoryId = null;
  private List<UserData> mentors = null;
  private String readmeRawUrl = null;
  private String readmeHtmlUrl = null;

  /**
   * Create a ProjectData object from a ProjectEntity and its associated GitHub repository.
   *
   * <p>The main use of this constructor is for testing, where the GitHub repository must be mocked.
   * However, in the case where the GHRepository associated with a ProjectEntity has already been
   * created, it would be preferred to use this constructor to save GitHub API calls by not
   * recreating the GHRepository.
   *
   * @param projectEntity
   * @param repository
   * @throws IllegalArgumentException Throws if the ProjectEntity does not have the same repository
   *     ID as the supplied GitHub repository.
   */
  public ProjectData(ProjectEntity projectEntity, GHRepository repository)
      throws IllegalArgumentException {
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
   *
   * @param projectEntity
   * @throws IOException Throws if there was an error retrieving the GitHub repository associated
   *     with this ProjectEntity.
   */
  public ProjectData(ProjectEntity projectEntity) throws IOException {
    this(projectEntity, GitHubGetter.getGitHub().getRepositoryById(projectEntity.repositoryId));
  }

  /**
   * Gets the name of the repository or returns a cached value if it exists.
   *
   * @return Returns the name of the repository.
   */
  public String getName() {
    if (name == null) {
      name = repository.getName();
    }

    return name;
  }

  /**
   * Gets the repository description and caches it or returns a cached value if it exists.
   *
   * @return Returns the repository description.
   */
  public String getDescription() {
    if (description == null) {
      description = repository.getDescription();
    }

    return description;
  }

  /**
   * Gets the repository topic tags and caches them or returns cached values if they exist.
   *
   * @return Returns the repository topic tags.
   */
  public List<String> getTopicTags() throws IOException {
    if (topicTags == null) {
      topicTags = repository.listTopics();
    }

    return Collections.unmodifiableList(topicTags);
  }

  /**
   * Gets the repository's primary language and caches it or returns a cached value if it exists.
   *
   * @return Returns the repository's primary language.
   */
  public String getPrimaryLanguage() {
    if (primaryLanguage == null) {
      primaryLanguage = repository.getLanguage();
    }

    return primaryLanguage;
  }

  /**
   * Gets the number of mentors and caches it or returns a cached value if it exists.
   *
   * @return Returns the number of mentors.
   */
  public int getNumMentors() {
    if (numMentors == null) {
      numMentors = projectEntity.numMentors;
    }

    return numMentors;
  }

  /**
   * Gets the repository ID and caches it or returns a cached value if it exists.
   *
   * @return Returns the repository ID.
   */
  public String getRepositoryId() {
    if (repositoryId == null) {
      repositoryId = projectEntity.repositoryId;
    }

    return repositoryId;
  }

  /**
   * Gets the mentors and caches them or returns cached values if they exist.
   *
   * @return Returns the mentors.
   */
  public List<UserData> getMentors() throws IOException {
    if (mentors == null) {
      Map<String, UserEntity> userEntities =
          ofy().load().type(UserEntity.class).ids(projectEntity.mentorIds);
      mentors = new ArrayList<UserData>();
      for (UserEntity entity : userEntities.values()) {
        mentors.add(new UserData(entity));
      }
    }

    return Collections.unmodifiableList(mentors);
  }

  private transient GHContent repositoryReadme = null;
  private transient boolean repositoryReadmeLoaded = false;
  /**
   * Gets the repository README.md file or returns a cached version if it exists. If the repository
   * does not have a README.md file, returns null.
   *
   * @return Returns the repository README.md file or returns null if the repository does not have
   *     one.
   */
  private GHContent getRepositoryReadme() throws IOException {
    if (!repositoryReadmeLoaded) {
      try {
        repositoryReadme = repository.getReadme();
      } catch (GHFileNotFoundException e) {
        repositoryReadme = null;
      }

      repositoryReadmeLoaded = true;
    }

    return repositoryReadme;
  }

  /**
   * Gets the URL to the raw markdown of the project README.md file. If the project has no README.md
   * file, returns null.
   *
   * @return Returns the URL to the raw markdown of the project README.md file or null if the
   *     project doesn't have a README.md file.
   * @throws IOException
   */
  public String getReadmeRawUrl() throws IOException {
    GHContent repositoryReadme = getRepositoryReadme();
    if (repositoryReadme != null) {
      readmeRawUrl = repositoryReadme.getDownloadUrl();
    }

    return readmeRawUrl;
  }

  /**
   * Gets the URL to the GitHub HTML page displaying the project README.md file. If the project has
   * no README.md file, returns null.
   *
   * @return Returns the URL to the GitHub HTML page displaying the project README.md file or null
   *     if the project doesn't have a README.md file.
   * @throws IOException
   */
  public String getReadmeHtmlUrl() throws IOException {
    GHContent repositoryReadme = getRepositoryReadme();
    if (repositoryReadme != null) {
      readmeHtmlUrl = repositoryReadme.getHtmlUrl();
    }

    return readmeHtmlUrl;
  }
}
