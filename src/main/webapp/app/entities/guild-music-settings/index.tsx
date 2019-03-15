import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GuildMusicSettings from './guild-music-settings';
import GuildMusicSettingsDetail from './guild-music-settings-detail';
import GuildMusicSettingsUpdate from './guild-music-settings-update';
import GuildMusicSettingsDeleteDialog from './guild-music-settings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GuildMusicSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GuildMusicSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GuildMusicSettingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={GuildMusicSettings} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={GuildMusicSettingsDeleteDialog} />
  </>
);

export default Routes;
