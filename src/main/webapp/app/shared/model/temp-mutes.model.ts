import { Moment } from 'moment';

export interface ITempMutes {
  id?: number;
  guildId?: number;
  userId?: number;
  finish?: Moment;
}

export const defaultValue: Readonly<ITempMutes> = {};
