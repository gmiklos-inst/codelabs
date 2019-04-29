import { createStore } from 'redux';
import {appReducer, AppState} from "../reducers";

export const createAppStore = (initialState: AppState) => createStore(appReducer, initialState);