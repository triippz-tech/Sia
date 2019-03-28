import { IWelcomeMessage } from 'app/shared/model/welcome-message.model';

export interface IGuildSettings {
  id?: number;
  guildId?: number;
  modRoleId?: number;
  modLogId?: number;
  serverLogId?: number;
  messageLogId?: number;
  voiceLogId?: number;
  avatarLogId?: number;
  prefix?: string;
  timezone?: string;
  raidMode?: number;
  muteRole?: number;
  welcomemessages?: IWelcomeMessage[];
}

export const defaultValue: Readonly<IGuildSettings> = {};
