import { TodoFilterState } from "../components/TodoFilter";
import { AsyncAction } from "./asyncActions";
import { TodoItem } from "../model/todoItem";

export const SET_TODO_TEXT_INPUT = 'SET_TODO_TEXT_INPUT';
export const SET_TODO_FILTER_STATE = 'SET_TODO_FILTER_STATE';
export const ADD_TODO = 'ADD_TODO';
export const TOGGLE_TODO = 'TOGGLE_TODO';
export const DELETE_TODO = 'DELETE_TODO';

export const SET_TODOS = 'SET_TODOS';

export { loadTodosAsync, addTodoAsync, deleteTodoAsync, toggleTodoAsync } from './asyncActions';

type SetTodoTextInputAction = {
    type: typeof SET_TODO_TEXT_INPUT;
    textInput: string;
};

type SetTodoFilterStateAction = {
    type: typeof SET_TODO_FILTER_STATE;
    filterState: TodoFilterState;
};

type AddTodoAction = {
    type: typeof ADD_TODO;
    todoItem: TodoItem;
};

type ToggleTodoAction = {
    type: typeof TOGGLE_TODO;
    id: string;
};

type DeleteTodoAction = {
    type: typeof DELETE_TODO;
    id: string;
};

type SetTodosAction = {
    type: typeof SET_TODOS;
    todos: TodoItem[];
};

export type AppAction =
    SetTodoTextInputAction |
    SetTodoFilterStateAction |
    AddTodoAction |
    ToggleTodoAction |
    DeleteTodoAction |
    SetTodosAction;

export const setTodoTextInput = (textInput: string): AppAction => ({
    type: SET_TODO_TEXT_INPUT,
    textInput,
});

export const setTodoFilterState = (filterState: TodoFilterState): AppAction => ({
    type: SET_TODO_FILTER_STATE,
    filterState,
});

export const addTodo = (todoItem: TodoItem): AppAction => ({
    type: ADD_TODO,
    todoItem,
});

export const toggleTodo = (id: string): AppAction => ({
    type: TOGGLE_TODO,
    id,
});

export const deleteTodo = (id: string): AppAction => ({
    type: DELETE_TODO,
    id,
});

export const setTodos = (todos: TodoItem[]): AppAction => ({
    type: SET_TODOS,
    todos,
});