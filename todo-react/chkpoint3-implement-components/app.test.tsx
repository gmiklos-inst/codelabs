import React from 'react'
import { render, cleanup, getByTestId } from 'react-testing-library'
import { App } from './App'

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