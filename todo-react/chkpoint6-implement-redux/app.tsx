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
import { setTodoTextInput, addTodo, toggleTodo } from './actions';

class App extends Component<any> {
  render() {
    const {todos, setTodoTextInput, addTodo, toggleTodo, textInput } = this.props;
    return (
      <div className="app">
        <h1>Hi!</h1>
        <TodoInput 
          onSubmit={() => addTodo()} 
          onChange={(text) => setTodoTextInput(text)} 
          value={textInput}
        />
        <TodoList todoItems={todos} onToggleTodo={(id) => toggleTodo(id)} />
        <TodoStatus itemLeftCount={todos.filter(todo => !todo.completed).length} />
        <TodoFilter todoFilterState={TodoFilterState.ALL} />
      </div>
    )
  }
}

const store = createStore(appReducer);
const container = document.getElementById('app-container');

const mapStateToProps = (state: AppState) => {
  return {
    textInput: state.ui.textInput,
    todos: state.todos,
  };
};

const mapDispatchToProps = dispatch => ({
  setTodoTextInput: text => dispatch(setTodoTextInput(text)),
  addTodo: () => dispatch(addTodo()),
  toggleTodo: id => dispatch(toggleTodo(id)),
});

const ConnectedApp = connect(mapStateToProps, mapDispatchToProps)(App);

ReactDOM.render(<Provider store={store}><ConnectedApp /></Provider>, container);
