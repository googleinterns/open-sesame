import {DataFetcher} from './components/DataFetcher.js';
import {AuthButton} from './components/AuthButton.js';
import {authDataFetch} from '../../auth_check.js';

/**
 * Renders the auth button using the state of the data fetcher.
 * @param {DataFetcherState} dataFetcher
 * @return {React.Component} Returns the React component.
 */
function renderButton(dataFetcher) {
  return (
    <AuthButton
      loading={dataFetcher.isFetching}
      className=
        'btn btn-emphasis mt-5 mb-5 text-center btn-lg btn-block btn-main'
      authData={dataFetcher.data} />
  );
}

const buttonContainer = document.getElementById('auth-button-container');
ReactDOM.render(
      <DataFetcher
        createFetchRequest={() => authDataFetch}
        render={renderButton} />,
      buttonContainer);

