package com.google.opensesame.projects;

import com.google.opensesame.github.GitHubGetter;
import com.google.opensesame.user.UserData;
import java.io.IOException;
import java.util.List;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * A class containing all relevant data for a project. This is intended to be serialized and sent to
 * the client.
 */
public class ProjectData {
  /**
   * Creates project data from a ProjectEntity.
   *
   * @param projectEntity A project entity from datastore or manually created.
   * @return Returns the created ProjectData.
   * @throws IOException Throws when there is an error communicating with the GitHub API.
   */
  public static ProjectData fromProjectEntity(ProjectEntity projectEntity) throws IOException {
    GitHub gitHub = GitHubGetter.getGitHub();
    GHRepository repository = gitHub.getRepositoryById(projectEntity.repositoryId);

    return fromProjectEntity(projectEntity, repository);
  }

  /**
   * Creates project data from a ProjectEntity and the associated GitHub repository.
   *
   * @param projectEntity A project entity from datastore or manually created.
   * @param repository The GitHub repository associated with the project.
   * @return Returns the created ProjectData.
   * @throws IOException Throws when there is an error communicating with the GitHub API.
   */
  public static ProjectData fromProjectEntity(ProjectEntity projectEntity, GHRepository repository)
      throws IOException {
    ProjectPreviewData previewData =
        ProjectPreviewData.fromProjectEntity(projectEntity, repository);

    // TODO : Use helper function to get mentors by ID.
    // Will use mock data for now.
    List<UserData> mentors = createMentorMockData();

    return new ProjectData(previewData, mentors);
  }

  /**
   * This is a temporary function that creates mentor mock data. This will be removed when a helper
   * function is created for getting mentors by ID.
   *
   * @return Returns the mentor mock data.
   * @throws IOException
   */
  private static List<UserData> createMentorMockData() throws IOException {
    return null;
  }

  // This is currently in its most basic form. In the future there will be more data that differs
  // from the preview data.
  private final ProjectPreviewData previewData;
  private final List<UserData> mentors;

  public ProjectData(ProjectPreviewData previewData, List<UserData> mentors) {
    this.previewData = previewData;
    this.mentors = mentors;
  }
}
