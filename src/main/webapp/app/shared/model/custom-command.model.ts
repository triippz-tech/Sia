import { IGuildRoles } from 'app/shared/model/guild-roles.model';
import { IGuildSettings } from 'app/shared/model/guild-settings.model';

export interface ICustomCommand {
  id?: number;
  guildId?: number;
  commandName?: string;
  message?: any;
  guildroles?: IGuildRoles[];
  guildsettings?: IGuildSettings;
}

export const defaultValue: Readonly<ICustomCommand> = {};
