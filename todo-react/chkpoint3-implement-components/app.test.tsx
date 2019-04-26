import React from 'react'
import { render, cleanup } from 'react-testing-library'
import { App } from './App'

describe('App', () => {
  
  afterEach(cleanup);

  it('renders the greeting', () => {
    const { getByText } = render(<App />);
    getByText('Hi!');
  });

  it('renders the input', () => {
    const { getByDisplayValue } = render(<App />);
    getByDisplayValue('TODO item text');
  });
  
  it('renders the list', async () => {
    const { findAllByText } = render(<App />);
    const items = await findAllByText(/item\d/);
    expect(items.map(e => e.innerHTML)).toMatchObject(['item1', 'item2']);
  });

});