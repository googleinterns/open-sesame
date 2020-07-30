package com.google.opensesame.util;

public class ServletValidationException extends Exception {
  private final String userMessage;
  private final int statusCode;

  public ServletValidationException(String message, String userMessage, int statusCode) {
    super(message);

    this.userMessage = userMessage;
    this.statusCode = statusCode;
  }

  public String getUserMessage() {
    return userMessage;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
