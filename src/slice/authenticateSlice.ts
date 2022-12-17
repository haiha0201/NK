import {createSlice} from '@reduxjs/toolkit';

interface AuthenticationState {
  isLogin: boolean;
}

const initialState: AuthenticationState = {
  isLogin: false,
};
export const authenticateSlice = createSlice({
  name: 'authentication',
  initialState: initialState,
  reducers: {
    login(state) {
      state.isLogin = true;
    },
    logout(state) {
      state.isLogin = false;
    },
  },
});

export const {login, logout} = authenticateSlice.actions;
export default authenticateSlice.reducer;
