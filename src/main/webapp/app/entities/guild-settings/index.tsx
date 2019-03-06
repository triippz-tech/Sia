import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GuildSettings from './guild-settings';
import GuildSettingsDetail from './guild-settings-detail';
import GuildSettingsUpdate from './guild-settings-update';
import GuildSettingsDeleteDialog from './guild-settings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GuildSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GuildSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GuildSettingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={GuildSettings} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={GuildSettingsDeleteDialog} />
  </>
);

export default Routes;
