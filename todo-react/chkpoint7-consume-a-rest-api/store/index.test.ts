import 'jest-dom/extend-expect';

import {addTodo, deleteTodo, setTodoFilterState, setTodoTextInput, toggleTodo} from '../actions'
import {createAppStore} from '.';
import {TodoFilterState} from "../components/TodoFilter";
import {AppState} from "../reducers";

const testState = (expandWith: any): AppState => ({
    ui: {
        textInput: '',
        filterState: TodoFilterState.ALL,
    },
    todos: [],
    lastId: 0,
    ...expandWith
});

describe('AppStore', () => {

    it('setTodoTextInput sets text input state correctly', async () => {
        const store = createAppStore(testState({
            ui: {
                textInput: "original",
            }
        }));

        expect(store.getState().ui.textInput).toBe("original");

        store.dispatch(setTodoTextInput("barackfa"));

        expect(store.getState().ui.textInput).toBe("barackfa");
    });

    it('setTodoFilterState set filter state correctly', async () => {
        const store = createAppStore(testState({
            ui: {
                filterState: TodoFilterState.ALL
            }
        }));

        expect(store.getState().ui.filterState).toBe(TodoFilterState.ALL);

        store.dispatch(setTodoFilterState(TodoFilterState.ACTIVE));

        expect(store.getState().ui.filterState).toBe(TodoFilterState.ACTIVE);
    });

    it('addTodo adds item correctly', async () => {
        const store = createAppStore(testState({
            todos: []
        }));

        expect(store.getState().todos).toHaveLength(0);

        store.dispatch(setTodoTextInput("barackfa"));
        store.dispatch(addTodo());

        expect(store.getState().todos).toHaveLength(1);
        expect(store.getState().todos[0]).toEqual({
            id: "1",
            title: "barackfa",
            completed: false
        });
    });

    it('toggleTodo toggles item correctly', async () => {
        const store = createAppStore(testState({
            todos: [{
                id: "1",
                title: "item",
                completed: false
            }]
        }));

        expect(store.getState().todos[0].completed).toBeFalsy();

        store.dispatch(toggleTodo("1"));

        expect(store.getState().todos[0].completed).toBeTruthy();
    });

    it('deleteTodo deletes item correctly', async () => {
        const store = createAppStore(testState({
            todos: [{
                id: "1",
                title: "item",
                completed: false
            }]
        }));

        expect(store.getState().todos).toHaveLength(1);

        store.dispatch(deleteTodo("1"));

        expect(store.getState().todos).toHaveLength(0);
    });

});