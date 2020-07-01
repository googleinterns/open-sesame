package com.google.opensesame.servlets;

import java.io.IOException;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PersonTests {
  private static final String MENTOR_1_NAME = "Mentor 1";
  private static final String MENTOR_1_GTHUB = "Mentor_1_GitHub_ID";
  private static final String PROJECT_1 = "Project 1";

  @Test
  public void addProjects() {
    MentorObject mentor1 = new MentorBuilder()
        .name(MENTOR_1_NAME)
        .gitHubID(MENTOR_1_GITHUB)
        .buildMentor();
    mentor1.addProject(PROJECT_1);
    
    List<String> expected = new List<String>;
    expected.add(PROJECT_1);

    List<String> actual = mentor1.getProjectIDs();

    Assert.assertEquals(expected, actual);
  }
}