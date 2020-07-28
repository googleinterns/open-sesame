package com.google.opensesame.mockObjects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.kohsuke.github.GHMyself;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

/**
 * MockGHObjects is a helper class with two main static functions to help mock the kohsuke GitHub
 * API during testing; createMockUser() - Creates a mock GHUser instance. createMockRepository() -
 * Creates a mock GHRepository instance.
 */
public class MockGHObjects {

  // #### MOCK GHUser ####

  /** The name of the mocked GHUser returned by createMockUser() */
  public static String MOCK_GHUSER_NAME = "MOCK_NAME";
  /** The bio of the mocked GHUser returned by createMockUser() */
  public static String MOCK_BIO = "MOCK_BIO";
  /** The Avatar URL of the mocked GHUser returned by createMockUser() */
  public static String MOCK_AVATAR_URL = "MOCK_AVATAR_URL";
  /** The location of the mocked GHUser returned by createMockUser() */
  public static String MOCK_LOCATION = "MOCK_LOCATION";

  /**
   * The HTML URL of the mocked GHUser returned by createMockUser()
   *
   * @throws MalformedURLException
   */
  public static URL MOCK_HTML_URL() throws MalformedURLException {
    return new URL("http://localhost/");
  }

  /**
   * Creates an instance of a GHUser mocked with Mockito. The mocked user comes with the following
   * functions predefined;
   * <li>getBio() -> MOCK_BIO
   * <li>getAvatarUrl() -> MOCK_AVATAR_URL
   * <li>getLocation() -> MOCK_LOCATION
   * <li>getName() -> MOCK_NAME
   * <li>getHtmlUrl().toString() -> MOCK_HTML_URL();
   * <li>getLogin() -> MOCK_LOGIN
   *
   *     <p>NOTE: all functions return fixtures defined in this class. MOCK_HTML_URL() is a function
   *     due to complications with Java's URL Class.
   *
   * @return a mocked instance of a GHUser
   * @throws IOException
   */
  public static GHUser createMockUser() throws IOException {
    // Create A GHUser Stub
    GHUser mockGHUser = mock(GHUser.class);
    when(mockGHUser.getAvatarUrl()).thenReturn(MOCK_AVATAR_URL);
    when(mockGHUser.getBio()).thenReturn(MOCK_BIO);
    when(mockGHUser.getHtmlUrl()).thenReturn(MOCK_HTML_URL());
    when(mockGHUser.getLocation()).thenReturn(MOCK_LOCATION);
    when(mockGHUser.getName()).thenReturn(MOCK_GHUSER_NAME);
    when(mockGHUser.getLogin()).thenReturn(MOCK_LOGIN);
    return mockGHUser;
  }

  // #### MOCK GHRepository ####

  /** The Language of the mocked GHRepository returned by createMockRepository() */
  public static String MOCK_LANGUAGE = "MOCK_LANGUAGE";
  /** The name of the mocked GHRepository returned by createMockRepository() */
  public static String MOCK_GHREPO_NAME = "MOCK_REPO_NAME";
  /** The description of the mocked GHRepository returned by createMockRepository() */
  public static String MOCK_DESCRIPTION = "MOCK_DESCRIPTION";
  /** The Id of the mocked GHRepository returned by createMockRepository() */
  public static Long MOCK_ID = 123456L;
  /**
   * The topics of the mocked GHRepository returned by createMockRepository(). ["TOPIC 1","TOPIC 2"]
   * represented as a List.
   */
  public static List<String> MOCK_TOPICS = createMockTopics();

  /**
   * A list of fake topics for mocking purposes.
   *
   * @return ["TOPIC 1","TOPIC 2"] represented as a List.
   */
  private static List<String> createMockTopics() {
    List<String> mockTopics = new ArrayList<String>();
    mockTopics.add("TOPIC 1");
    mockTopics.add("TOPIC 2");
    return mockTopics;
  }

  /**
   * Creates an instance of a GHRepository mocked with Mockito. The mocked repository comes with the
   * following functions predefined;
   * <li>getDescription() -> MOCK_DESCRIPTION
   * <li>getId() -> MOCK_ID
   * <li>getLanguage() -> MOCK_LANGUAGE
   * <li>getName() -> MOCK_REPO_NAME
   * <li>listTopics() -> MOCK_TOPICS
   *
   *     <p>NOTE: all functions return fixtures defined in this class
   *
   * @return a mocked instance of a GHRepository
   * @throws IOException
   */
  public static GHRepository createMockRepository() throws IOException {
    GHRepository mockGHRepository = mock(GHRepository.class);
    when(mockGHRepository.getDescription()).thenReturn(MOCK_DESCRIPTION);
    when(mockGHRepository.getLanguage()).thenReturn(MOCK_LANGUAGE);
    when(mockGHRepository.getName()).thenReturn(MOCK_GHREPO_NAME);
    when(mockGHRepository.listTopics()).thenReturn(MOCK_TOPICS);
    when(mockGHRepository.getId()).thenReturn(MOCK_ID);
    return mockGHRepository;
  }

  // #### MOCK GHMyself ####

  /** The login of the mocked GHMyself object returned by createMockMyself() */
  public static String MOCK_LOGIN = "MOCK_LOGIN";

  /**
   * Creates an instance of a GHMyself mocked with Mockito. The mocked object comes with the
   * following functions predefined;
   * <li>getLogin() -> MOCK_LOGIN
   *
   *     <p>NOTE: MOCK_LOGIN is a fixture in this class
   *
   * @return a mocked instance of a GHMyself Object
   * @throws IOException
   */
  public static GHMyself createMockMyself() throws IOException {
    // Create A GHUser Stub
    GHMyself mockGHMyself = mock(GHMyself.class);
    when(mockGHMyself.getLogin()).thenReturn(MOCK_LOGIN);
    return mockGHMyself;
  }

  // #### MOCK GitHub API ####

  /**
   * Creates an instance of a GitHub mocked with Mockito. The mocked object comes with the following
   * functions predefined;
   * <li>getMyself() -> GHMyself returned by {@link #createMockMyself() createMockMyself}
   * <li>getUser(String anyString) -> GHUser returned by {@link #createMockUser() createMockUser}
   * <li>getRepositoryById(String anyString) -> GHRepository returned by {@link
   *     #createMockRepository() createMockRepository}
   *
   *     <p>NOTE: all functions return fixtures defined in this class
   *
   * @return a mocked instance of a GitHub
   * @throws IOException
   */
  public static GitHub createMockGitHub() throws IOException {
    GitHub mockGitHub = mock(GitHub.class);
    when(mockGitHub.getMyself()).thenReturn(createMockMyself());
    when(mockGitHub.getUser(MOCK_LOGIN)).thenReturn(createMockUser());
    when(mockGitHub.getRepositoryById(MOCK_ID.toString())).thenReturn(createMockRepository());
    return mockGitHub;
  }
}
