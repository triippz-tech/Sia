import { IGuildSettings } from 'app/shared/model/guild-settings.model';

export interface IWelcomeMessage {
  id?: number;
  name?: string;
  messageTitle?: string;
  body?: any;
  footer?: string;
  websiteUrl?: string;
  logoUrl?: string;
  active?: boolean;
  guildsettings?: IGuildSettings;
}

export const defaultValue: Readonly<IWelcomeMessage> = {
  active: false
};
