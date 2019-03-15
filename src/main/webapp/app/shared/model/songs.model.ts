import { IPlaylist } from 'app/shared/model/playlist.model';

export interface ISongs {
  id?: number;
  songName?: string;
  songQuery?: string;
  playlist?: IPlaylist;
}

export const defaultValue: Readonly<ISongs> = {};
