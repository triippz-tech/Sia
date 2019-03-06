import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Strikes from './strikes';
import StrikesDetail from './strikes-detail';
import StrikesUpdate from './strikes-update';
import StrikesDeleteDialog from './strikes-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={StrikesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={StrikesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={StrikesDetail} />
      <ErrorBoundaryRoute path={match.url} component={Strikes} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={StrikesDeleteDialog} />
  </>
);

export default Routes;
