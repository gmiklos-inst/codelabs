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

import React from 'react';
import ReactDOM from 'react-dom';
import { createStore } from 'redux';
import { Provider } from 'react-redux';
import { appReducer, AppState } from './reducers';
import { App } from './app';

export const loadState = (): AppState | undefined => {
    try {
      const serializedState = localStorage.getItem('state');
      if (serializedState === null) {
        return undefined;
      }
      return JSON.parse(serializedState);
    } catch (err) {
      return undefined;
    }
  }; 
  
  export const saveState = (state: AppState) => {
    try {
      const serializedState = JSON.stringify(state);
      localStorage.setItem('state', serializedState);
    } catch {
      // ignore errors
    }
  };
  
  const store = createStore(appReducer, loadState());
  
  store.subscribe(() => {
    saveState(store.getState());
  });
  
  
  const container = document.getElementById('app-container');


  ReactDOM.render(<Provider store={store}><App /></Provider>, container);
  