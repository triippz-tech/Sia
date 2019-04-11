import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GuildEvent from './guild-event';
import GuildEventDetail from './guild-event-detail';
import GuildEventUpdate from './guild-event-update';
import GuildEventDeleteDialog from './guild-event-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GuildEventUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GuildEventUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GuildEventDetail} />
      <ErrorBoundaryRoute path={match.url} component={GuildEvent} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={GuildEventDeleteDialog} />
  </>
);

export default Routes;
