import React, { Component } from 'react';
import { TodoInput } from './components/TodoInput';
import { TodoList } from './components/TodoList';
import { TodoStatus } from './components/TodoStatus';
import { TodoFilter, TodoFilterState } from './components/TodoFilter';

export class App extends Component {
  render() {
    return (
      <div className="app">
        <TodoInput />
        <TodoList todoItems={
          [{
            id: "1",
            title: "item1",
          },
          {
            id: "2",
            title: "item2",
            completed: true,
          }]
        }/>
        <TodoStatus itemLeftCount={2} />
        <TodoFilter todoFilterState={TodoFilterState.ALL} />
      </div>
    )
  }
}

