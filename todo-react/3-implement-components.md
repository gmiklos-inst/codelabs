---
title: Implementing our components
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

## TodoInput
*Can be found under components/TodoInput.tsx*

Using the Component class comprises a large bulk of work that we will going to do with React. TodoInput in this case is a very simple composite component that wraps a TextInput with some custom icons.

Make a directory named `compomnents` and create a file named `TodoInput.tsx` with the following contents:  

```typescript jsx
import React, { Component } from 'react';

import { default as TextInput } from '@inst/bridge-ui-components.text-input';
import { EditIcon } from '@inst/bridge-ui-components.icon';

export type TodoInputProps = {
    onChange?: (text: string) => void;
    onSubmit?: () => void;
    value?: string;
};

export class TodoInput extends Component<TodoInputProps> {
    render() {
      const { onChange, onSubmit, value } = this.props;
      return <TextInput
        value={value || ""}
        label="Add new TODO item here"
        icon={<EditIcon />}
        onChange={event => onChange && onChange(event.target.value)}
        onKeyUp={(event) => {
          if (event.charCode === 13 || event.keyCode === 13) {
            onSubmit && onSubmit();
          }
        }}
       data-testid="TodoInput"
      />;
    }
}
```

Most of the components we are going to write are subclasses of the React.Component class which also has a type parameter specifying the properties associated with this component. Properties on a particular component can be set using attributes:
```typescript jsx
<TodoInput value="some value" />
```
You can specify an arbitrary amount of properties for a single component.

In our case the TodoInput component needs to have the following properties:
* value - the value entered in the text field
* onChange (optional) - event handler to call when the value in the text field changes
* onSubmit (optional) - event handler invoked when the user presses Enter

Next, we need to tell React how to render our component in the `render()` method which always return an element that holds the visual representation of our component. We are going to wrap a BUC `<TextInput />` component and enhance it with a few new features.

Setting the `value` property on TextInput will set its text. As this is a constant value the TextInput field will be read-only, but later on when we introduce state the field will be editable by the user.

The `label` property just sets the floating label that is visible on the `TextInput`.

The `icon` property accepts an arbitrary component to display as an icon. The UI library contains an `<EditIcon />` component that can be used as-is here. Notice that in this case we need to use braces to pass a component as the property value - we are passing an actual element there not just a string.

The lambda set on the `onChange` property will be invoked whenever the value of the text field changes - e.g. when the user types in something new. We are going to delegate this event handler to a higher level and invoke it only when this property is actually set to a value (notice the ? for marking it nullable in the props definition).

The lambda set on the `onKeyUp` property will be invoked on each keypress received by the `TextInput` component. The only key we are interested in this case is the Enter key, which will invoke the function defined by the `onSubmit` property on our component if it is not null (`onSubmit` was marked for nullability just like `onChange`).

In order to ensure that our `TodoInput` component loads properly we are going to add this simple test. Create a file named `TodoInput.test.tsx` with the following contents:

```typescript jsx
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

## TodoItemRow
*Can be found under components/TodoItemRow.tsx*

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
  onToggle?: () => void;
  onDelete?: () => void;
};

export class TodoItemRow extends Component<TodoItemRowProps> {
    render() {
      const { item, onToggle, onDelete } = this.props;
        return <Tr data-testid="TodoItemRow">
          <Td>
            <Checkbox checked={item.completed} onChange={() => onToggle && onToggle()} data-testid="TodoItemToggle"/>
          </Td>
          <Td>
            {
              item.completed ?
                (<del>{item.title}</del>) :
                item.title
            }
          </Td>
          <Td>
            <IconButton label="Remove" filled icon={<ClearIcon />} onClick={() => onDelete && onDelete()} />
          </Td>
        </Tr>;
    }
}
```

More complex components in React receive data using components. When using Typescript we have to opportunity to precisely define how the properties will look:

```typescript
export type TodoItemRowProps = {
    item: TodoItem;
    onToggle?: () => void;
    onDelete?: () => void;
};
``` 

The properties list here have the following purpose:
* item - the item to be displayed
* onToggle (optional) - invoked when the to-do item is toggled for completion
* onDelete (optional) - invoked when the user clicks on the delete button

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
*Can be found under components/TodoList.tsx*

Now that we have the presentational part of the items that we are going to display done we also need a list component that takes these items as a property and then displays them as a part of a list.

Routinely as before, create a file named `TodoList.tsx` in `components`:
```typescript jsx
import React, { Component } from 'react';

import { default as Table, THead, Tr, Th, TBody } from '@inst/bridge-ui-components.table';
import { TodoItem } from '../model/todoItem';
import { TodoItemRow } from './TodoItemRow';

export type TodoListProps = {
  todoItems: TodoItem[];
  onToggleTodo?: (id) => void;
  onDeleteTodo?: (id) => void;
};

export class TodoList extends Component<TodoListProps> {
  render() {
    const { onToggleTodo, onDeleteTodo, todoItems } = this.props;
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
            todoItems.map((todoItem) =>
              <TodoItemRow
                  item={todoItem}
                  key={todoItem.id}
                  onToggle={() => onToggleTodo && onToggleTodo(todoItem.id) }
                  onDelete={() => onDeleteTodo && onDeleteTodo(todoItem.id) }
              />
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
  onToggleTodo?: (id) => void;
  onDeleteTodo?: (id) => void;
};
```

Each of these properties have the following purpose:
* todoItems - the actual list of to-do items to display.
* onToggleTodo (optional) - invoked when the user toggles a particular to-do item.
* onDeleteTodo (optional) - invoked when the user attempts to delete a particular to-do item.

What we are going to do is to build a table that displays all the to-do items that were passed in as properties. Given that this property is just an array it is a frequent pattern to just use the `.map()` function to transform these items into actual components:
```typescript jsx
{
todoItems.map((todoItem) =>
  <TodoItemRow
      item={todoItem}
      key={todoItem.id}
      onToggle={() => onToggleTodo && onToggleTodo(todoItem.id) }
      onDelete={() => onDeleteTodo && onDeleteTodo(todoItem.id) }
  />
)
}
```

Returning an array in a curly braces will render all the elements produced by that expression. It is important to note that in components that maintain a list of items, the `key` attribute is very important - this needs to be a unique id for each item that React will use to efficiently render and update these items. The `onToggle` and `onDelete` properties are just delegated to a higher level with additionally specifying the particular to-do item it was invoked for.

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
  onChange?: (filterState: TodoFilterState) => void;
};

export class TodoFilter extends Component<TodoFilterProps> {
    render() {
        return <RadioButtonGroup 
          selected={this.props.todoFilterState}
          onChange={filterState => this.props.onChange && this.props.onChange(filterState)}
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
  onChange?: (filterState: TodoFilterState) => void;
};
```

The purpose of these properties are the following:
* todoFilterState - holds the current filter state.
* onChange (optional) - invoked when value of radio group changes.

Our `render()` method mostly deals with grouping the radio buttons into a single value. In order to make the group fully functional we bind the `todoFilterState` property to the `RadioButtonGroup` `selected` property. Similarly as with other event handler properties in previous components the `onChange` property just delegates the event to a higher level.

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