import { IPlaylist } from 'app/shared/model/playlist.model';

export interface IGuildMusicSettings {
  id?: number;
  guildId?: number;
  textChannelId?: number;
  voiceChannelId?: number;
  djRoleId?: number;
  volume?: number;
  repeat?: boolean;
  stayInChannel?: boolean;
  songInGame?: boolean;
  nowPlayingImages?: boolean;
  maxSeconds?: number;
  playlist?: IPlaylist;
}

export const defaultValue: Readonly<IGuildMusicSettings> = {
  repeat: false
};
