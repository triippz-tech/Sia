import { Moment } from 'moment';

export interface IPremium {
  id?: number;
  guildId?: number;
  until?: Moment;
  level?: number;
}

export const defaultValue: Readonly<IPremium> = {};
