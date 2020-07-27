import {
  AuthButton,
  USER_SIGNUP_URL,
} from '../common/react/components/AuthButton.js';
import '@testing-library/jest-dom/extend-expect';
import {render, screen} from '@testing-library/react';

/**
 * Mock auth data representing when a user is authorized and has created a
 * profile.
 */
const mockAuthData1 = {
  authorized: true,
  hasProfile: true,
  logoutUrl: '/logout',
  loginUrl: '/login',
};

/**
 * Mock auth data representing when a user is authorized but has not yet created
 * a profile.
 */
const mockAuthData2 = {
  authorized: true,
  hasProfile: false,
  logoutUrl: '/logout',
  loginUrl: '/login',
};

/**
 * Mock auth data representing when a user is not authorized.
 *
 * In this case, hasProfile can only be false because there is no identity
 * associated with the user to check for an existing profile.
 */
const mockAuthData3 = {
  authorized: false,
  hasProfile: false,
  logoutUrl: '/logout',
  loginUrl: '/login',
};

describe('Auth button', () => {
  it('is added to the DOM', () => {
    const elem =
        render(<AuthButton loading={false} authData={mockAuthData1} />);
    
    expect(elem.container.firstChild).not.toBeNull();
  });

  it('handles if authorization data is loading', () => {
    render(<AuthButton loading={true} />);

    expect(screen.getByText('Log In')).not.toBeNull();
    expect(screen.getByText('Log In')).toHaveClass('disabled');
  });

  it('handles if user is authorized and has a profile', () => {
    render(<AuthButton loading={false} authData={mockAuthData1} />);

    expect(screen.getByText('Log Out')).not.toBeNull();
    expect(screen.getByText('Log Out'))
        .toHaveAttribute('href', mockAuthData1.logoutUrl);
    expect(screen.getByText('Log Out')).not.toHaveClass('disabled');
  });

  it('handles if user is authorized and has no profile', () => {
    render(<AuthButton loading={false} authData={mockAuthData2} />);

    expect(screen.getByText('Create Profile')).not.toBeNull();
    expect(screen.getByText('Create Profile'))
        .toHaveAttribute('href', USER_SIGNUP_URL);
    expect(screen.getByText('Create Profile')).not.toHaveClass('disabled');
  });

  it('handles if user is not authorized', () => {
    render(<AuthButton loading={false} authData={mockAuthData3} />);

    expect(screen.getByText('Log In')).not.toBeNull();
    expect(screen.getByText('Log In'))
        .toHaveAttribute('href', mockAuthData3.loginUrl);
    expect(screen.getByText('Log In')).not.toHaveClass('disabled');
  });
});
