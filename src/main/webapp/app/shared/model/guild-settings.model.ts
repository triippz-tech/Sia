import { IWelcomeMessage } from 'app/shared/model/welcome-message.model';
import { ICustomCommand } from 'app/shared/model/custom-command.model';
import { IGuildEvent } from 'app/shared/model/guild-event.model';
import { IGiveAway } from 'app/shared/model/give-away.model';

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
  guildevents?: IGuildEvent[];
  giveaways?: IGiveAway[];
}

export const defaultValue: Readonly<IGuildSettings> = {};
