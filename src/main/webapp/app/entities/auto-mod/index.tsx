import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AutoMod from './auto-mod';
import AutoModDetail from './auto-mod-detail';
import AutoModUpdate from './auto-mod-update';
import AutoModDeleteDialog from './auto-mod-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AutoModUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AutoModUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AutoModDetail} />
      <ErrorBoundaryRoute path={match.url} component={AutoMod} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={AutoModDeleteDialog} />
  </>
);

export default Routes;
