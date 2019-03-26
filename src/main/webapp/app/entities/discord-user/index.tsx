import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DiscordUser from './discord-user';
import DiscordUserDetail from './discord-user-detail';
import DiscordUserUpdate from './discord-user-update';
import DiscordUserDeleteDialog from './discord-user-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DiscordUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DiscordUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DiscordUserDetail} />
      <ErrorBoundaryRoute path={match.url} component={DiscordUser} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={DiscordUserDeleteDialog} />
  </>
);

export default Routes;
