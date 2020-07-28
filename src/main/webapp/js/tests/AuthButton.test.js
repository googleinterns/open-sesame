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
const AUTHORIZED_WITH_PROFILE_AUTH_DATA = {
  authorized: true,
  hasProfile: true,
  logoutUrl: '/logout',
  loginUrl: '/login',
};

/**
 * Mock auth data representing when a user is authorized but has not yet created
 * a profile.
 */
const AUTHORIZED_WITHOUT_PROFILE_AUTH_DATA = {
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
const UNAUTHORIZED_AUTH_DATA = {
  authorized: false,
  hasProfile: false,
  logoutUrl: '/logout',
  loginUrl: '/login',
};

describe('Auth button', () => {
  it('is added to the DOM', () => {
    const elem = render(<AuthButton
      loading={false}
      authData={AUTHORIZED_WITH_PROFILE_AUTH_DATA} />);

    expect(elem.container.firstChild).not.toBeNull();
  });

  it('is disabled if authorization data is loading', () => {
    render(<AuthButton loading={true} />);

    expect(screen.getByText('Log In')).not.toBeNull();
    expect(screen.getByText('Log In')).toHaveClass('disabled');
  });

  it('shows "Log Out" if user is authorized and has a profile', () => {
    render(<AuthButton
      loading={false}
      authData={AUTHORIZED_WITH_PROFILE_AUTH_DATA} />);

    expect(screen.getByText('Log Out')).not.toBeNull();
    expect(screen.getByText('Log Out'))
        .toHaveAttribute('href', AUTHORIZED_WITH_PROFILE_AUTH_DATA.logoutUrl);
    expect(screen.getByText('Log Out')).not.toHaveClass('disabled');
  });

  it('shows "Create Profile" if user is authorized and has no profile', () => {
    render(<AuthButton
      loading={false}
      authData={AUTHORIZED_WITHOUT_PROFILE_AUTH_DATA} />);

    expect(screen.getByText('Create Profile')).not.toBeNull();
    expect(screen.getByText('Create Profile'))
        .toHaveAttribute('href', USER_SIGNUP_URL);
    expect(screen.getByText('Create Profile')).not.toHaveClass('disabled');
  });

  it('shows "Log In" if user is not authorized', () => {
    render(<AuthButton loading={false} authData={UNAUTHORIZED_AUTH_DATA} />);

    expect(screen.getByText('Log In')).not.toBeNull();
    expect(screen.getByText('Log In'))
        .toHaveAttribute('href', UNAUTHORIZED_AUTH_DATA.loginUrl);
    expect(screen.getByText('Log In')).not.toHaveClass('disabled');
  });
});
