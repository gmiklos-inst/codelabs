import React from 'react'
import { render, cleanup, getByText } from 'react-testing-library'
import { TodoItemRow } from './TodoItemRow';
import 'jest-dom/extend-expect';

describe('TodoItemRow', () => {
  
  afterEach(cleanup);

  it('renders the title', async () => {
    const props = {
      item: {
        title: 'item', 
        id: 'id',
      }
    };
    const { getByText } = render(<table><tbody><TodoItemRow {...props} /></tbody></table>);
    getByText(props.item.title);
  });

  it('renders with an unchecked toggle', async () => {
    const props = {
      item: {
        title: 'item', 
        id: 'id',
      }
    };
    const { container } = render(<table><tbody><TodoItemRow {...props} /></tbody></table>);
    expect(container.querySelector('input[type="checkbox"]')).not.toHaveAttribute('checked');
  });

  it('renders with a checked toggle', async () => {
    const props = {
      item: {
        title: 'item', 
        id: 'id',
        completed: true,
      }
    };
    const { container } = render(<table><tbody><TodoItemRow {...props} /></tbody></table>);
    expect(container.querySelector('input[type="checkbox"]')).toHaveAttribute('checked');
  });

  it('renders delete button with tooltip label', async () => {
    const props = {
      item: {
        title: 'item', 
        id: 'id',
        completed: true,
      }
    };
    const { getByText, container } = render(<table><tbody><TodoItemRow {...props} /></tbody></table>);
    getByText('Remove');
    expect(container.querySelector('button')).toBeInTheDocument();
  });

});