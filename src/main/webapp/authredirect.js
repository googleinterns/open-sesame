fetch('/auth').then((response) => {
  if (response.ok) {
    return response.json();
  } else {
    return Promise.reject();
  }
}).then((authData) => {
  if (authData.authorized) {
    if (!authData.hasProfile) {
      window.location.href = '/signup.html';
    } else {
      window.location.href = '/dashboard.html';
    }
  } else {
    window.location.href = '/';
  }
}).catch((errorResponse) => {
  window.location.href = '/';
});
