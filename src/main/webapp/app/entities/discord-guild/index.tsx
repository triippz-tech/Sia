import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DiscordGuild from './discord-guild';
import DiscordGuildDetail from './discord-guild-detail';
import DiscordGuildUpdate from './discord-guild-update';
import DiscordGuildDeleteDialog from './discord-guild-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DiscordGuildUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DiscordGuildUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DiscordGuildDetail} />
      <ErrorBoundaryRoute path={match.url} component={DiscordGuild} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={DiscordGuildDeleteDialog} />
  </>
);

export default Routes;
