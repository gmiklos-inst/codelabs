---
title: Using Redux with React
parent: React
nav_order: 5
---

# Using Redux with React

Now that we have our actions, reducer and store in place we can begin integrating our components with Redux. Start by installing the React bindings for Redux:

```
$ npm i --save react-redux
```

# Connecting the application to the store

In order to make the Redux store available to all our components the following two changes need to be made in `index.tsx`:

* Import the application store and the `<Provider/>` tag: 
```typescript
import {createAppStore} from "./store";
import { Provider } from 'react-redux';
```

* Wrap the application component with the provider tag:
```typescript
ReactDOM.render(<Provider store={createAppStore()}><App /></Provider>, container);
```

The `<Provider />` tag makes the store available to all the components that had been wrapped using the `connect()` function - connecting components is how we make them React-aware.

While you can connect and wrap an arbitrary number of components for the purposes of our simple application we are only going to wrap the main `<App/>` component which will then propagate any changes through properties.

# Making the App component Redux-aware

Our first steps integrating Redux with `App` component involve defining the properties we are going to use for retrieving application state from our store and dispatch actions. 

```typescript
type AppProps = {
    todos: TodoItem[],
    textInput: string,
    filterState: TodoFilterState,
    setTodoTextInput: (string) => void,
    setTodoFilterState: (TodoFilterState) => void,
    addTodo: () => void,
    toggleTodo: (string) => void,
    deleteTodo: (string) => void
};
```

The purpose of these properties in order of declaration:
* todos - the list of to-do items to display - used by the `TodoList` component.
* textInput - the current value of the to-do text input field - used by the `TodoInput` component.
* filterState - the current value of the filter radio group - used by the `TodoFilter` and `TodoList` component.
* setTodoTextInput - function to invoke when the text input changes - used by the `TodoInput` component.
* setTodoFilterState - function to invoke when the filter value changes - used by the `TodoFilter` component.
* addTodo - function to invoke when the user is ready to add the typed in to-do item. Used by the `TodoInput` component.
* toggleTodo - function to invoke when a particular to-do is toggled. Used by the `TodoList` and `TodoItemRow` component.
* deleteTodo - function to invoke when a particular to-do item is about to be deleted. Used by the `TodoList` and `TodoItemRow` component.

Each of the components need to be wired piece by piece. Lets start by wiring up all components in order as they appear.

## Wiring up components

In order to make things a little bit less verbose we unwrap all the properties at the beginning of the `render()` method:

```typescript
const { 
  todos, 
  setTodoTextInput, 
  setTodoFilterState, 
  addTodo, 
  toggleTodo, 
  textInput, 
  filterState, 
  deleteTodo 
} = this.props;
```

### TodoInput

```typescript
<TodoInput 
  onSubmit={() => addTodo()} 
  onChange={text => setTodoTextInput(text)} 
  value={textInput}
/>
```

The value of the `textInput` property is simply bound to the `value` property of `TodoInput`. Firing `onSubmit` will call `addTodo()` to create the current text input value as new to-do item. The `onChange` event will propagate changes using the `setTodoTextInput` function.

### TodoList

```typescript
<TodoList 
  todoItems={todos.filter((todo) => {
    switch (filterState) {
      case TodoFilterState.COMPLETED:
        return todo.completed;
      case TodoFilterState.ACTIVE:
        return !todo.completed;
      default:
        return true;
    }
  })} 
  onToggleTodo={id => toggleTodo(id)} 
  onDeleteTodo={id => deleteTodo(id)} 
/>
```

Passing `todoItems` as is to the `todoItems` property of `TodoList` would not suffice - we need to perform some filtering first based on the current `filterState`. For toggling and deletion to work `onToggleTodo` need to call their corresponding `toggleTodo` and `deleteTodo` functions with the item id to actually perform these actions.

### TodoStatus

```typescript
<TodoStatus itemLeftCount={todos.filter(todo => !todo.completed).length} />
```

The only thing we need in this case is the number of to-do items left to be done which can be easily computed by using `Array.filter()` and `Array.length`.

### TodoFilter

```typescript
<TodoFilter
          todoFilterState={filterState} 
          onChange={filterState => setTodoFilterState(filterState)}
        />
```

The filter component needs the current filter state to be passed in (`filterState` bound to `todoFilterState`) and it also needs to propagate changes (`onChange` invokes `setTodoFilterState` with the item id).

