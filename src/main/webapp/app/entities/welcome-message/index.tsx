import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WelcomeMessage from './welcome-message';
import WelcomeMessageDetail from './welcome-message-detail';
import WelcomeMessageUpdate from './welcome-message-update';
import WelcomeMessageDeleteDialog from './welcome-message-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WelcomeMessageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WelcomeMessageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WelcomeMessageDetail} />
      <ErrorBoundaryRoute path={match.url} component={WelcomeMessage} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={WelcomeMessageDeleteDialog} />
  </>
);

export default Routes;
