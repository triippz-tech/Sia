import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Actions from './actions';
import ActionsDetail from './actions-detail';
import ActionsUpdate from './actions-update';
import ActionsDeleteDialog from './actions-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ActionsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ActionsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ActionsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Actions} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ActionsDeleteDialog} />
  </>
);

export default Routes;
