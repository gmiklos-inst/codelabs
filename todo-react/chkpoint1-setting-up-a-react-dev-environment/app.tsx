import './styles.scss'

import React, { Component } from 'react';
import ReactDOM from 'react-dom';

class App extends Component {
  render() {
    return (
      <div className="app">
        <h1>Hi!</h1>
      </div>
    )
  }
}

const $container = document.getElementById('app-container');

ReactDOM.render(<App />, $container);
