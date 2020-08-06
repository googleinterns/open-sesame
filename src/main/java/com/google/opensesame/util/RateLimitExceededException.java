package com.google.opensesame.util;

import java.io.IOException;

/**
 * The exception to be thrown when the GitHub API has run out of calls.
 *
 * <p>NOTE: When this Exception is caught, the appropriate error code to throw is an
 * HTML_FORBIDDEN (403). This is to stay consistent with the GitHub Restful API convention found <a
 * href="https://developer.github.com/v3/#rate-limiting">here</a>
 */
public class RateLimitExceededException extends IOException {

  /** The default error message to be sent when OpenSesame runs out of API calls. */
  private static String defaultErrorMessage =
      "Open Sesame has exceeded its available GitHub API calls.";

  public RateLimitExceededException() {
    super(defaultErrorMessage);
  }
}
