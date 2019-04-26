import React from 'react'
import { render } from 'react-testing-library'
import { App } from './App'

describe('App', () => {
  it('renders the greeting', () => {
    const { queryByText, getByText, findAllByText } = render(<App />);
    getByText('Hi!');
  });
});