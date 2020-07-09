/**
 * @fileoverview Formats the errors from a fetch made to the Open Sesame API.
 * There are three different types of errors that are formatted here: a network
 * error from the Fetch API, a server error from the Open Sesame API, and a
 * defined API error from the Open Sesame API.
 *
 * All error responses from the Open Sesame API should contain a JSON body
 * formatted to match the ErrorResponse object here. {@link ErrorResponse}.
 */
/**
 * @typedef ErrorResponse
 * @property {string} error
 * @property {number} [statusCode]
 * @property {string} userMessage
 */
/**
 * Standardizes the errors of a fetch request made to the Open Sesame backend
 * for easier error handling.
 * {@link ErrorResponse}
 * @param {Promise} fetchRequest A fetch request to the Open Sesame backend.
 * @param {string} fetchFailedUserMessage The message to display to the user
 *    when the fetch itself has failed due to a network error.
 * @param {string} genericServerErrorUserMessage The generic message to display
 *    to the user when a server error has been encountered.
 * @return {Promise} Returns a promise that formats the errors from a fetch
 *    request.
 */
export default function standardizeFetchErrors(
    fetchRequest, fetchFailedUserMessage, genericServerErrorUserMessage) {
  return fetchRequest.catch((fetchError) => {
    // Fetch encountered an error while making the request (a network
    // error).
    /** @type {ErrorResponse} */
    const errorResponse = {
      error: fetchError,
      userMessage: fetchFailedUserMessage,
    };

    return Promise.reject(errorResponse);
  }).then((response) => {
    if (!response.ok) {
      // Fetch received a response from the server but the response contained
      // an error code.
      return formatAPIErrorResponse(response, genericServerErrorUserMessage);
    } else {
      return response.json();
    }
  });
}

/**
 * Formats an API error response to match the ErrorResponse convention.
 * {@link ErrorResponse}
 * @param {Response} errorResponse
 * @param {string} genericServerErrorUserMessage
 * @return {Promise} Returns a promise that rejects with a properly-formatted
 *    ErrorResponse object. {@link ErrorResponse}
 */
function formatAPIErrorResponse(errorResponse, genericServerErrorUserMessage) {
  if (errorResponse.headers.get('Content-Type').startsWith('text/html')) {
    // HTML-based error responses are received from the servlet-based errors
    // of the Open Sesame API. For example, the servlet has encountered an
    // internal error and could not continue with the request
    // or a 404 error if the servlet could not find a requested endpoint.
    return errorResponse.text().then((errorText) => {
      return Promise.reject({
        error: extractErrorTextFromHTML(errorText),
        statusCode: errorResponse.status,
        userMessage: genericServerErrorUserMessage,
      });
    });
  } else if (errorResponse.headers.get('Content-Type')
      .startsWith('application/json')) {
    // JSON-based error responses are received from the defined behavior
    // for the Open Sesame API. These responses follow the same format as
    // the ErrorResponse type.
    return errorResponse.json().then((errorJson) => {
      return Promise.reject(errorJson);
    });
  } else {
    return Promise.reject({
      error: 'Error could not be parsed; unanticipated content type.',
      statusCode: errorResponse.status,
      userMessage: genericServerErrorUserMessage,
    });
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
