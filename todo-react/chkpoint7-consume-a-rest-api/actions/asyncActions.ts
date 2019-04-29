import axios from 'axios';
import config from '../app-config.json';
import { setTodos, AppSyncAction, addTodoItem, setTodoTextInput, deleteTodoItem, toggleTodoItem } from '.';
import { ThunkAction } from 'redux-thunk';
import { AppState } from '../reducers/index.js';
import { TodoItem } from '../model/todoItem.js';

export const LOAD_TODOS = 'LOAD_TODOS';
export const ADD_TODO = 'ADD_TODO';
export const DELETE_TODO = 'DELETE_TODO';
export const TOGGLE_TODO = 'TOGGLE_TODO';

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
                dispatch(setTodos(response.data
                    .map(item => ({
                        id: item.id,
                        title: item.title,
                        completed: item.completed,
                        createdAt: item.created_at,
                    }))
                    .sort((i1: TodoItem, i2: TodoItem) => i1.createdAt < i2.createdAt ? 1 : -1)
                ));
            });
    };
};

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
};

export const deleteTodo = (id: string): AsyncAction => {
    return dispatch => {
        dispatch(deleteTodoItem(id));
        axios
            .delete(`${config.apiBaseUrl}/todos/${id}`, {
                headers: { ...AUTH },
            } as any);
    };
};

export const toggleTodo = (id: string): AsyncAction => {
    return (dispatch, getState) => {
        const { todos } = getState();
        dispatch(toggleTodoItem(id));
        axios
            .put(`${config.apiBaseUrl}/todos/${id}`, {
                completed: !todos.filter(todo => todo.id === id)[0].completed,
            }, {
                headers: { ...AUTH },
            } as any);
    };
};