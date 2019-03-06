export interface IActions {
  id?: number;
  guildId?: number;
  numStrikes?: number;
  action?: number;
  time?: number;
}

export const defaultValue: Readonly<IActions> = {};
