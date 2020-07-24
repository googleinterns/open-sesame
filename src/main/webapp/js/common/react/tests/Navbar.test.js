import React from 'react';
import {Navbar} from '../components/Navbar.js';
import '@testing-library/jest-dom/extend-expect';
import {render, screen} from '@testing-library/react';

const navbarUrls = [
  {
    href: '/',
    name: 'Home',
    requiresAuth: false,
  },
  {
    href: '/projects.html',
    name: 'Projects',
    requiresAuth: false,
  },
  {
    href: '/mentors.html',
    name: 'Mentors',
    requiresAuth: false,
  },
  {
    href: '/dashboard.html',
    name: 'Dashboard',
    requiresAuth: true,
  },
];

const defaultMockAuthData = {
  authorized: true,
  hasProfile: true,
};

describe('Navbar component', () => {
  it('is added to the DOM', () => {
    const elem = render(
        <Navbar urls={navbarUrls} loading={true} />);
    expect(elem.container.firstChild).not.toBeNull();
  });

  it('displays the names of the urls', () => {
    render(
        <Navbar
          urls={navbarUrls}
          loading={false}
          authData={defaultMockAuthData} />);

    expect(screen.queryByText('Home')).not.toBeNull();
    expect(screen.queryByText('Projects')).not.toBeNull();
    expect(screen.queryByText('Mentors')).not.toBeNull();
    expect(screen.queryByText('Dashboard')).not.toBeNull();
  });

  it('contains the urls', () => {
    render(
        <Navbar
          urls={navbarUrls}
          loading={false}
          authData={defaultMockAuthData} />);

    expect(screen.getByText('Home')).toHaveAttribute('href', '/');
    expect(screen.getByText('Projects'))
        .toHaveAttribute('href', '/projects.html');
    expect(screen.getByText('Mentors'))
        .toHaveAttribute('href', '/mentors.html');
    expect(screen.getByText('Dashboard'))
        .toHaveAttribute('href', '/dashboard.html');
  });

  it('does not display a restricted link to an unauthenticated user', () => {
    const authData = {
      authorized: false,
      hasProfile: false,
    };

    render(
        <Navbar urls={navbarUrls} loading={false} authData={authData} />);

    expect(screen.queryByText('Dashboard')).toBeNull();
  });

  it('does not display a restricted link to a user without a profile', () => {
    const authData = {
      authorized: true,
      hasProfile: false,
    };

    render(
        <Navbar urls={navbarUrls} loading={false} authData={authData} />);

    expect(screen.queryByText('Dashboard')).toBeNull();
  });

  it('does not display the restricted link while loading auth data', () => {
    render(<Navbar urls={navbarUrls} loading={true} />);

    expect(screen.queryByText('Dashboard')).toBeNull();
  });
});
