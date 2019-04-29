import React from 'react'
import { render, cleanup, fireEvent, waitForElement } from 'react-testing-library';
import { TodoInput } from './TodoInput';

describe('TodoInput', () => {
  
  afterEach(cleanup);

  it.only('renders the input', () => {
    const props = {
      value: 'TODO item text',
      onChange: jest.fn(),
      onSubmit: jest.fn(),
    };
    const { getByDisplayValue } = render(<TodoInput {...props}/>);
    getByDisplayValue('TODO item text');
  });

  it.only('let edit the input', async () => {
    const onChangeMock = jest.fn();
    const props = {
      value: 'foo',
      onChange: onChangeMock,
      onSubmit: jest.fn(),
    };
    const { getByDisplayValue, getByText } = render(<TodoInput {...props}/>);
    const inputNode = getByDisplayValue('foo');
    fireEvent.change(inputNode, {target : {value : 'bar'}});
    expect(onChangeMock.mock.calls[0][0]).toBe('bar');
  });

  it.only('calls submit on enter', async () => {
    const onSubmitMock = jest.fn();
    const props = {
      value: 'foo',
      onChange: jest.fn(),
      onSubmit: onSubmitMock,
    };
    const { getByDisplayValue, getByText } = render(<TodoInput {...props}/>);
    const inputNode = getByDisplayValue('foo');
    fireEvent.change(inputNode, {target : {value : 'baz'}});
    fireEvent.keyUp(inputNode, { key: "Enter", keyCode: 13, charCode:13 });
    //const updatedinputNode = await waitForElement(() => getByDisplayValue('bar'))
    expect(onSubmitMock.mock.calls).toHaveLength(1);
  });
});