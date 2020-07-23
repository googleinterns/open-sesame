package com.google.opensesame.mockObjects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;

public class MockGHObjects {
  public String MOCK_NAME = "MOCK_NAME";
  public String MOCK_DESCRIPTION = "MOCK_DESCRIPTION";
  public String MOCK_BIO = "MOCK_BIO";
  public String MOCK_AVATAR_URL = "MOCK_AVATAR_URL";
  public String MOCK_LOCTAION = "MOCK_LOCTAION";
  public String MOCK_HTML_URL = "MOCK_HTML_URL";
  public String MOCK_LANGUAGE = "MOCK_LANGUAGE";
  public List<String> MOCK_TOPICS = createMockTopics();


  private List<String> createMockTopics() {
    List<String> MOCK_TOPICS = new ArrayList<String>();
    MOCK_TOPICS.add("TOPIC 1");
    MOCK_TOPICS.add("TOPIC 2");
    return MOCK_TOPICS;
  }
  
  public GHUser setupMockUser() throws IOException {
    // Create A GHUser Stub
  GHUser mockGHUser = mock(GHUser.class);
  when(mockGHUser.getBio()).thenReturn(MOCK_BIO);
  when(mockGHUser.getAvatarUrl()).thenReturn(MOCK_AVATAR_URL);
  when(mockGHUser.getLocation()).thenReturn(MOCK_LOCTAION);
  when(mockGHUser.getName()).thenReturn(MOCK_NAME);
  when(mockGHUser.getHtmlUrl().toString()).thenReturn(MOCK_HTML_URL);
  return mockGHUser;
  } 
  
  public GHRepository setupMockRepository() throws IOException {
    GHRepository mockGHRepository = mock(GHRepository.class);
    when(mockGHRepository.getName()).thenReturn(MOCK_NAME);
    when(mockGHRepository.getDescription()).thenReturn(MOCK_DESCRIPTION);
    when(mockGHRepository.listTopics()).thenReturn(MOCK_TOPICS);
    when(mockGHRepository.getLanguage()).thenReturn(MOCK_LANGUAGE);
    return mockGHRepository;
  }
  
}