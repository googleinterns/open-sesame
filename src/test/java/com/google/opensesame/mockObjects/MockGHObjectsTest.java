package com.google.opensesame.mockObjects;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;

@RunWith(JUnit4.class)
public class MockGHObjectsTest {
  @Test
  public void mockUserSetupTests() throws IOException {
    GHUser mockUser = MockGHObjects.setupMockUser();
    assertEquals(mockUser.getBio(), MockGHObjects.MOCK_BIO);
    assertEquals(mockUser.getName(), MockGHObjects.MOCK_GHUSER_NAME);
    assertEquals(mockUser.getAvatarUrl(), MockGHObjects.MOCK_AVATAR_URL);
    assertEquals(mockUser.getLocation(), MockGHObjects.MOCK_LOCTAION);
    assertEquals(mockUser.getHtmlUrl().toString(), 
    MockGHObjects.MOCK_HTML_URL().toString());
  }

  @Test
  public void mockRepositorySetupTests() throws IOException {
    GHRepository mockRepository = MockGHObjects.setupMockRepository();
    assertEquals(mockRepository.getDescription(), MockGHObjects.MOCK_DESCRIPTION);
    assertEquals(mockRepository.getName(), MockGHObjects.MOCK_GHREPO_NAME);
    assertEquals(mockRepository.listTopics(), MockGHObjects.MOCK_TOPICS);
    assertEquals(mockRepository.getLanguage(), MockGHObjects.MOCK_LANGUAGE);
  }
}
