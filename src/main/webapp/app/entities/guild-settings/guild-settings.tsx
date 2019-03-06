import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './guild-settings.reducer';
import { IGuildSettings } from 'app/shared/model/guild-settings.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildSettingsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class GuildSettings extends React.Component<IGuildSettingsProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { guildSettingsList, match } = this.props;
    return (
      <div>
        <h2 id="guild-settings-heading">
          <Translate contentKey="siaApp.guildSettings.home.title">Guild Settings</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="siaApp.guildSettings.home.createLabel">Create new Guild Settings</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildSettings.guildId">Guild Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildSettings.modRoleId">Mod Role Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildSettings.modLogId">Mod Log Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildSettings.serverLogId">Server Log Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildSettings.messageLogId">Message Log Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildSettings.voiceLogId">Voice Log Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildSettings.avatarLogId">Avatar Log Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildSettings.prefix">Prefix</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildSettings.timezone">Timezone</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildSettings.raidMode">Raid Mode</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildSettings.muteRole">Mute Role</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {guildSettingsList.map((guildSettings, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${guildSettings.id}`} color="link" size="sm">
                      {guildSettings.id}
                    </Button>
                  </td>
                  <td>{guildSettings.guildId}</td>
                  <td>{guildSettings.modRoleId}</td>
                  <td>{guildSettings.modLogId}</td>
                  <td>{guildSettings.serverLogId}</td>
                  <td>{guildSettings.messageLogId}</td>
                  <td>{guildSettings.voiceLogId}</td>
                  <td>{guildSettings.avatarLogId}</td>
                  <td>{guildSettings.prefix}</td>
                  <td>{guildSettings.timezone}</td>
                  <td>{guildSettings.raidMode}</td>
                  <td>{guildSettings.muteRole}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${guildSettings.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${guildSettings.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${guildSettings.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ guildSettings }: IRootState) => ({
  guildSettingsList: guildSettings.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildSettings);
