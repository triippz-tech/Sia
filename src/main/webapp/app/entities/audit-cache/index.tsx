import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AuditCache from './audit-cache';
import AuditCacheDetail from './audit-cache-detail';
import AuditCacheUpdate from './audit-cache-update';
import AuditCacheDeleteDialog from './audit-cache-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AuditCacheUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AuditCacheUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AuditCacheDetail} />
      <ErrorBoundaryRoute path={match.url} component={AuditCache} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={AuditCacheDeleteDialog} />
  </>
);

export default Routes;
