---
title: Implementing a component using BUC
parent: React
nav_order: 3
---


# Implementing our components

The Bridge UI components library is a collection of commonly used components in the Bridge suite of products.

Documentation along with examples can be accessed here on Storybook:
[Bridge UI Components](https://buc.inseng.net/?path=/story/bridge-ui-components--readme)

Lets grab all the components we are going to need for the next few components:

```
npm i --save @inst/bridge-ui-components @inst/bridge-ui-components.avatar @inst/bridge-ui-components.buc-utils @inst/bridge-ui-components.button @inst/bridge-ui-components.checkbox @inst/bridge-ui-components.chip @inst/bridge-ui-components.focus-indicator @inst/bridge-ui-components.format-message@inst/bridge-ui-components.icon@inst/bridge-ui-components.icon-button @inst/bridge-ui-components.overridable-link@inst/bridge-ui-components.radio-button @inst/bridge-ui-components.svg-props @inst/bridge-ui-components.table @inst/bridge-ui-components.text-input @inst/bridge-ui-components.tooltip
```

In order to fix a compilation problem while using parcel you need to add an alias to your `package.json` (place this at the root of the object):

```json
{
  "alias": {
    "buc/format-message": "@inst/bridge-ui-components.format-message"
  }
}
```  

At this point of the tutorial we will only create the presentational parts of the components - state, properties and events - everything that makes our components interactive will be added later on. 

## TodoInput (components/TodoInput.tsx)

Using the Component class comprises a large bulk of work that we will going to do with React. TodoInput in this case is a very simple composite component that wraps a TextInput with some custom icons.

Make a directory named `compomnents` and create a file named `TodoInput.tsx` with the following contents:  

```typescript jsx
import React, { Component } from 'react';

import { default as TextInput } from '@inst/bridge-ui-components.text-input';
import { EditIcon } from '@inst/bridge-ui-components.icon';

export class TodoInput extends Component {
    render() {
      return <TextInput data-testid="TodoInput"
        value="TODO item text"
        label="Add new TODO item here"
        onChange={() => {}}
        icon={<EditIcon />}
      />;
    }
}
```

After importing the components that are needed we to tell React how to render our component in the `render()` method which always return an element that holds the visual representation of our component.

Setting the `value` property on TextInput will set its text. As this is a constant value the TextInput field will be read-only, but later on when we introduce state the field will be editable by the user.

The `label` property just sets the floating label that is visible on the `TextInput`.

The `icon` property accepts an arbitrary component to display as an icon. The UI library contains an `<EditIcon />` component that can be used as-is here. Notice that in this case we need to use braces to pass a component as the property value - we are passing an actual element there not just a string.

In order to ensure that our `TodoInput` component loads properly we are going to add this simple test. Create a file named `TodoInput.test.tsx` with the following contents:

```typescript jsx
import React from 'react'
import { render, cleanup, getByTestId } from 'react-testing-library'
import { TodoInput } from './TodoInput';

describe('TodoInput', () => {
  
  afterEach(cleanup);

  it('renders the input', () => {
    const { getByDisplayValue } = render(<TodoInput />);
    getByDisplayValue('TODO item text');
  });
});
```

As mentioned before, you can just run `npm test` to verify that everything works as it should.

The specific documentation for `TextInput` is available [here](https://buc.inseng.net/?path=/story/textinput--textinputsimple).
By clicking on "Show info" you can display the available property types for this component.

The JS extensions provided by React allows us to declare the TextInput tag in-line - the rationale for this extension is that otherwise creating these element would be quite a hassle.

Consider these two snippets:

Without extensions:
```javascript
const element = React.createElement(
  'h1',
  {className: 'greeting'},
  'Hello, world!'
);
```

Using JSX:
```jsx
const element = (
  <h1 className="greeting">
    Hello, world!
  </h1>
);
```

As you can see this feels much more natural, especially when having to add multiple levels of children to your element.

## TodoItemRow (components/TodoItemRow.tsx)

Before we are able to implement the TodoList component we need to have a component for the actual items. Because we're using TypeScript we also need to define what those items look like structurally. Create a directory named `model` and create a file named `TodoItem.ts` with the following contents:

```typescript
export interface TodoItem { 
    id: string;
    /**
     * Description of the task
     */
    title: string;
    /**
     * True if the task is already completed
     */
    completed?: boolean;
    createdAt?: Date;
    updatedAt?: Date;
    completedAt?: Date;
}
```

This is also going to be the common representation that the frontend and backend parts use data exchange.

Just as with the previous component create a file named `TodoItemRow.tsx` in the `components` directory:
```typescript jsx
import React, { Component } from 'react';

import { TodoItem } from '../model/todoItem';
import { Tr, Td } from '@inst/bridge-ui-components.table';
import { default as Checkbox } from '@inst/bridge-ui-components.checkbox';
import { IconButton } from '@inst/bridge-ui-components.icon-button';
import { ClearIcon } from '@inst/bridge-ui-components.icon';

export type TodoItemRowProps = {
  item: TodoItem;
};

export class TodoItemRow extends Component<TodoItemRowProps> {
    render() {
      const { item } = this.props;
        return <Tr>
          <Td>
            <Checkbox checked={item.completed} />
          </Td>
          <Td>
            {item.title}
          </Td>
          <Td>
            <IconButton label="Clear" filled icon={<ClearIcon />} />
          </Td>
        </Tr>;
    }
}
```

More complex components in React receive data using components. When using Typescript we have to opportunity to precisely define how the properties will look:

```typescript
export type TodoItemRowProps = {
  item: TodoItem;
};
``` 

As you can see this component only has a single property named `item` that is of the `TodoItem` type that we had defined previously. 

The TodoItemRow component uses a `<tr>` tag to display itself and has a `item` property that lets users of this component to pass in the actual data for the item. You can specify an arbitrary amount of properties for a single component. 

All of the properties that are passed to this component can be accessed using the `this.props` class variable. This usually takes the form of:
 
```jsx
 <TodoItemRow item={someitem} />
```
 
 Whenever you reference properties in your `render()` method your component will bind that property and know when to re-render due to changes. As you can see from the source the `checked` property of the `Checkbox` component is bound to the `completed` field of the item property. 

If you'd like to display text inline you can just put an expression with curly braces in the content part of tag:

```jsx
<Td>
    {item.title}
</Td>
```

Just as before, passing an element to an attribute is done by using braces instead of quotes:

```jsx
<IconButton label="Clear" filled icon={<ClearIcon />} />
```  

Later on, this `IconButton` will enable us to delete this to-do item.

A more involved test case for making sure that this component loads and renders properly looks like this:
```typescript jsx
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
```

## TodoList

Now that we have the presentational part of the items that we are going to display done we also need a list component that takes these items as a property and then displays them as a part of a list.

Routinely as before, create a file named `TodoList.tsx` in `components`:
```typescript jsx
import React, { Component } from 'react';

import { default as Table, THead, Tr, Th, TBody } from '@inst/bridge-ui-components.table';
import { TodoItem } from '../model/todoItem';
import { TodoItemRow } from './TodoItemRow';

export type TodoListProps = {
  todoItems: TodoItem[];
};

export class TodoList extends Component<TodoListProps> {
  render() {
    return <div className="todo-list" data-testid="TodoList">
      <Table responsive hover>
        <THead>
          <Tr>
            <Th>
              Check
            </Th>
            <Th>
              Item
            </Th>
            <Th>
              Action
            </Th>
          </Tr>
        </THead>
        <TBody>
          {
            this.props.todoItems.map((todoItem) =>
              <TodoItemRow item={todoItem} key={todoItem.id} />
            )
          }
        </TBody>
      </Table>
    </div>;
  }
}
``` 

First, we start by defining how our properties will look:
```typescript
export type TodoListProps = {
  todoItems: TodoItem[];
};
```

This specifies that only a single property named `todoItems` is passed in which is an array of TodoItem types.

Then we build a table that displays all the to-do items that were passed in as properties. Given that this property is just an array it is a frequent pattern to just use the `.map()` function to transform these items into actual components:
```typescript jsx
{
this.props.todoItems.map((todoItem) =>
  <TodoItemRow item={todoItem} key={todoItem.id} />
)
}
```

Returning an array in a curly braces will render all the elements produced by that expression. It is important to note that in components that maintain a list of items, the `key` attribute is very important - this needs to be a unique id for each item that React will use to efficiently render and update these items.

We also include some tests to ensure that the component renders properly (`TodoList.test.tsx`):

```typescript jsx
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
```

## TodoFilter

As per the specification in the previous chapter we also need a component which consists of a group of radio buttons that specify how the displayed items should be filtered.

We are going to name the file for this component as `TodoFilter.tsx` and put it under the `components` directory:

```typescript jsx

import React, { Component } from 'react';

import { RadioButtonGroup, default as RadioButton  } from '@inst/bridge-ui-components.radio-button';

export enum TodoFilterState {
  ALL,
  ACTIVE,
  COMPLETED,
}

export type TodoFilterProps = {
  todoFilterState: TodoFilterState;
};

export class TodoFilter extends Component<TodoFilterProps> {
    render() {
        return <RadioButtonGroup 
          selected={this.props.todoFilterState}
          onChange={() => {}}
          data-testid="TodoFilter"
        >
          <RadioButton
            label="All"
            value={TodoFilterState.ALL}
            data-testid={`TodoFilterStateOption${TodoFilterState.ALL}`}
          />
          <RadioButton
            label="Active"
            value={TodoFilterState.ACTIVE}
            data-testid={`TodoFilterStateOption${TodoFilterState.ACTIVE}`}
          />
          <RadioButton
            label="Completed"
            value={TodoFilterState.COMPLETED}
            data-testid={`TodoFilterStateOption${TodoFilterState.COMPLETED}`}
          />
        </RadioButtonGroup>;
    }
}
```

We start by defining the components' properties as before but in order to do that we need to define an enum which holds all the possible filter values:

```typescript jsx
export enum TodoFilterState {
  ALL,
  ACTIVE,
  COMPLETED,
}
```

After the definition we can elaborate on how our properties will look:
```typescript jsx
export type TodoFilterProps = {
  todoFilterState: TodoFilterState;
};
```

We only need a single property that specifies the currently active filter.

Our `render()` method mostly deals with grouping the radio buttons into a single value. Whichever filter type was passed in as the property will determine the currently selected radio button in this group.

The following tests ensure that this component renders as intended (`TodoFilter.test.tsx`):
```typescript jsx
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
```

## TodoStatus

The last of our user interface components to develop is the status display that tells the user how many of to-do items remain to be done.

This component should be put in a file named `TodoStatus.tsx` under `components`:

```typescript jsx
import React, { Component } from 'react';

import { default as Chip } from '@inst/bridge-ui-components.chip';

export type TodoStatusProps = {
  itemLeftCount: number;
};

export class TodoStatus extends Component<TodoStatusProps> {
    render() {
        const label = this.props.itemLeftCount > 1 ? `${this.props.itemLeftCount} items left` : this.props.itemLeftCount === 1 ? `1 item left` : `no items left`;
        return <Chip label={label} data-testid="TodoStatus"/>;
    }
}
```

We are only going to pass in a single property that denotes how many to-do items remain to be done.

A little bit of extra logic is needed to display "no items left" when zero items remain instead of "0 items left".

The `Chip` tag is just a small visual component that displays text with a rounded rectangle background.

In order to verify rendering we create the following testcases (`TodoStatus.test.tsx`):

```typescript jsx
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
```