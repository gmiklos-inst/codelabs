import './styles.css'
import '@inst/bridge-ui-components.svg-props/dist/main.css'
import '@inst/bridge-ui-components.text-input/dist/main.css'
import '@inst/bridge-ui-components.table/dist/main.css'
import '@inst/bridge-ui-components.chip/dist/main.css'
import '@inst/bridge-ui-components.radio-button/dist/main.css'
import '@inst/bridge-ui-components.checkbox/dist/main.css'
import '@inst/bridge-ui-components.button/dist/main.css'
import '@inst/bridge-ui-components.icon-button/dist/main.css'
import '@inst/bridge-ui-components.icon/dist/main.css'
import '@inst/bridge-ui-components.tooltip/dist/main.css'

import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { TodoInput } from './components/TodoInput';
import { TodoList } from './components/TodoList';
import { TodoStatus } from './components/TodoStatus';
import { TodoFilter, TodoFilterState } from './components/TodoFilter';

class App extends Component {
  render() {
    return (
      <div className="app">
        <h1>Hi!</h1>
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

const container = document.getElementById('app-container');

ReactDOM.render(<App />, container);
