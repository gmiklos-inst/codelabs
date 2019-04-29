import { createStore } from 'redux';
import {appReducer, AppState} from "../reducers";

export const loadState = (): AppState | undefined => {
    try {
        const serializedState = localStorage.getItem('state');
        if (serializedState === null) {
            return undefined;
        }
        return JSON.parse(serializedState);
    } catch (err) {
        return undefined;
    }
};

export const saveState = (state: AppState) => {
    try {
        const serializedState = JSON.stringify(state);
        localStorage.setItem('state', serializedState);
    } catch {
        // ignore errors
    }
};

export const createAppStore = (initialState: AppState) => createStore(appReducer, initialState);

export const createPersistedAppStore = () => {
    const store = createAppStore(loadState());

    store.subscribe(() => {
        saveState(store.getState());
    });

    return store;
};