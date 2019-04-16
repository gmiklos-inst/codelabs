import './styles.css'
import '@inst/bridge-ui-components.svg-props/dist/main.css'
import '@inst/bridge-ui-components.text-input/dist/main.css'

import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { TodoInput } from './components/TodoInput';

class App extends Component {
  render() {
    return (
      <div className="app">
        <h1>Hi!</h1>
        <TodoInput />
      </div>
    )
  }
}

const container = document.getElementById('app-container');

ReactDOM.render(React.createElement(App), container);
