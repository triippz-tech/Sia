import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AuditCache from './audit-cache';
import AutoMod from './auto-mod';
import GuildSettings from './guild-settings';
import Ignored from './ignored';
import Premium from './premium';
import Actions from './actions';
import Strikes from './strikes';
import TempMutes from './temp-mutes';
import TempBans from './temp-bans';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/audit-cache`} component={AuditCache} />
      <ErrorBoundaryRoute path={`${match.url}/auto-mod`} component={AutoMod} />
      <ErrorBoundaryRoute path={`${match.url}/guild-settings`} component={GuildSettings} />
      <ErrorBoundaryRoute path={`${match.url}/ignored`} component={Ignored} />
      <ErrorBoundaryRoute path={`${match.url}/premium`} component={Premium} />
      <ErrorBoundaryRoute path={`${match.url}/actions`} component={Actions} />
      <ErrorBoundaryRoute path={`${match.url}/strikes`} component={Strikes} />
      <ErrorBoundaryRoute path={`${match.url}/temp-mutes`} component={TempMutes} />
      <ErrorBoundaryRoute path={`${match.url}/temp-bans`} component={TempBans} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
