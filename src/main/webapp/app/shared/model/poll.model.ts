import { Moment } from 'moment';
import { IPollItems } from 'app/shared/model/poll-items.model';

export interface IPoll {
  id?: number;
  guildId?: number;
  userId?: number;
  textChannelId?: number;
  messageId?: number;
  title?: string;
  finishTime?: Moment;
  expired?: boolean;
  pollitems?: IPollItems[];
}

export const defaultValue: Readonly<IPoll> = {};
