import React from 'react'
import { render, cleanup, getByTestId } from 'react-testing-library'
import { TodoInput } from './TodoInput';

describe('TodoInput', () => {
  
  afterEach(cleanup);

  it('renders the input', () => {
    const { getByDisplayValue } = render(<TodoInput value="TODO item text" />);
    getByDisplayValue('TODO item text');
  });
});