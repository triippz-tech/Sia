import { IPoll } from 'app/shared/model/poll.model';

export interface IPollItems {
  id?: number;
  itemName?: string;
  reaction?: string;
  votes?: number;
  poll?: IPoll;
}

export const defaultValue: Readonly<IPollItems> = {};
