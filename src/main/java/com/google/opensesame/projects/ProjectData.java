package com.google.opensesame.projects;

import com.google.opensesame.github.GitHubGetter;
import com.google.opensesame.servlets.MentorObject;
import com.google.opensesame.servlets.PersonBuilder;
import java.io.IOException;
import java.util.ArrayList;
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
    List<MentorObject> mentors = createMentorMockData();

    return new ProjectData(previewData, mentors);
  }

  /**
   * This is a temporary function that creates mentor mock data. This will be removed when a helper
   * function is created for getting mentors by ID.
   *
   * @return Returns the mentor mock data.
   * @throws IOException
   */
  private static List<MentorObject> createMentorMockData() throws IOException {
    List<MentorObject> mentors = new ArrayList<MentorObject>();

    ArrayList<String> ObiSkills = new ArrayList<String>();
    ObiSkills.add("Meme god");
    ObiSkills.add("HTML wrangler");
    MentorObject Obi =
        new PersonBuilder()
            .name("Obi")
            .gitHubID("Obinnabii")
            .description("Obi is awesome.")
            .interestTags(ObiSkills)
            .buildMentor();
    mentors.add(Obi);

    ArrayList<String> SamiSkills = new ArrayList<String>();
    SamiSkills.add("Stone carver");
    SamiSkills.add("Bootstrap convert");
    MentorObject Sami =
        new PersonBuilder()
            .name("Sami")
            .gitHubID("Sami-2000")
            .description("Sami is fun.")
            .interestTags(SamiSkills)
            .buildMentor();
    mentors.add(Sami);

    ArrayList<String> RichiSkills = new ArrayList<String>();
    RichiSkills.add("Minecraft boss");
    RichiSkills.add("React wizard");
    MentorObject Richi =
        new PersonBuilder()
            .name("Richi")
            .gitHubID("Richie78321")
            .description("Richi is cool.")
            .interestTags(RichiSkills)
            .buildMentor();
    mentors.add(Richi);

    return mentors;
  }

  // This is currently in its most basic form. In the future there will be more data that differs
  // from the preview data.
  private final ProjectPreviewData previewData;
  private final List<MentorObject> mentors;

  public ProjectData(ProjectPreviewData previewData, List<MentorObject> mentors) {
    this.previewData = previewData;
    this.mentors = mentors;
  }
}
