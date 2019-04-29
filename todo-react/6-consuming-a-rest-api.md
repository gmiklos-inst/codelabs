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

# Synchronous and asynchronous actions

Before delving deeper into using thunks we must elaborate a bit on the differences between the types of actions:

* Synchronous actions are what we were using so far - dispatching them has an immediate, synchronous effect - the state updates immediately. They are represented as simple, immutable objects.
* Asynchronous actions are essentially functions that dispatch other events later when they are executed by `redux-thunk`. Functions like these do not need to be pure - you can fire off any sort of asynchronous computation and when that either succeeds or fails you can dispatch the appropriate synchronous action that signifies success or failure.

Redux-Thunk is a so-called middleware that lets us define asynchronous actions. When we design an asynchronous action we usually create three synchronous actions to dispatch:
* Began processing the async request.
* Async request has finished successfully.
* Async request had failed. 

For the purposes of this tutorial we will only handle the successful outcome. It is left for the reader to implement loading or error states for the application.

# Creating actions for loading to-do items

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

type LoadTodosAction = (dispatch) => void;

export type AsyncAction = LoadTodosAction;

const AUTH = {
    Authentication: config.apiKey,
}

export const loadTodos = (): LoadTodosAction => {
    return dispatch => {
        axios
            .get(`${config.apiBaseUrl}/todos/`, {
                headers: {...AUTH},
            } as any)
            .then((response) => {
                dispatch(setTodos(response.data));
            });
    };
}
```

We define the action as a lambda that takes the dispatch function from Redux as the parameter. In order to successfully communicate with the backend we need to retrieve the `apiKey` value from app configuration and include in our request headers. Similarly important is to get the `apiBaseUrl` to the direct the requests towards the correct host.

The action creator for this async action is where we fire off the HTTP GET request to retrieve to-do items - when that request successfully completes the `setTodos` will be dispatched that will carry the items from the response data.

One minor thing to adjust in `actions/index.ts` is to include our async action union type (`AsyncAction`) that we have created in the union type that lists all our application actions:

```typescript
export type AppAction = 
    SetTodoTextInputAction | 
    SetTodoFilterStateAction | 
    AddTodoAction | 
    ToggleTodoAction | 
    DeleteTodoAction |
    SetTodosAction |
    AsyncAction;
``` 

# Expanding the reducer

The asynchronous action itself does not need to be handled in the reducer but the synchronous actions still do need an implementation. Open `reducers/index.ts` and add the following to handle the `setTodos` action:

```typescript
case SET_TODOS:
    return {
        ...state,
        todos: action.todos,
    };
``` 

# Enhancing the store with the thunk middleware

Just so that our store can handle asynchronous events we need to include the thunk middleware while creating the store in `store/index.ts`:

```typescript
import { createStore, applyMiddleware } from 'redux';
import thunk from 'redux-thunk';

export const createAppStore = (initialState: AppState) => createStore(appReducer, initialState, applyMiddleware(thunk));
```

# Loading the items on application load

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
  loadTodos: () => dispatch(loadTodos()),
});
``` 

By now, you should have the to-do items loaded from the backend.