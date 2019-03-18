import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Playlist from './playlist';
import PlaylistDetail from './playlist-detail';
import PlaylistUpdate from './playlist-update';
import PlaylistDeleteDialog from './playlist-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PlaylistUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PlaylistUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PlaylistDetail} />
      <ErrorBoundaryRoute path={match.url} component={Playlist} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PlaylistDeleteDialog} />
  </>
);

export default Routes;
