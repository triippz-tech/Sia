import { Moment } from 'moment';

export interface IPremium {
  id?: number;
  discordId?: number;
  until?: Moment;
  level?: number;
}

export const defaultValue: Readonly<IPremium> = {};
