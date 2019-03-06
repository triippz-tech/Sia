import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TempBans from './temp-bans';
import TempBansDetail from './temp-bans-detail';
import TempBansUpdate from './temp-bans-update';
import TempBansDeleteDialog from './temp-bans-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TempBansUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TempBansUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TempBansDetail} />
      <ErrorBoundaryRoute path={match.url} component={TempBans} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={TempBansDeleteDialog} />
  </>
);

export default Routes;
