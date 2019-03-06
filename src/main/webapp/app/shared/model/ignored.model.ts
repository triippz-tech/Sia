export interface IIgnored {
  id?: number;
  guildId?: number;
  entityId?: number;
  type?: number;
}

export const defaultValue: Readonly<IIgnored> = {};
