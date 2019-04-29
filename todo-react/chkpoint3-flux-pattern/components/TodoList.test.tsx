import React from 'react'
import { render, cleanup, getByText } from 'react-testing-library'
import { TodoList } from './TodoList';

describe('TodoList', () => {
  
  afterEach(cleanup);

  it('renders a header', async () => {
    const props = {todoItems: []};
    const { getByText } = render(<TodoList {...props} />);
    getByText('Check');
    getByText('Item');
    getByText('Action');
  });

  it('renders a row', async () => {
    const props = {todoItems: [{
      id: "1",
      title: "item1",
    }]};
    const { getByTestId } = render(<TodoList {...props} />);
    getByTestId('TodoItemRow');
  });

  it('renders the list', async () => {
    const props = {todoItems: [{
      id: "1",
      title: "item1",
    },
    {
      id: "2",
      title: "item2",
    }]};
    const { findAllByTestId } = render(<TodoList {...props} />);
    const items = await findAllByTestId('TodoItemRow');
    expect(items).toHaveLength(2);
  });

});