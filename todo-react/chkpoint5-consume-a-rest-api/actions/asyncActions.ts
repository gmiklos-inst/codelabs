import axios from 'axios';
import config from '../app-config.json';
import {addTodo, deleteTodo, setTodos, setTodoTextInput, toggleTodo} from '.';
import {TodoItem} from '../model/todoItem';

const AUTH = {
    'x-api-key': config.apiKey,
};

export type AsyncAction = (dispatch, getState) => void;

export const loadTodosAsync = (): AsyncAction => {
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

export const addTodoAsync = (): AsyncAction => {
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
                dispatch(addTodo(response.data));
            });
    };
};

export const deleteTodoAsync = (id: string): AsyncAction => {
    return dispatch => {
        dispatch(deleteTodo(id));
        axios
            .delete(`${config.apiBaseUrl}/todos/${id}`, {
                headers: { ...AUTH },
            } as any);
    };
};

export const toggleTodoAsync = (id: string): AsyncAction => {
    return (dispatch, getState) => {
        const { todos } = getState();
        dispatch(toggleTodo(id));
        axios
            .put(`${config.apiBaseUrl}/todos/${id}`, {
                completed: !todos.filter(todo => todo.id === id)[0].completed,
            }, {
                headers: { ...AUTH },
            } as any);
    };
};