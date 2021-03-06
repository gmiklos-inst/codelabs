import { SET_TODO_TEXT_INPUT, SET_TODO_FILTER_STATE, SET_TODOS, ADD_TODO, DELETE_TODO, TOGGLE_TODO, AppAction } from "../actions";
import { TodoItem } from "../model/todoItem";
import { TodoStatus } from "../components/TodoStatus";
import { TodoFilterState } from "../components/TodoFilter";
import { Action } from "redux";

export type AppState = {
    ui: {
        textInput: string;
        filterState: TodoFilterState;
    },
    todos: TodoItem[];
};

export const initialState: AppState = {
    ui: {
        textInput: '',
        filterState: TodoFilterState.ALL,
    },
    todos: [{
        id: "1",
        title: "item1",
    },
    {
        id: "2",
        title: "item2",
        completed: true,
    }],
};

export const appReducer = (state: AppState = initialState, action: AppAction): AppState => {
    switch (action.type) {
        case SET_TODO_TEXT_INPUT:
            return {...state, ui: {...state.ui, textInput: action.textInput}};
        case SET_TODO_FILTER_STATE:
            return {...state, ui: {...state.ui, filterState: action.filterState}};
        case ADD_TODO:
            return {
                ...state, 
                ui: {...state.ui, textInput: ''},
                todos: [action.todoItem, ...state.todos],
            };
        case TOGGLE_TODO:
            return {
                ...state,
                todos: state.todos.map((todo) => {
                    if (todo.id === action.id) {
                        return {...todo, completed: !todo.completed}
                    } else {
                        return todo;
                    }
                }),
            };
        case DELETE_TODO:
            return {
                ...state,
                todos: state.todos.filter(todo => todo.id !== action.id),
            };
        case SET_TODOS:
            return {
                ...state,
                todos: action.todos,
            };
        default:
            return state;
    }
};