## Mapping state to properties

None of the properties mentioned while wiring up the components would exist if it there was not a mechanism that would pass them in. The properties we were using purely for their value was `textInput`, `filterState` and `todos`. We need to tell Redux how to map the application state to these properties:

```typescript
const mapStateToProps = (state: AppState) => {
  return {
    textInput: state.ui.textInput,
    filterState: state.ui.filterState,
    todos: state.todos,
  };
};
```

## Mapping dispatch to properties

We also had multiple components where the event handlers relied on properties that contained functions to invoke when a certain event happens. This is the mechanism where we can dispatch actions towards our store - the properties mapped here will be passed to the `App` component as simple functions that dispatch these actions.

```typescript
const mapDispatchToProps = dispatch => ({
  setTodoTextInput: text => dispatch(setTodoTextInput(text)),
  setTodoFilterState: id => dispatch(setTodoFilterState(id)),
  addTodo: () => dispatch(addTodo()),
  toggleTodo: id => dispatch(toggleTodo(id)),
  deleteTodo: id => dispatch(deleteTodo(id)),
});
```

## Making the App component connected to Redux

The last and most important thing to do is to wrap the `App` component in a so called container using `connect()`:

```typescript
export const App = connect(mapStateToProps, mapDispatchToProps)(AppComponent);
```

The component wrapped in such a container will receive the properties outlined in the mapping through Redux. Changes in state will be propagated through the properties mapped in `mapStateToProps`, functions for dispatching actions will be provided through the properties defined in `mapDispatchToProps`.

Congratulations! Now your application performs all the basic functionality that can be expected from such application.


## Full code listing for the `App` component (`app.tsx`):

```typescript
import React, { Component } from 'react';
import { TodoInput } from './components/TodoInput';
import { TodoList } from './components/TodoList';
import { TodoStatus } from './components/TodoStatus';
import { TodoFilter, TodoFilterState } from './components/TodoFilter';

import { setTodoTextInput, addTodo, toggleTodo, deleteTodo, setTodoFilterState } from './actions';
import {TodoItem} from "./model/TodoItem";

import { connect } from 'react-redux';
import { AppState } from './reducers';

type AppProps = {
    todos: TodoItem[],
    textInput: string,
    filterState: TodoFilterState,
    setTodoTextInput: (string) => void,
    setTodoFilterState: (TodoFilterState) => void,
    addTodo: () => void,
    toggleTodo: (string) => void,
    deleteTodo: (string) => void
};

class AppComponent extends Component<AppProps> {
  render() {
    const { 
      todos, 
      setTodoTextInput, 
      setTodoFilterState, 
      addTodo, 
      toggleTodo, 
      textInput, 
      filterState, 
      deleteTodo 
    } = this.props;
    return (
      <div className="app">
        <h1>Hi!</h1>
        <TodoInput 
          onSubmit={() => addTodo()} 
          onChange={text => setTodoTextInput(text)} 
          value={textInput}
        />
        <TodoList 
          todoItems={todos.filter((todo) => {
            switch (filterState) {
              case TodoFilterState.COMPLETED:
                return todo.completed;
              case TodoFilterState.ACTIVE:
                return !todo.completed;
              default:
                return true;
            }
          })} 
          onToggleTodo={id => toggleTodo(id)} 
          onDeleteTodo={id => deleteTodo(id)} 
        />
        <TodoStatus itemLeftCount={todos.filter(todo => !todo.completed).length} />
        <TodoFilter
          todoFilterState={filterState} 
          onChange={filterState => setTodoFilterState(filterState)}
        />
      </div>
    )
  }
}

const mapStateToProps = (state: AppState) => {
  return {
    textInput: state.ui.textInput,
    filterState: state.ui.filterState,
    todos: state.todos,
  };
};

const mapDispatchToProps = dispatch => ({
  setTodoTextInput: text => dispatch(setTodoTextInput(text)),
  setTodoFilterState: id => dispatch(setTodoFilterState(id)),
  addTodo: () => dispatch(addTodo()),
  toggleTodo: id => dispatch(toggleTodo(id)),
  deleteTodo: id => dispatch(deleteTodo(id)),
});

export const App = connect(mapStateToProps, mapDispatchToProps)(AppComponent);
```