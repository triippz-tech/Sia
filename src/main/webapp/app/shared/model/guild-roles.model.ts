import { ICustomCommand } from 'app/shared/model/custom-command.model';

export interface IGuildRoles {
  id?: number;
  guildId?: number;
  roleId?: number;
  roleName?: string;
  customcommand?: ICustomCommand;
}

export const defaultValue: Readonly<IGuildRoles> = {};
