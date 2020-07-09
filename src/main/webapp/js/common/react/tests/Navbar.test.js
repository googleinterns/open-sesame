import React from 'react';
import Navbar from '../components/Navbar.js';
import '@testing-library/jest-dom/extend-expect';
import {render, screen} from '@testing-library/react';
import fetchMock from 'fetch-mock-jest';
import {makeRelativeUrlAbsolute} from '../../../fetch_handler.js';

const navbarUrls = [
  {
    href: '/',
    name: 'Home',
  },
  {
    href: '/projects.html',
    name: 'Projects',
  },
];

beforeAll(() => {
  const authData = {
    authorized: false,
    loginUrl: '/login',
    logoutUrl: '/logout',
    hasProfile: false,
  };

  fetchMock.get(makeRelativeUrlAbsolute('/auth'), {
    body: JSON.stringify(authData),
    status: 200,
    headers: {
      'Content-Type': 'application/json',
    },
  });
});

afterAll(() => {
  fetchMock.restore();
});

describe('Navbar component', () => {
  it('is added to the DOM', () => {
    const elem = render(<Navbar urls={navbarUrls} />);
    expect(elem.container.firstChild).not.toBeNull();
  });

  it('displays the names of the urls', () => {
    render(<Navbar urls={navbarUrls} />);

    expect(screen.queryByText('Home')).not.toBeNull();
    expect(screen.queryByText('Projects')).not.toBeNull();
  });

  it('contains both urls', () => {
    render(<Navbar urls={navbarUrls} />);

    expect(screen.getByText('Home')).toHaveAttribute('href', '/');
    expect(screen.getByText('Projects'))
        .toHaveAttribute('href', '/projects.html');
  });
});
