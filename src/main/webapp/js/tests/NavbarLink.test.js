import React from 'react';
import {NavbarLink} from '../common/react/components/NavbarLink.js';
import '@testing-library/jest-dom/extend-expect';
import {render, screen} from '@testing-library/react';

const mockUrl = {
  href: '/home.html',
  name: 'Home',
  requiresAuth: false,
};

describe('NavbarLink component', () => {
  it('is added to the DOM', () => {
    const elem = render(<NavbarLink url={mockUrl} />);
    expect(elem.container.firstChild).not.toBeNull();
  });

  it('displays the name', () => {
    render(<NavbarLink url={mockUrl} />);
    expect(screen.queryByText('Home')).not.toBeNull();
  });

  it('contains the intended href', () => {
    render(<NavbarLink url={mockUrl} />);
    expect(screen.queryByText('Home')).toHaveAttribute('href', '/home.html');
  });
});
