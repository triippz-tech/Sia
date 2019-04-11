import { Moment } from 'moment';
import { IGuildSettings } from 'app/shared/model/guild-settings.model';

export interface IGuildEvent {
  id?: number;
  eventName?: string;
  eventImageUrl?: string;
  eventMessage?: any;
  eventStart?: Moment;
  expired?: boolean;
  guildsettings?: IGuildSettings;
}

export const defaultValue: Readonly<IGuildEvent> = {};
