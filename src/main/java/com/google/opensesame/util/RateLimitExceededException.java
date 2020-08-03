package com.google.opensesame.util;

import java.io.IOException;

/**
 * The exception to be thrown when the GitHub API has run out of calls. This will help us avoid
 * lengthy stalls when we lose access to GitHub Data i.e. Our API calls have exceeded our current
 * GitHub hourly call rate limit.
 *
 * <p>NOTE: When this Exception is caught, the appropriate error code to throw would be an
 * HTML_FORBIDDEN (403). This is to stay consistent with the GitHub Restful API convention found <a
 * href="https://developer.github.com/v3/#rate-limiting">here</a>
 */
public class RateLimitExceededException extends IOException {

  /** The default error message to be sent when OpenSesame runs out of API calls. */
  private static String defaultErrorMessage =
      "OpenSesame has Exceeded it's " + "available GitHub API calls";

  public RateLimitExceededException() {
    super(defaultErrorMessage);
  }
}
