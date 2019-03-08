import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from './user-management';
// prettier-ignore
import auditCache, {
  AuditCacheState
} from 'app/entities/audit-cache/audit-cache.reducer';
// prettier-ignore
import autoMod, {
  AutoModState
} from 'app/entities/auto-mod/auto-mod.reducer';
// prettier-ignore
import guildSettings, {
  GuildSettingsState
} from 'app/entities/guild-settings/guild-settings.reducer';
// prettier-ignore
import ignored, {
  IgnoredState
} from 'app/entities/ignored/ignored.reducer';
// prettier-ignore
import premium, {
  PremiumState
} from 'app/entities/premium/premium.reducer';
// prettier-ignore
import actions, {
  ActionsState
} from 'app/entities/actions/actions.reducer';
// prettier-ignore
import strikes, {
  StrikesState
} from 'app/entities/strikes/strikes.reducer';
// prettier-ignore
import tempMutes, {
  TempMutesState
} from 'app/entities/temp-mutes/temp-mutes.reducer';
// prettier-ignore
import tempBans, {
  TempBansState
} from 'app/entities/temp-bans/temp-bans.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly auditCache: AuditCacheState;
  readonly autoMod: AutoModState;
  readonly guildSettings: GuildSettingsState;
  readonly ignored: IgnoredState;
  readonly premium: PremiumState;
  readonly actions: ActionsState;
  readonly strikes: StrikesState;
  readonly tempMutes: TempMutesState;
  readonly tempBans: TempBansState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  auditCache,
  autoMod,
  guildSettings,
  ignored,
  premium,
  actions,
  strikes,
  tempMutes,
  tempBans,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
