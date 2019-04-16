import { Moment } from 'moment';
import { IDiscordUser } from 'app/shared/model/discord-user.model';
import { IGuildSettings } from 'app/shared/model/guild-settings.model';

export interface IGiveAway {
  id?: number;
  name?: string;
  message?: any;
  messageId?: number;
  textChannelId?: number;
  finish?: Moment;
  winner?: number;
  expired?: boolean;
  discordUsers?: IDiscordUser[];
  guildsettings?: IGuildSettings;
}

export const defaultValue: Readonly<IGiveAway> = {
  expired: false
};
