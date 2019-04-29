import axios from 'axios';
import config from '../app-config.json';
import { setTodos, AppSyncAction, addTodoItem, setTodoTextInput, deleteTodoItem } from '.';
import { ThunkAction } from 'redux-thunk';
import { AppState } from '../reducers/index.js';

export const LOAD_TODOS = 'LOAD_TODOS';
export const ADD_TODO = 'ADD_TODO';
export const DELETE_TODO = 'DELETE_TODO';

export type AsyncAction = ThunkAction<void, AppState, {}, AppSyncAction>;

const AUTH = {
    'x-api-key': config.apiKey,
}

export const loadTodos = (): AsyncAction => {
    return dispatch => {
        dispatch(setTodoTextInput(''));
        axios
            .get(`${config.apiBaseUrl}/todos/?page_size=1000`, {
                headers: { ...AUTH },
            } as any)
            .then((response) => {
                dispatch(setTodos(response.data));
            });
    };
}

export const addTodo = (): AsyncAction => {
    return (dispatch, getState) => {
        const { ui } = getState();
        axios
            .post(`${config.apiBaseUrl}/todos/`, {
                title: ui.textInput,
                completed: false,
            },{
                headers: { ...AUTH },
            } as any)
            .then((response) => {
                dispatch(addTodoItem(response.data));
            });
    };
}

export const deleteTodo = (id: string): AsyncAction => {
    return (dispatch, getState) => {
        dispatch(deleteTodoItem(id));
        axios
            .delete(`${config.apiBaseUrl}/todos/${id}`, {
                headers: { ...AUTH },
            } as any);
    };
}