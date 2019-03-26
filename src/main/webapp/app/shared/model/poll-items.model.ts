import { IPoll } from 'app/shared/model/poll.model';
import { IDiscordUser } from 'app/shared/model/discord-user.model';

export interface IPollItems {
  id?: number;
  itemName?: string;
  reaction?: string;
  votes?: number;
  discordusers?: IDiscordUser[];
  poll?: IPoll;
}

export const defaultValue: Readonly<IPollItems> = {};
