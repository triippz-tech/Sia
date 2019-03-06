import { Moment } from 'moment';

export interface ITempBans {
  id?: number;
  guildId?: number;
  userId?: number;
  finish?: Moment;
}

export const defaultValue: Readonly<ITempBans> = {};
