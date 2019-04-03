import { IWelcomeMessage } from 'app/shared/model/welcome-message.model';
import { ICustomCommand } from 'app/shared/model/custom-command.model';

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
  customcommands?: ICustomCommand[];
}

export const defaultValue: Readonly<IGuildSettings> = {};
