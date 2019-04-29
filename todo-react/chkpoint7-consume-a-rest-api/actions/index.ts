import { TodoFilterState } from "../components/TodoFilter";
import { AsyncAction } from "./asyncActions";
import { TodoItem } from "../model/todoItem";

export const SET_TODO_TEXT_INPUT = 'SET_TODO_TEXT_INPUT';
export const SET_TODO_FILTER_STATE = 'SET_TODO_FILTER_STATE';
export const ADD_TODO_ITEM = 'ADD_TODO_ITEM';
export const TOGGLE_TODO_ITEM = 'TOGGLE_TODO_ITEM';
export const DELETE_TODO_ITEM = 'DELETE_TODO_ITEM';

export const SET_TODOS = 'SET_TODOS';

export { loadTodos, addTodo, deleteTodo, toggleTodo } from './asyncActions';

type SetTodoTextInputAction = {
    type: typeof SET_TODO_TEXT_INPUT;
    textInput: string;
};

type SetTodoFilterStateAction = {
    type: typeof SET_TODO_FILTER_STATE;
    filterState: TodoFilterState;
};

type AddTodoItemAction = {
    type: typeof ADD_TODO_ITEM;
    todoItem: TodoItem;
};

type ToggleTodoItemAction = {
    type: typeof TOGGLE_TODO_ITEM;
    id: string;
};

type DeleteTodoItemAction = {
    type: typeof DELETE_TODO_ITEM;
    id: string;
};

type SetTodosAction = {
    type: typeof SET_TODOS;
    todos: TodoItem[];
};

export type AppSyncAction = 
    SetTodoTextInputAction | 
    SetTodoFilterStateAction | 
    AddTodoItemAction | 
    ToggleTodoItemAction | 
    DeleteTodoItemAction |
    SetTodosAction;

export type AppAction = 
    AppSyncAction |
    AsyncAction;

export const setTodoTextInput = (textInput: string): SetTodoTextInputAction => ({
    type: SET_TODO_TEXT_INPUT,
    textInput,
});

export const setTodoFilterState = (filterState: TodoFilterState): SetTodoFilterStateAction => ({
    type: SET_TODO_FILTER_STATE,
    filterState,
});

export const addTodoItem = (todoItem: TodoItem): AddTodoItemAction => ({
    type: ADD_TODO_ITEM,
    todoItem,
});

export const toggleTodoItem = (id: string): ToggleTodoItemAction => ({
    type: TOGGLE_TODO_ITEM,
    id,
});

export const deleteTodoItem = (id: string): DeleteTodoItemAction => ({
    type: DELETE_TODO_ITEM,
    id,
});

export const setTodos = (todos: TodoItem[]): SetTodosAction => ({
    type: SET_TODOS,
    todos,
});