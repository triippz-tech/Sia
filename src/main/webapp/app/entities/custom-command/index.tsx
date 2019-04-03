import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CustomCommand from './custom-command';
import CustomCommandDetail from './custom-command-detail';
import CustomCommandUpdate from './custom-command-update';
import CustomCommandDeleteDialog from './custom-command-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CustomCommandUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CustomCommandUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CustomCommandDetail} />
      <ErrorBoundaryRoute path={match.url} component={CustomCommand} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CustomCommandDeleteDialog} />
  </>
);

export default Routes;
