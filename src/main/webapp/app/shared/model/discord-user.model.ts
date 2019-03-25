import { IPoll } from 'app/shared/model/poll.model';

export interface IDiscordUser {
  id?: number;
  userId?: number;
  commandsIssued?: number;
  blacklisted?: boolean;
  polls?: IPoll[];
}

export const defaultValue: Readonly<IDiscordUser> = {
  blacklisted: false
};
