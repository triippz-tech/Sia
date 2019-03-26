import { IPoll } from 'app/shared/model/poll.model';
import { IPollItems } from 'app/shared/model/poll-items.model';

export interface IDiscordUser {
  id?: number;
  userId?: number;
  commandsIssued?: number;
  blacklisted?: boolean;
  polls?: IPoll[];
  pollitems?: IPollItems[];
}

export const defaultValue: Readonly<IDiscordUser> = {
  blacklisted: false
};
