/**
 * @fileoverview This is a convenience module that implements the default
 * redirect behavior of an authenticated-only page. If a user is not authorized,
 * they are redirected to the home page. If a user is authorized but has not yet
 * completed profile creation, they are redirected to the profile creation page.
 * If a user is authorized and has a profile, they are not redirected.
 */
import {authRedirect} from './auth_check.js';

authRedirect('/', null, '/signup.html');
