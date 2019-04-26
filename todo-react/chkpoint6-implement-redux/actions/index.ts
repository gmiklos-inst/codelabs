import { TodoFilterState } from "../components/TodoFilter";

export const SET_TODO_TEXT_INPUT = 'SET_TODO_TEXT_INPUT';
export const SET_TODO_FILTER_STATE = 'SET_TODO_FILTER_STATE';
export const ADD_TODO = 'ADD_TODO';
export const TOGGLE_TODO = 'TOGGLE_TODO';
export const DELETE_TODO = 'DELETE_TODO';

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
};

type ToggleTodoAction = {
    type: typeof TOGGLE_TODO;
    id: string;
};

type DeleteTodoAction = {
    type: typeof DELETE_TODO;
    id: string;
};

export type AppAction = 
    SetTodoTextInputAction | 
    SetTodoFilterStateAction | 
    AddTodoAction | 
    ToggleTodoAction | 
    DeleteTodoAction;

export const setTodoTextInput = (textInput: string): AppAction => ({
    type: SET_TODO_TEXT_INPUT,
    textInput,
})

export const setTodoFilterState = (filterState: TodoFilterState): AppAction => ({
    type: SET_TODO_FILTER_STATE,
    filterState,
})

export const addTodo = (): AppAction => ({
    type: ADD_TODO,
});

export const toggleTodo = (id: string): AppAction => ({
    type: TOGGLE_TODO,
    id,
});

export const deleteTodo = (id: string): AppAction => ({
    type: DELETE_TODO,
    id,
});