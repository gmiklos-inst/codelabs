import React, { Component } from 'react';
import { TodoInput } from './components/TodoInput';
import { TodoList } from './components/TodoList';
import { TodoStatus } from './components/TodoStatus';
import { TodoFilter, TodoFilterState } from './components/TodoFilter';

import { setTodoTextInput, addTodoAsync, toggleTodoAsync, deleteTodoAsync, setTodoFilterState, loadTodosAsync } from './actions';
import {TodoItem} from "./model/TodoItem";

import { connect } from 'react-redux';
import { AppState } from './reducers';

type AppProps = {
    todos: TodoItem[],
    textInput: string,
    filterState: TodoFilterState,
    setTodoTextInput: (string) => void,
    setTodoFilterState: (TodoFilterState) => void,
    addTodo: () => void,
    toggleTodo: (string) => void,
    deleteTodo: (string) => void,
    loadTodos: () => void,
};

class AppComponent extends Component<AppProps> {

  componentDidMount() {
    const { loadTodos } = this.props;
    loadTodos();
  }

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
  addTodo: () => dispatch(addTodoAsync()),
  toggleTodo: id => dispatch(toggleTodoAsync(id)),
  deleteTodo: id => dispatch(deleteTodoAsync(id)),
  loadTodos: () => dispatch(loadTodosAsync()),
});

export const App = connect(mapStateToProps, mapDispatchToProps)(AppComponent);
