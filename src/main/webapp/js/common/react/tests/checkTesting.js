/**
 * Checks if the code is currently being run in a NodeJS environment, in which
 * case the React modules should be imported.
 *
 * The reason that this needs to be called from every React component file
 * boils down to the two environments where these React components will be run:
 * the browser, and NodeJS. When running in the browser, scripts like React
 * and PropTypes are imported via the script tags in each HTML file. 
 * Running in the browser is the intended use-case for
 * these components. When running in NodeJS, they are being tested.
 * Because they are not being loaded as part of an HTML file, the
 * scripts like React and PropTypes are not being loaded with script tags, 
 * and must instead be loaded via NodeJS dependencies as is happening below.
 *
 * An explored alternative to this solution is a JavaScript bundler like 
 * webpack https://webpack.js.org/ or browserify http://browserify.org/, which
 * allows modules to be imported from a browser context in the same way that 
 * they are imported below, instead of via script tags in the HTML document.
 */
export default function checkTesting() {
  if ((typeof process !== 'undefined') && (process.release.name === 'node')) {
    global.PropTypes = require('prop-types');
    global.React = require('react');
  }
}
