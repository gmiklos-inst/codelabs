---
title: Consuming a REST API
parent: React
nav_order: 6
---

# Consuming a REST API

As most applications use some kind of online data source we our to-do application will retrieve and persist our to-do items using a REST API.

Create a file named `app-config.json` in your workspace root with the following contents:

```json
{
  "apiBaseUrl": "https://instructure-rails-workshop.herokuapp.com/",
  "apiKey": "such-secret-much-wow"
}
```

This is where we are going to store all externalized configuration for our application. The `apiBaseUrl` will be used to construct REST API paths and the `apiKey` will be sent with all requests as a header in order to authenticate with our credentials.

As we are going to import this file in our Typescript code the compiler the option for resolving json modules should be enabled in `tsconfig.json`: 

```json
{
  "compilerOptions": {
    "jsx": "react",
    "noImplicitAny": false,
    "esModuleInterop": true,
    "lib": ["es6", "dom"],
    "outDir": "dist",
    "resolveJsonModule": true
  },
  "exclude": [
    "node_modules"
  ]
}
```

We will also need to bring in `axios` and `redux-thunk` as new application dependencies:

```
$ npm i --save axios redux-thunk
```
# Loading to-do items from the backend

## Synchronous and asynchronous actions

Before delving deeper into using thunks we must elaborate a bit on the differences between the types of actions:

* Synchronous actions are what we were using so far - dispatching them has an immediate, synchronous effect - the state updates immediately. They are represented as simple, immutable objects.
* Asynchronous actions are essentially functions that dispatch other events later when they are executed by `redux-thunk`. Functions like these do not need to be pure - you can fire off any sort of asynchronous computation and when that either succeeds or fails you can dispatch the appropriate synchronous action that signifies success or failure.

Redux-Thunk is a so-called middleware that lets us define asynchronous actions. When we design an asynchronous action we usually create three synchronous actions to dispatch:
* Began processing the async request.
* Async request has finished successfully.
* Async request had failed. 

For the purposes of this tutorial we will only handle the successful outcome. It is left for the reader to implement loading or error states for the application.

## Creating actions for loading to-do items

Not implementing the load and load error states will require us to only create a single synchronous action for setting the to-do items. 

Expand your `actions/index.ts` file with the following:
```typescript
export const SET_TODOS = 'SET_TODOS';

type SetTodosAction = {
    type: typeof SET_TODOS;
    todos: TodoItem[];
};

export type AppAction = 
    SetTodoTextInputAction | 
    SetTodoFilterStateAction | 
    AddTodoAction | 
    ToggleTodoAction | 
    DeleteTodoAction |
    SetTodosAction;

export const setTodos = (todos: TodoItem[]): AppAction => ({
    type: SET_TODOS,
    todos,
});
```

This includes all the boilerplate we create for a synchronous action - a type constant, a type for the action, adding the action to the AppAction union type and providing an action creator for it.

Now that we have the required synchronous action we need to create the asynchronous action that will be using it. Create a files named `asyncActions.ts` under the `actions` directory with the following contents:

```typescript
import axios from 'axios';
import config from '../app-config.json';
import { setTodos } from '.';
export const LOAD_TODOS = 'LOAD_TODOS';

export type AsyncAction = (dispatch, getState) => void;

const AUTH = {
    'x-api-key': config.apiKey,
}

export const loadTodosAsync = (): AsyncAction => {
    return dispatch => {
        dispatch(setTodoTextInput(''));
        axios
            .get(`${config.apiBaseUrl}/todos/?page_size=1000`, {
                headers: { ...AUTH },
            } as any)
            .then((response) => {
                dispatch(setTodos(response.data
                    .map(item => ({
                        id: item.id,
                        title: item.title,
                        completed: item.completed,
                        createdAt: item.created_at,
                    }))
                    .sort((i1: TodoItem, i2: TodoItem) => i1.createdAt < i2.createdAt ? 1 : -1)
                ));
            });
    };
};
```

We define the action as a lambda that takes the dispatch function from Redux as the parameter. In order to successfully communicate with the backend we need to retrieve the `apiKey` value from app configuration and include in our request headers. Similarly important is to get the `apiBaseUrl` to the direct the requests towards the correct host.

The action creator for this async action is where we fire off the HTTP GET request to retrieve to-do items - when that request successfully completes the `setTodos` will be dispatched that will carry the items from the response data. As the REST API has a slightly different naming convention with underscores we need to peform some mapping on the data. We also sort the items by the creation date.

## Expanding the reducer

The asynchronous action itself does not need to be handled in the reducer but the synchronous actions still do need an implementation. Open `reducers/index.ts` and add the following to handle the `setTodos` action:

```typescript
case SET_TODOS:
    return {
        ...state,
        todos: action.todos,
    };
``` 

## Enhancing the store with the thunk middleware

Just so that our store can handle asynchronous events we need to include the thunk middleware while creating the store in `store/index.ts`:

```typescript
import { createStore, applyMiddleware } from 'redux';
import thunk from 'redux-thunk';

export const createAppStore = (initialState: AppState) => createStore(appReducer, initialState, applyMiddleware(thunk));
```

## Loading the items on application load

Some small changes still need to be done on the main application component so that asynchronous actions will be dispatched on startup.

