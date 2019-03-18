import { ISongs } from 'app/shared/model/songs.model';
import { IGuildMusicSettings } from 'app/shared/model/guild-music-settings.model';

export interface IPlaylist {
  id?: number;
  guildId?: number;
  userId?: number;
  playlistName?: string;
  songs?: ISongs[];
  guildmusicsettings?: IGuildMusicSettings;
}

export const defaultValue: Readonly<IPlaylist> = {};
