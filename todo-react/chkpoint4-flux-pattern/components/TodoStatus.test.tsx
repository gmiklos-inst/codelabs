
import React from 'react'
import { render, cleanup, getByTestId } from 'react-testing-library'
import { TodoStatus } from './TodoStatus';

describe('TodoStatus', () => {
  
  afterEach(cleanup);

  it('renders the status text', () => {
    const props = {itemLeftCount: 42};
    const { getByText } = render(<TodoStatus {...props} />);
    getByText('42 items left');
  });

  it('renders the status text with 0', () => {
    const props = {itemLeftCount: 0};
    const { getByText } = render(<TodoStatus {...props} />);
    getByText('no items left');
  });

  it('renders the singular status text', () => {
    const props = {itemLeftCount: 1};
    const { getByText } = render(<TodoStatus {...props} />);
    getByText('1 item left');
  });
});