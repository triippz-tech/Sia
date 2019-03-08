export interface IStrikes {
  id?: number;
  guildId?: number;
  userId?: number;
  strikes?: number;
}

export const defaultValue: Readonly<IStrikes> = {};
