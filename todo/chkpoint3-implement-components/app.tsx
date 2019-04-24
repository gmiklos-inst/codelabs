import './styles.css'
import '@inst/bridge-ui-components.svg-props/dist/main.css'
import '@inst/bridge-ui-components.text-input/dist/main.css'
import '@inst/bridge-ui-components.table/dist/main.css'

import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { TodoInput } from './components/TodoInput';
import { TodoList } from './components/TodoList';

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
          }]
        }/>
      </div>
    )
  }
}

const container = document.getElementById('app-container');

ReactDOM.render(<App />, container);
