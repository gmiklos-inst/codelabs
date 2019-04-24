# Flux architecture and the application state

Flux is an application architecture invented by Facebook for building client-side web applications. It is more like a pattern than a framework in which data flows in one direction. 

Major parts of an application that adheres to the Flux architecture principles:
* Views - retrieves data from the store and display them.
* Action - essentially events that can change application state as a consequence of them being disptached. Created by action helpers in most cases they contain pure data that describes the action in its simplest form.
* Stores - contains the state that can be mutated by the consequence of actions that are submitted through the dispatcher. The only way to mutate the store is to dispatch actions.
* Dispatcher - when the user interacts with a Flux application the view propagates action through a central dispatcher which in turn will effect changes in the store.

The undirectional flow can be described as follows:
![Unidirectional flow](images/flux.png)

Actions are triggered by user interaction or asynchronous events which then get submitted to a dispatcher that notifies the store which in turn modifies its state and also notifies the views that are interested in its state.

One of the popular implementations of Flux is the Redux library which we are going to use in this workshop. First, we are going to design the actions that are available in our application:

```typescript
import { v1 as uuidv1 } from 'uuid';

export const addTodo = (text: string) => ({
  type: 'ADD_TODO',
  id: uuidv1(),
  text
})

export const setVisibilityFilter = (filter: VisibilityFilters) => ({
  type: 'SET_VISIBILITY_FILTER',
  filter
})

export const toggleTodo = (id: string) => ({
  type: 'TOGGLE_TODO',
  id
})

enum VisibilityFilters {
    ShowAll = 'SHOW_ALL',
    ShowCompleted = 'SHOW_COMPLETED',
    ShowActive = 'SHOW_ACTIVE'
}
```

In order to distinguish actions between each other we are going to use a `type` field.

The `addTodo` action as its name implies creates a now to-do item. All it needs to contain is the to-do ID and the actual text.

The `setVisibilityFilter` action sets the current visibility filter. It only contains the target filter to activate.

The `toggleTodo` action toggles the completed state on a specific to-do item. All it contains is just the to-do ID.

As you can see actions should be kept short and sweet with minimal amount of fuss around their creation.