import React from 'react';
import NavbarLink from '../components/NavbarLink.js';
import '@testing-library/jest-dom/extend-expect';
import {render, screen} from '@testing-library/react';

describe('NavbarLink component', () => {
  it('is added to the DOM', () => {
    const elem = render(<NavbarLink href="/home.html" name="Home" />);
    expect(elem.container.firstChild).not.toBeNull();
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
