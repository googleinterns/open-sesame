// Global to mimic the global variables available within a browser environment.
// For example, React and ReactDOM, which are normally loaded from the script
// tags in the HTML.
import React from 'react';
import NavbarLink from '../components/NavbarLink.js';
import '@testing-library/jest-dom/extend-expect';
import {render, screen} from '@testing-library/react';

describe('NavbarLink component', () => {
  it('is rendered', () => {
    const elem = render(<NavbarLink href="/home.html" name="Home" />);
    expect(elem.container).not.toBeNull();
  });

  it('displays the name', () => {
    const elem = render(<NavbarLink href="/home.html" name="Home" />);
    expect(screen.queryByText('Home')).not.toBeNull();
  });

  it('contains the intended href', () => {
    const elem = render(<NavbarLink href="/home.html" name="Home" />);
    expect(screen.queryByText('Home')).toHaveAttribute('href', '/home.html');
  });
});
