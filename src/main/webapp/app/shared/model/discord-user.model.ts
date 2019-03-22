export interface IDiscordUser {
  id?: number;
  userId?: number;
  commandsIssued?: number;
  blacklisted?: boolean;
}

export const defaultValue: Readonly<IDiscordUser> = {
  blacklisted: false
};
