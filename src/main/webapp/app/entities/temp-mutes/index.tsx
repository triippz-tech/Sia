import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TempMutes from './temp-mutes';
import TempMutesDetail from './temp-mutes-detail';
import TempMutesUpdate from './temp-mutes-update';
import TempMutesDeleteDialog from './temp-mutes-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TempMutesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TempMutesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TempMutesDetail} />
      <ErrorBoundaryRoute path={match.url} component={TempMutes} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={TempMutesDeleteDialog} />
  </>
);

export default Routes;
