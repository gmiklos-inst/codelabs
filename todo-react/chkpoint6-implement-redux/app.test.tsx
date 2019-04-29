import React from 'react'
import {createStore} from 'redux'
import {Provider} from 'react-redux'
import { render as rtlRender, cleanup } from 'react-testing-library'
import { App } from './App'
import { appReducer, initialState } from './reducers';

function render(
  ui,
  {store = createStore(appReducer, initialState)} = {},
) {
  return {
    ...rtlRender(<Provider store={store}>{ui}</Provider>),
    store,
  }
}

describe('App', () => {
  
  afterEach(cleanup);

  it('renders the greeting', () => {
    const { getByText } = render(<App />);
    getByText('Hi!');
  });

  it('renders the input', () => {
    const { getByTestId } = render(<App />);
    getByTestId('TodoInput');
  });
  
  it('renders the list', async () => {
    const { getByTestId } = render(<App />);
    getByTestId('TodoList');
  });

  it('renders the status', async () => {
    const { getByTestId } = render(<App />);
    getByTestId('TodoStatus');
  });

  it('renders the filters', async () => {
    const { getByTestId } = render(<App />);
    getByTestId('TodoFilter');
  });

});