import React from 'react'
import { render } from 'react-testing-library'
import { App } from './App'

describe('App', () => {
  it('renders the greeting', () => {
    const { getByText } = render(<App />);
    getByText('Hi!');
  });
});