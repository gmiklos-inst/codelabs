import axios from 'axios';
import config from '../app-config.json';
import { setTodos } from '.';
export const LOAD_TODOS = 'LOAD_TODOS';

type LoadTodosAction = (dispatch) => void;

export type AsyncAction = LoadTodosAction;

const AUTH = {
    Authentication: config.apiKey,
}

export const loadTodos = (): LoadTodosAction => {
    return dispatch => {
        axios
            .get(`${config.apiBaseUrl}/todos/`, {
                headers: {...AUTH},
            } as any)
            .then((response) => {
                dispatch(setTodos(response.data));
            });
    };
}