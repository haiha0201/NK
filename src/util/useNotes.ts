import axios from 'axios';
import {useQuery} from 'react-query';
import {url} from './handleAuthentication';
import {NoteInterface} from '../entity/NoteInterface';

export const useNotes = () => {
  return useQuery<NoteInterface[]>('notes', () =>
    axios.get(`${url}/api/notes`).then(value => value.data),
  );
};
