package com.google.opensesame.servlets;

import java.io.IOException;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PersonTests {
  private static final String MENTOR_NAME = "Mentor";
  private static final String MENTOR_GITHUB_ID = "Mentor_GitHub_ID";
  private static final String PROJECT_ID = "Project_ID";

  @Test
  public void addProjects() {
    MentorObject mentor = new MentorBuilder()
        .name(MENTOR_NAME)
        .gitHubID(MENTOR_GITHUB_ID)
        .buildMentor();
    mentor.addProject(PROJECT_ID);
    
    List<String> expected = new List<String>;
    expected.add(PROJECT_ID);

    List<String> actual = mentor.getProjectIDs();

    Assert.assertEquals(expected, actual);
  }
}