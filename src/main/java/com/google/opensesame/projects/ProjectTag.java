package com.google.opensesame.projects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.github.GHRepository;

public class ProjectTag {
  // ProjectTag class is used instead of just a string in anticipation of future tag types.

  public static List<ProjectTag> fromRepository(GHRepository repository) throws IOException {
    List<String> topics = repository.listTopics();
    String primaryLanguage = repository.getLanguage();

    ArrayList<ProjectTag> projectTags = new ArrayList<ProjectTag>(topics.size() + 1);
    projectTags.add(new ProjectTag(primaryLanguage));
    for (String topic : topics) {
      projectTags.add(new ProjectTag(topic));
    }

    return projectTags;
  }

  public ProjectTag(String tagText) {
    this.tagText = tagText;
  }

  private final String tagText;

  public String getTagText() {
    return tagText;
  }
}