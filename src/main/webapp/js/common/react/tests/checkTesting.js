/**
 * Checks if the code is currently being run in a NodeJS environment, in which
 * case the code is being tested and the React modules should be imported.
 */
export default function checkTesting() {
  if ((typeof process !== 'undefined') && (process.release.name === 'node')) {
    global.PropTypes = require('prop-types');
    global.React = require('react');
  }
}