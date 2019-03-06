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
}

export const defaultValue: Readonly<IGuildSettings> = {};
