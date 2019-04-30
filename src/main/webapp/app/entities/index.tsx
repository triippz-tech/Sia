import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AuditCache from './audit-cache';
import AutoMod from './auto-mod';
import GuildSettings from './guild-settings';
import Ignored from './ignored';
import Premium from './premium';
import Actions from './actions';
import Strikes from './strikes';
import TempMutes from './temp-mutes';
import TempBans from './temp-bans';
import GuildMusicSettings from './guild-music-settings';
import Playlist from './playlist';
import Songs from './songs';
import Poll from './poll';
import PollItems from './poll-items';
import DiscordUser from './discord-user';
import WelcomeMessage from './welcome-message';
import GuildRoles from './guild-roles';
import CustomCommand from './custom-command';
import GuildEvent from './guild-event';
import GiveAway from './give-away';
import DiscordGuild from './discord-guild';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/audit-cache`} component={AuditCache} />
      <ErrorBoundaryRoute path={`${match.url}/auto-mod`} component={AutoMod} />
      <ErrorBoundaryRoute path={`${match.url}/guild-settings`} component={GuildSettings} />
      <ErrorBoundaryRoute path={`${match.url}/ignored`} component={Ignored} />
      <ErrorBoundaryRoute path={`${match.url}/premium`} component={Premium} />
      <ErrorBoundaryRoute path={`${match.url}/actions`} component={Actions} />
      <ErrorBoundaryRoute path={`${match.url}/strikes`} component={Strikes} />
      <ErrorBoundaryRoute path={`${match.url}/temp-mutes`} component={TempMutes} />
      <ErrorBoundaryRoute path={`${match.url}/temp-bans`} component={TempBans} />
      <ErrorBoundaryRoute path={`${match.url}/guild-music-settings`} component={GuildMusicSettings} />
      <ErrorBoundaryRoute path={`${match.url}/playlist`} component={Playlist} />
      <ErrorBoundaryRoute path={`${match.url}/songs`} component={Songs} />
      <ErrorBoundaryRoute path={`${match.url}/poll`} component={Poll} />
      <ErrorBoundaryRoute path={`${match.url}/poll-items`} component={PollItems} />
      <ErrorBoundaryRoute path={`${match.url}/discord-user`} component={DiscordUser} />
      <ErrorBoundaryRoute path={`${match.url}/welcome-message`} component={WelcomeMessage} />
      <ErrorBoundaryRoute path={`${match.url}/guild-roles`} component={GuildRoles} />
      <ErrorBoundaryRoute path={`${match.url}/custom-command`} component={CustomCommand} />
      <ErrorBoundaryRoute path={`${match.url}/guild-event`} component={GuildEvent} />
      <ErrorBoundaryRoute path={`${match.url}/give-away`} component={GiveAway} />
      <ErrorBoundaryRoute path={`${match.url}/discord-guild`} component={DiscordGuild} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
