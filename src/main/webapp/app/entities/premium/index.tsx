import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Premium from './premium';
import PremiumDetail from './premium-detail';
import PremiumUpdate from './premium-update';
import PremiumDeleteDialog from './premium-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PremiumUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PremiumUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PremiumDetail} />
      <ErrorBoundaryRoute path={match.url} component={Premium} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PremiumDeleteDialog} />
  </>
);

export default Routes;
