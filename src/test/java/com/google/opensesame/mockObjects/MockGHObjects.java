package com.google.opensesame.mockObjects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;

public class MockGHObjects {

  // #### MOCK GHUser ####

  /** The name of the mocked GHUser returned by setupMockUser() */
  public String MOCK_GHUSER_NAME = "MOCK_NAME";
  /** The bio of the mocked GHUser returned by setupMockUser() */
  public String MOCK_BIO = "MOCK_BIO";
   /** The Avatar URL of the mocked GHUser returned by setupMockUser() */
  public String MOCK_AVATAR_URL = "MOCK_AVATAR_URL";
   /** The bio of the mocked GHUser returned by setupMockUser() */
  public String MOCK_LOCTAION = "MOCK_LOCTAION";
  /** The HTML URL of the mocked GHUser returned by setupMockUser() */
  public String MOCK_HTML_URL = "MOCK_HTML_URL";
  
  /**
   * Creates an instance of a GHUser mocked with Mockito. The mocked user comes
   * with the following functions predefined;
   * getBio() -> MOCK_BIO
   * getAvatarUrl() -> MOCK_AVATAR_URL
   * getLocation() -> MOCK_LOCATION
   * getName() -> MOCK_NAME
   * getHtmlUrl().toString() -> MOCK_HTML_URL;
   * 
   * NOTE: all functions return fixtures defined in this class
   * @return a mocked instance of a GHUser
   * @throws IOException
   */
  public GHUser setupMockUser() throws IOException {
    // Create A GHUser Stub
  GHUser mockGHUser = mock(GHUser.class);
  when(mockGHUser.getAvatarUrl()).thenReturn(MOCK_AVATAR_URL);
  when(mockGHUser.getBio()).thenReturn(MOCK_BIO);
  when(mockGHUser.getHtmlUrl().toString()).thenReturn(MOCK_HTML_URL);
  when(mockGHUser.getLocation()).thenReturn(MOCK_LOCTAION);
  when(mockGHUser.getName()).thenReturn(MOCK_GHUSER_NAME);
  return mockGHUser;
  } 

  // #### MOCK GHRepository ####

  /** The Language of the mocked GHRepository returned by setupMockRepository() */
  public String MOCK_LANGUAGE = "MOCK_LANGUAGE";
   /** The name of the mocked GHRepository returned by setupMockRepository() */
   public String MOCK_GHREPO_NAME = "MOCK_REPO_NAME";
   /** The description of the mocked GHRepository returned by setupMockRepository() */
   public String MOCK_DESCRIPTION = "MOCK_DESCRIPTION";
  /** 
   * The topics of the mocked GHRepository returned by setupMockRepository().
   * ["TOPIC 1","TOPIC 2"] represented as a List. 
   */
  public List<String> MOCK_TOPICS = createMockTopics();
  
   /**
   * A list of fake topics for mocking purposes.
   * @return ["TOPIC 1","TOPIC 2"] represented as a List.
   */
  private List<String> createMockTopics() {
    List<String> MOCK_TOPICS = new ArrayList<String>();
    MOCK_TOPICS.add("TOPIC 1");
    MOCK_TOPICS.add("TOPIC 2");
    return MOCK_TOPICS;
  }

    /**
   * Creates an instance of a GHRepository mocked with Mockito. The mocked repository
   * comes with the following functions predefined;
   * getDescription() -> MOCK_DESCRIPTION
   * getLanguage() -> MOCK_LANGUAGE
   * getName() -> MOCK_REPO_NAME
   * listTopics() -> MOCK_TOPICS
   * 
   * NOTE: all functions return fixtures defined in this class
   * @return a mocked instance of a GHUser
   * @throws IOException
   */
  public GHRepository setupMockRepository() throws IOException {
    GHRepository mockGHRepository = mock(GHRepository.class);
    when(mockGHRepository.getDescription()).thenReturn(MOCK_DESCRIPTION);
    when(mockGHRepository.getLanguage()).thenReturn(MOCK_LANGUAGE);
    when(mockGHRepository.getName()).thenReturn(MOCK_GHREPO_NAME);
    when(mockGHRepository.listTopics()).thenReturn(MOCK_TOPICS);
    return mockGHRepository;
  }
  
}