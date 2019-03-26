import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Poll from './poll';
import PollDetail from './poll-detail';
import PollUpdate from './poll-update';
import PollDeleteDialog from './poll-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PollUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PollUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PollDetail} />
      <ErrorBoundaryRoute path={match.url} component={Poll} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PollDeleteDialog} />
  </>
);

export default Routes;
