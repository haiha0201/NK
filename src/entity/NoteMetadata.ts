import {NoteStyle} from './NoteStyle';

export interface NoteMetadata {
  createdDate: number;
  lastEdited: number;
  lastSynced: number | undefined;
  styling: NoteStyle;
  id: number;
}
