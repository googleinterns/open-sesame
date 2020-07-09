import standardizeFetchErrors from './fetch_handler.js';
const fetchMock = require('fetch-mock-jest').sandbox();

const fetchFailedMessage = 'Fetch failed';
const genericServerErrorMessage = 'Server failed';

afterEach(() => {
  fetchMock.restore();
});

describe('Fetch error handler', () => {
  it('correctly formats fetch errors', () => {
    fetchMock.get('https://localhost/test', {
      throws: 'Test error',
    });

    const formattedRequest = standardizeFetchErrors(
      fetchMock('https://localhost/test'), 
      fetchFailedMessage, 
      genericServerErrorMessage);

    return formattedRequest.then((res) => {
      fail('Did not catch the error.');
    }).catch((errorResponse) => {
      expect(errorResponse.error).toEqual('Test error');
      expect(errorResponse.userMessage).toEqual(fetchFailedMessage);
      expect(errorResponse.statusCode).toBeUndefined();
    });
  });

  it('correctly formats servlet errors', () => {
    fetchMock.get('https://localhost/test', {
      body: "<html><body><h1>Error message</h1></body></html>",
      status: 500,
      headers: {
        'Content-Type': 'text/html',
      },
    });

    const formattedRequest = standardizeFetchErrors(
      fetchMock('https://localhost/test'), 
      fetchFailedMessage, 
      genericServerErrorMessage);
      
    return formattedRequest.then((res) => {
      fail('Did not catch the error.');
    }).catch((errorResponse) => {
      expect(errorResponse.error).toEqual('Error message');
      expect(errorResponse.userMessage).toEqual(genericServerErrorMessage);
      expect(errorResponse.statusCode).toEqual(500);
    });
  });

  it('correctly formats API errors', () => {
    const apiErrorResponse = {
      error: 'Test API error',
      statusCode: 400,
      userMessage: 'Test user message',
    };
    
    fetchMock.get('https://localhost/test', {
      body: JSON.stringify(apiErrorResponse),
      status: 400,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    const formattedRequest = standardizeFetchErrors(
      fetchMock('https://localhost/test'), 
      fetchFailedMessage, 
      genericServerErrorMessage);

    return formattedRequest.then((res) => {
      fail('Did not catch the error.');
    }).catch((errorResponse) => {
      expect(errorResponse.error).toEqual('Test API error');
      expect(errorResponse.userMessage).toEqual('Test user message');
      expect(errorResponse.statusCode).toEqual(400);
    });
  });

  it('correctly handles an unknown error response content type', () => {
    fetchMock.get('https://localhost/test', {
      status: 500,
      headers: {
        'Content-Type': 'application/xml', // Some unanticipated content type
      },
    });

    const formattedRequest = standardizeFetchErrors(
      fetchMock('https://localhost/test'), 
      fetchFailedMessage, 
      genericServerErrorMessage);
      
    return formattedRequest.then((res) => {
      fail('Did not catch the error.');
    }).catch((errorResponse) => {
      expect(errorResponse.error)
          .toEqual('Error could not be parsed; unanticipated content type.');
      expect(errorResponse.userMessage).toEqual(genericServerErrorMessage);
      expect(errorResponse.statusCode).toEqual(500);
    });
  });

  it('resolves with a valid response if there are no errors', () => {
    const mockApiResponse = {
      message: 'Test response',
    };

    fetchMock.get('https://localhost/test', {
      body: JSON.stringify(mockApiResponse),
      status: 200,
        headers: {
          'Content-Type': 'application/json',
        },
    });

    const formattedRequest = standardizeFetchErrors(
      fetchMock('https://localhost/test'), 
      fetchFailedMessage, 
      genericServerErrorMessage);

    return formattedRequest.then((json) => {
      expect(json.message).toEqual('Test response');
    }).catch((errorResponse) => {
      fail('Caught an error from a successful request.');
    });
  });
});