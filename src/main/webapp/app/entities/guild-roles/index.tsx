import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GuildRoles from './guild-roles';
import GuildRolesDetail from './guild-roles-detail';
import GuildRolesUpdate from './guild-roles-update';
import GuildRolesDeleteDialog from './guild-roles-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GuildRolesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GuildRolesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GuildRolesDetail} />
      <ErrorBoundaryRoute path={match.url} component={GuildRoles} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={GuildRolesDeleteDialog} />
  </>
);

export default Routes;
