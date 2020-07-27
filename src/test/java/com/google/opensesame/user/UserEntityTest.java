package com.google.opensesame.user;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UserEntityTest {
  @Test
  public void isMentorTest() throws IOException {
    UserEntity mockUser = new UserEntity();
    assertEquals(false, mockUser.isMentor());

    mockUser.addProject("12345L");
    assertEquals(true, mockUser.isMentor());
  }
}