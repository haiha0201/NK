import {combineReducers, createStore} from '@reduxjs/toolkit';
import {authenticateSlice} from '../slice/authenticateSlice';

const reducer = combineReducers({
  authentication: authenticateSlice.reducer,
});

const store = createStore(reducer);
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export default store;
