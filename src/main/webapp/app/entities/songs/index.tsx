import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Songs from './songs';
import SongsDetail from './songs-detail';
import SongsUpdate from './songs-update';
import SongsDeleteDialog from './songs-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SongsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SongsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SongsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Songs} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SongsDeleteDialog} />
  </>
);

export default Routes;
