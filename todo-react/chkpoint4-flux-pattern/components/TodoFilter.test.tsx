import React from 'react'
import { render, cleanup } from 'react-testing-library'
import { TodoFilter, TodoFilterState } from './TodoFilter';
import 'jest-dom/extend-expect';

describe('TodoFilter', () => {
  
  afterEach(cleanup);

  it('renders the options', async () => {
    const props = {todoFilterState: null};
    const { getByText } = render(<TodoFilter {...props} />);
    getByText('All');
    getByText('Active');
    getByText('Completed');
  });

  it('renders the unchecked checkboxes', async () => {
    const props = {todoFilterState: null};
    const { getByLabelText } = render(<TodoFilter {...props} />);
    expect(getByLabelText('All')).not.toHaveAttribute('checked');
    expect(getByLabelText('Active')).not.toHaveAttribute('checked');
    expect(getByLabelText('Completed')).not.toHaveAttribute('checked');
  });

  it('renders the checked current state', async () => {
    const props = {todoFilterState: TodoFilterState.ALL};
    const { getByLabelText } = render(<TodoFilter {...props} />);
    expect(getByLabelText('All')).toHaveAttribute('checked');
    expect(getByLabelText('Active')).not.toHaveAttribute('checked');
    expect(getByLabelText('Completed')).not.toHaveAttribute('checked');
  });

});