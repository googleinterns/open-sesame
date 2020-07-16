package com.google.opensesame;

import com.google.opensesame.servlets.MentorObject;
import com.google.opensesame.servlets.PersonBuilder;
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
  private static final Long PROJECT_ID = 1234L;

  @Test
  public void addProjects() throws IOException {
    MentorObject mentor =
        new PersonBuilder().name(MENTOR_NAME).gitHubID(MENTOR_GITHUB_ID).buildMentor();
    mentor.addProject(PROJECT_ID);

    List<Long> expected = new ArrayList<Long>();
    expected.add(PROJECT_ID);

    List<Long> actual = mentor.getProjectIDs();

    Assert.assertEquals(expected, actual);
  }
}
