import { IGuildSettings } from 'app/shared/model/guild-settings.model';
import { IAuditCache } from 'app/shared/model/audit-cache.model';
import { IAutoMod } from 'app/shared/model/auto-mod.model';
import { IGuildMusicSettings } from 'app/shared/model/guild-music-settings.model';
import { IDiscordUser } from 'app/shared/model/discord-user.model';
import { IIgnored } from 'app/shared/model/ignored.model';
import { ITempMutes } from 'app/shared/model/temp-mutes.model';
import { ITempBans } from 'app/shared/model/temp-bans.model';
import { IPoll } from 'app/shared/model/poll.model';
import { IGuildRoles } from 'app/shared/model/guild-roles.model';
import { ICustomCommand } from 'app/shared/model/custom-command.model';
import { IActions } from 'app/shared/model/actions.model';
import { IStrikes } from 'app/shared/model/strikes.model';
import { IWelcomeMessage } from 'app/shared/model/welcome-message.model';
import { IGuildEvent } from 'app/shared/model/guild-event.model';
import { IGiveAway } from 'app/shared/model/give-away.model';

export interface IDiscordGuild {
  id?: number;
  guildId?: number;
  guildName?: string;
  inviteLink?: string;
  guildSettings?: IGuildSettings;
  auditCache?: IAuditCache;
  autoMod?: IAutoMod;
  guildMusicSettings?: IGuildMusicSettings;
  discordUsers?: IDiscordUser[];
  ignoreds?: IIgnored[];
  tempMutes?: ITempMutes[];
  tempBans?: ITempBans[];
  polls?: IPoll[];
  guildRoles?: IGuildRoles[];
  customCommands?: ICustomCommand[];
  actions?: IActions[];
  strikes?: IStrikes[];
  welcomeMessages?: IWelcomeMessage[];
  guildEvents?: IGuildEvent[];
  giveAways?: IGiveAway[];
}

export const defaultValue: Readonly<IDiscordGuild> = {};
