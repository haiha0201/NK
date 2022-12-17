import {Authentication} from '../entity/Authentication';
import axios from 'axios';
import store from '../store/store';
import {login, logout} from '../slice/authenticateSlice';
import {NoteInterface} from '../entity/NoteInterface';

export const url = 'http://10.0.2.2:8500';

export function handleLogin(data: Authentication) {
  axios
    .post<string, string>(`${url}/auth/login`, data)
    .then(() => store.dispatch(login()))
    .catch(console.log);
}

export function handleLogout() {
  axios
    .post<string, string>(`${url}/auth/logout`)
    .then(() => store.dispatch(logout()))
    .catch(console.log);
}

export function getNotes() {
  axios.get(`${url}/api/images`).then(value => console.log(value.data));
}

export async function uploadImages(
  uri: string | undefined,
  name: string | undefined,
  type: string | undefined,
): Promise<string> {
  const data = new FormData();
  data.append('file', {uri: uri, name: name, type: type});
  return axios.post<FormData, string>(`${url}/api/images`, data, {
    headers: {'Content-Type': 'multipart/form-data'},
  });
}

export async function postNote(data: NoteInterface) {
  return axios.post<NoteInterface, void>(`${url}/api/notes`, data);
}
