/**
 * @fileoverview Formats the errors from a fetch made to the Open Sesame API.
 * There are three different types of errors that are formatted here: a network
 * error from the Fetch API, a server error from the Open Sesame API, and a
 * defined API error from the Open Sesame API.
 *
 * All error responses from the Open Sesame API should contain a JSON body
 * formatted to match the ErrorResponse class.
 */
class ErrorResponse extends Error {
  /**
   * Creates a JS error with additional information for frontend use.
   * @param {string} message
   * @param {number} [statusCode]
   * @param {string} userMessage 
   */
  constructor(message, statusCode, userMessage) {
    super(message);
    /**
     * The name of the error, which is error and the status code if one is
     * available. For example: 'Error 404'.
     * @type {string}
     */
    this.name = statusCode ? `Error ${statusCode}` : 'Error';
    /**
     * The HTTP status code of the error. Reference here for more information:
     * https://www.restapitutorial.com/httpstatuscodes.html
     * @type {number}
     */
    this.statusCode = statusCode;
    /**
     * A user-friendly message describing what went wrong.
     * @type {string}
     */
    this.userMessage = userMessage;
  }
}
/**
 * Standardizes the errors of a fetch request made to the Open Sesame backend
 * for easier error handling.
 * @param {Promise} fetchRequest A fetch request to the Open Sesame backend.
 * @param {string} fetchFailedUserMessage The message to display to the user
 *    when the fetch itself has failed due to a network error.
 * @param {string} genericServerErrorUserMessage The generic message to display
 *    to the user when a server error has been encountered.
 * @return {Promise} Returns a promise that formats the errors from a fetch
 *    request.
 */
function standardizeFetchErrors(
    fetchRequest, fetchFailedUserMessage, genericServerErrorUserMessage) {
  return fetchRequest.catch((fetchError) => {
    // Fetch encountered an error while making the request (a network
    // error).
    return Promise.reject(
        new ErrorResponse(fetchError.message, null, fetchFailedUserMessage));
  }).then((response) => {
    if (!response.ok) {
      // Fetch received a response from the server but the response contained
      // an error code.
      return formatAPIErrorResponse(response, genericServerErrorUserMessage);
    } else {
      return response;
    }
  });
}

/**
 * Makes a relative URL (like one to an Open Sesame endpoint) absolute. This
 * needs to be done for all fetch requests that are going to be tested with
 * NodeJS, as relative URLs are not supported on NodeJS.
 * @param {string} relativeUrl
 * @return {URL} Returns the absolute URL.
 */
function makeRelativeUrlAbsolute(relativeUrl) {
  return new URL(relativeUrl, window.location.origin);
}

/**
 * Logs the error to the console and alerts the user through a browser alert if
 * there is a user message available.
 * @param {Error} error
 */
function basicErrorHandling(error) {
  console.error(error);
  if (typeof(error.userMessage) !== 'undefined') {
    alert(error.userMessage);
  }
}

/**
 * Formats an API error response to match the ErrorResponse convention.
 * @param {Response} errorResponse
 * @param {string} genericServerErrorUserMessage
 * @return {Promise} Returns a promise that rejects with a properly-formatted
 *    ErrorResponse error.
 */
function formatAPIErrorResponse(errorResponse, genericServerErrorUserMessage) {
  if (errorResponse.headers.get('Content-Type').startsWith('text/html')) {
    // HTML-based error responses are received from the servlet-based errors
    // of the Open Sesame API. For example, the servlet has encountered an
    // internal error and could not continue with the request
    // or a 404 error if the servlet could not find a requested endpoint.
    return errorResponse.text().then((errorText) => {
      return Promise.reject(new ErrorResponse(
        extractErrorTextFromHTML(errorText),
        errorResponse.status,
        genericServerErrorUserMessage
      ));
    });
  } else if (errorResponse.headers.get('Content-Type')
      .startsWith('application/json')) {
    // JSON-based error responses are received from the defined behavior
    // for the Open Sesame API. These responses follow the same format as
    // the ErrorResponse type.
    return errorResponse.json().then((errorJson) => {
      return Promise.reject(new ErrorResponse(
        errorJson.message,
        errorJson.statusCode,
        errorJson.userMessage
      ));
    });
  } else {
    return Promise.reject(new ErrorResponse(
      'Error could not be parsed; unanticipated content type.',
      errorResponse.status,
      genericServerErrorUserMessage
    ));
  }
}

/**
 * Extracts the error text from the HTML error page.
 * @param {string} htmlString
 * @return {string} Returns the error text.
 */
function extractErrorTextFromHTML(htmlString) {
  const responseDOM = new DOMParser().parseFromString(htmlString, 'text/html');
  const body = responseDOM.getElementsByTagName('BODY')[0];

  return Array.from(body.childNodes)
      .map((child) => child.textContent).join(' ');
}

export {
  ErrorResponse,
  standardizeFetchErrors,
  makeRelativeUrlAbsolute,
  basicErrorHandling,
};