First, expand the properties with a `loadTodos` function: 

```typescript
type AppProps = {
    todos: TodoItem[],
    textInput: string,
    filterState: TodoFilterState,
    setTodoTextInput: (string) => void,
    setTodoFilterState: (TodoFilterState) => void,
    addTodo: () => void,
    toggleTodo: (string) => void,
    deleteTodo: (string) => void,
    loadTodos: () => void,
};
```

Then use the `componentDidMount()` lifecycle method to dispatch this action when the component is mounted:

```typescript
componentDidMount() {
    const { loadTodos } = this.props;
    loadTodos();
}
```
  
Just so that the `loadTodos` property is available for the component we also need to update the mapping to include it:

```typescript
const mapDispatchToProps = dispatch => ({
  setTodoTextInput: text => dispatch(setTodoTextInput(text)),
  setTodoFilterState: id => dispatch(setTodoFilterState(id)),
  addTodo: () => dispatch(addTodo()),
  toggleTodo: id => dispatch(toggleTodo(id)),
  deleteTodo: id => dispatch(deleteTodo(id)),
  loadTodos: () => dispatch(loadTodosAsync()),
});
``` 

By now, you should have the to-do items loaded from the backend.

# Adding a to-do item using the REST API

Some general changes need to be done to the way we are currently handling the addition of todo-items as we are going to get IDs from the server side when the item gets created.

## Tweak existing synchronous action

Change the `AddTodo` action in `actions/index.ts` to include the actual to-do item:

```typescript
type AddTodoAction = {
    type: typeof ADD_TODO;
    todoItem: TodoItem;
};
```

Also update the action creator for this action:

```typescript
export const addTodo = (todoItem: TodoItem): AppAction => ({
    type: ADD_TODO,
    todoItem,
});
```

## Create an asynchronous action

Lets expand `actions/asyncActions.ts` with a new action:

```typescript
export const addTodoAsync = (): AsyncAction => {
    return (dispatch, getState) => {
        const { ui } = getState();
        axios
            .post(`${config.apiBaseUrl}/todos/`, {
                title: ui.textInput,
                completed: false,
            },{
                headers: { ...AUTH },
            } as any)
            .then((response) => {
                dispatch(addTodo(response.data));
            });
    };
};
```

Somewhat similarly to the `loadTodoAsync` action it sends a HTTP POST request to the appropriate endpoint and then dispatches an `addTodo` action with the response data. It is worth to note that asynchronous actions can also base their logic on the current state - you can query the current state using the `getState()` function which gets passed in to the returned function.

## Tweak the reducer

Remove all instances of the `lastId` field in the `AppState` type and the `initialState` constant object as we no longer need it.

The item dispatched in the data can be merged into our list of items as is. We need to update our reducer in `reducers/index.ts` to do so:

```typescript
case ADD_TODO:
    return {
        ...state, 
        ui: {...state.ui, textInput: ''},
        todos: [action.todoItem, ...state.todos],
    };
```

Be sure to prepend that item to the list.

## Update dispatch mapping

Instead of simply calling into `addTodo()` we now need to call `addTodoSync()` at the dispatch to props mapping in `app.tsx`:

```typescript
const mapDispatchToProps = dispatch => ({
  // ...
  addTodo: () => dispatch(addTodoSync()),
  // ...
});
``` 

With these changes the to-do items should be persisted to the REST API service.

# Deleting a to-do item through the REST API

With the current machinery in place implementing deletion is rather simple.

## Create an async action

Extend `actions/asyncActions.ts` with the following code:

```typescript
export const deleteTodoAsync = (id: string): AsyncAction => {
    return dispatch => {
        dispatch(deleteTodo(id));
        axios
            .delete(`${config.apiBaseUrl}/todos/${id}`, {
                headers: { ...AUTH },
            } as any);
    };
};
```

In a similar fashion as before we send a HTTP DELETE request and dispatch an action to delete that item from the list.

## Update dispatch mapping

A slight change is required to the mappings in `app.tsx`:

```typescript
const mapDispatchToProps = dispatch => ({
  // ...
  deleteTodo: id => dispatch(deleteTodoAsync(id)),
  // ...
});
``` 

Item deletion is now propagated towards the REST API service.

# Persist toggling in the REST API service

Persisting the toggled state is similarly easy as deletion.

## Create async action

Extend `actions/asyncActions.ts` with the following code:

```typescript
export const toggleTodoAsync = (id: string): AsyncAction => {
    return (dispatch, getState) => {
        const { todos } = getState();
        dispatch(toggleTodo(id));
        axios
            .put(`${config.apiBaseUrl}/todos/${id}`, {
                completed: !todos.filter(todo => todo.id === id)[0].completed,
            }, {
                headers: { ...AUTH },
            } as any);
    };
};
```

You know the drill - we send a HTTP PUT request and dispatch the corresponding synchronous action that toggles that specific ID.

## Update dispatch mapping

Update the mappings in `app.tsx`:

```typescript
const mapDispatchToProps = dispatch => ({
  // ...
  toggleTodo: id => dispatch(toggleTodoAsync(id)),
  // ...
});
``` 

We now have a fully working to-do application that persists its data using a REST API service.