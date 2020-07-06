/**
 * Get the user @param user from the user servlet.
 * @param {String} user
 * @return a user object
 */
function getUser(user) {
  console.log('entering get user function/n');
  const params = new URLSearchParams();
  params.append('user', user);

  fetch('/user', { method: 'GET', body: params })
    .then(errorHandling).then((response) => response.json())
    .then((user) => {
      return user;
    })
    .catch((error) => {
      console.log(error);
    });
}

/**
 * Basic error handling checks if fetch results are 'ok.'
 * @param response the HTTP response
 * @return an 'ok' HTTP response
 */
function errorHandling(response) {
  if (!response.ok) {
    throw Error(response.statusText);
  }
  return response;
}
