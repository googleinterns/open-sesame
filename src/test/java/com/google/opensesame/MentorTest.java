package com.google.opensesame;

import com.google.opensesame.servlets.MentorBuilder;
import com.google.opensesame.servlets.MentorObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MentorTest {
  private static final String MENTOR_NAME = "Mentor";
  private static final String MENTOR_GITHUB_ID = "Obinnabii";
  private static final String PROJECT_ID = "Project_ID";

  @Test
  public void addProjects() throws IOException {
    MentorObject mentor =
        new MentorBuilder().name(MENTOR_NAME).gitHubID(MENTOR_GITHUB_ID).buildMentor();
    mentor.addProject(PROJECT_ID);

    List<String> expected = new ArrayList<String>();
    expected.add(PROJECT_ID);

    List<String> actual = mentor.getProjectIDs();

    Assert.assertEquals(expected, actual);
  }
}
