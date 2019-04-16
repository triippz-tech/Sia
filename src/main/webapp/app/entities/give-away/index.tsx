import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GiveAway from './give-away';
import GiveAwayDetail from './give-away-detail';
import GiveAwayUpdate from './give-away-update';
import GiveAwayDeleteDialog from './give-away-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GiveAwayUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GiveAwayUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GiveAwayDetail} />
      <ErrorBoundaryRoute path={match.url} component={GiveAway} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={GiveAwayDeleteDialog} />
  </>
);

export default Routes;
