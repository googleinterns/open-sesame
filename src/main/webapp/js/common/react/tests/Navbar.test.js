import React from 'react';
import Navbar from '../components/Navbar.js';
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
];

describe('Navbar component', () => {
  it('is added to the DOM', () => {
    const elem = render(<Navbar urls={navbarUrls} loading={true} />);
    expect(elem.container.firstChild).not.toBeNull();
  });

  it('displays the names of the urls', () => {
    render(<Navbar urls={navbarUrls} loading={true} />);

    expect(screen.queryByText('Home')).not.toBeNull();
    expect(screen.queryByText('Projects')).not.toBeNull();
  });

  it('contains both urls', () => {
    render(<Navbar urls={navbarUrls} loading={true} />);

    expect(screen.getByText('Home')).toHaveAttribute('href', '/');
    expect(screen.getByText('Projects'))
        .toHaveAttribute('href', '/projects.html');
  });
});
