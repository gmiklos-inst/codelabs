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
import { createStore } from 'redux';
import { Provider, connect } from 'react-redux';
import { TodoInput } from './components/TodoInput';
import { TodoList } from './components/TodoList';
import { TodoStatus } from './components/TodoStatus';
import { TodoFilter, TodoFilterState } from './components/TodoFilter';
import { appReducer, AppState } from './reducers';
import { setTodoTextInput, addTodo, toggleTodo, deleteTodo, setTodoFilterState } from './actions';

class App extends Component<any> {
  render() {
    const { 
      todos, 
      setTodoTextInput, 
      setTodoFilterState, 
      addTodo, 
      toggleTodo, 
      textInput, 
      filterState, 
      deleteTodo 
    } = this.props;
    return (
      <div className="app">
        <h1>Hi!</h1>
        <TodoInput 
          onSubmit={() => addTodo()} 
          onChange={text => setTodoTextInput(text)} 
          value={textInput}
        />
        <TodoList 
          todoItems={todos.filter((todo) => {
            switch (filterState) {
              case TodoFilterState.COMPLETED:
                return todo.completed;
              case TodoFilterState.ACTIVE:
                return !todo.completed;
              default:
                return true;
            }
          })} 
          onToggleTodo={id => toggleTodo(id)} 
          onDeleteTodo={id => deleteTodo(id)} 
        />
        <TodoStatus itemLeftCount={todos.filter(todo => !todo.completed).length} />
        <TodoFilter
          todoFilterState={filterState} 
          onChange={filterState => setTodoFilterState(filterState)}
        />
      </div>
    )
  }
}

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

const mapStateToProps = (state: AppState) => {
  return {
    textInput: state.ui.textInput,
    filterState: state.ui.filterState,
    todos: state.todos,
  };
};

const mapDispatchToProps = dispatch => ({
  setTodoTextInput: text => dispatch(setTodoTextInput(text)),
  setTodoFilterState: id => dispatch(setTodoFilterState(id)),
  addTodo: () => dispatch(addTodo()),
  toggleTodo: id => dispatch(toggleTodo(id)),
  deleteTodo: id => dispatch(deleteTodo(id)),
});

const ConnectedApp = connect(mapStateToProps, mapDispatchToProps)(App);

ReactDOM.render(<Provider store={store}><ConnectedApp /></Provider>, container);
