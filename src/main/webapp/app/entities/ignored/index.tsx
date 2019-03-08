import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Ignored from './ignored';
import IgnoredDetail from './ignored-detail';
import IgnoredUpdate from './ignored-update';
import IgnoredDeleteDialog from './ignored-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={IgnoredUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={IgnoredUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={IgnoredDetail} />
      <ErrorBoundaryRoute path={match.url} component={Ignored} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={IgnoredDeleteDialog} />
  </>
);

export default Routes;
