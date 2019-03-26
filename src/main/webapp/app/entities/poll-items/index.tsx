import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PollItems from './poll-items';
import PollItemsDetail from './poll-items-detail';
import PollItemsUpdate from './poll-items-update';
import PollItemsDeleteDialog from './poll-items-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PollItemsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PollItemsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PollItemsDetail} />
      <ErrorBoundaryRoute path={match.url} component={PollItems} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PollItemsDeleteDialog} />
  </>
);

export default Routes;
