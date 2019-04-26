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

import ReactDOM from 'react-dom';
import React from 'react';

import { App } from "./app";

const container = document.getElementById('app-container');

ReactDOM.render(<App />, container);