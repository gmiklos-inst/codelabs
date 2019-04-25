export const SET_TODO_TEXT_INPUT = 'SET_TODO_TEXT_INPUT';
export const ADD_TODO = 'ADD_TODO';
export const TOGGLE_TODO = 'TOGGLE_TODO';

type SetTodoTextInputAction = {
    type: typeof SET_TODO_TEXT_INPUT;
    textInput: string;
};

type AddTodoAction = {
    type: typeof ADD_TODO;
};

type ToggleTodoAction = {
    type: typeof TOGGLE_TODO;
    id: string;
};

export type AppAction = SetTodoTextInputAction | AddTodoAction | ToggleTodoAction;

export const setTodoTextInput = (textInput: string): AppAction => ({
    type: SET_TODO_TEXT_INPUT,
    textInput,
})

export const addTodo = (): AppAction => ({
    type: ADD_TODO,
});

export const toggleTodo = (id: string): AppAction => ({
    type: TOGGLE_TODO,
    id,
});