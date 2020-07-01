package com.google.opensesame.projects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.kohsuke.github.GHRepository;

public class ProjectTag {
  /**
   * Creates a list of ProjectTags from a GitHub repository's topics and primary language.
   *
   * @param repository The GitHub repository.
   * @return Returns the list of ProjectTags.
   * @throws IOException
   */
  public static List<ProjectTag> fromRepository(GHRepository repository) throws IOException {
    List<String> topics = repository.listTopics();
    ArrayList<ProjectTag> projectTags = new ArrayList<ProjectTag>();

    String primaryLanguage = repository.getLanguage();
    projectTags.add(new ProjectTag(primaryLanguage));
    for (String topic : topics) {
      projectTags.add(new ProjectTag(topic));
    }

    return projectTags;
  }

  /**
   * Creates a new ProjectTag.
   *
   * @param tagText
   */
  public ProjectTag(String tagText) {
    this.tagText = tagText;
  }

  private final String tagText;

  public String getTagText() {
    return tagText;
  }
}
