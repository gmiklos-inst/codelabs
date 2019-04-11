import './styles.scss'

import React, { Component } from 'react'
import ReactDOM from 'react-dom'

import theme from '@instructure/ui-themes/lib/canvas/high-contrast'
theme.use({ overrides: { colors: { brand: 'red' } } })

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

ReactDOM.render(React.createElement(App), $container);
