package com.google.opensesame.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public class ErrorResponse {
  private static Gson gson = new Gson();

  /**
   * Sends an error response with a JSON body that follows the established Open Sesame API
   * convention for error responses. See fetch_handler.js for more information about how errors are
   * intended to be parsed by the recipient. The convention used here is intended to follow RESTful
   * API standards: https://blog.restcase.com/rest-api-error-codes-101/
   * @param response The place to send the response.
   * @param validationException A servlet validation exception to get the error information for the
   *     servlet response.
   * @throws IOException
   */
  public static void sendJsonError(
      HttpServletResponse response,
      ServletValidationException validationException) throws IOException {
    sendJsonError(
        response,
        validationException.getMessage(),
        validationException.getStatusCode(),
        validationException.getUserMessage());
  }
  
  /**
   * Sends an error response with a JSON body that follows the established Open Sesame API
   * convention for error responses. See fetch_handler.js for more information about how errors are
   * intended to be parsed by the recipient. The convention used here is intended to follow RESTful
   * API standards: https://blog.restcase.com/rest-api-error-codes-101/
   *
   * @param response The place to send the response.
   * @param errorMessage A debug-focused error message describing what went wrong.
   * @param statusCode The HTTP status code that best describes the error encountered. For more
   *     information about HTTP status codes: https://www.restapitutorial.com/httpstatuscodes.html
   *     Servlets also have built-in constants that make your status codes more readable:
   *     https://tomcat.apache.org/tomcat-5.5-doc/servletapi/javax/servlet/http/HttpServletResponse.html
   * @param userMessage A user-friendly message describing what went wrong.
   * @throws IOException
   */
  public static void sendJsonError(
      HttpServletResponse response, String errorMessage, int statusCode, String userMessage)
      throws IOException {
    JsonObject responseObject = new JsonObject();
    responseObject.addProperty("message", errorMessage);
    responseObject.addProperty("statusCode", statusCode);
    responseObject.addProperty("userMessage", userMessage);

    String responseJson = gson.toJson(responseObject);
    response.setStatus(statusCode);
    response.setContentType("application/json;");
    response.getWriter().println(responseJson);
  }
}
