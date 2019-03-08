import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './guild-settings.reducer';
import { IGuildSettings } from 'app/shared/model/guild-settings.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildSettingsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class GuildSettingsDetail extends React.Component<IGuildSettingsDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { guildSettingsEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.guildSettings.detail.title">GuildSettings</Translate> [<b>{guildSettingsEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guildId">
                <Translate contentKey="siaApp.guildSettings.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{guildSettingsEntity.guildId}</dd>
            <dt>
              <span id="modRoleId">
                <Translate contentKey="siaApp.guildSettings.modRoleId">Mod Role Id</Translate>
              </span>
            </dt>
            <dd>{guildSettingsEntity.modRoleId}</dd>
            <dt>
              <span id="modLogId">
                <Translate contentKey="siaApp.guildSettings.modLogId">Mod Log Id</Translate>
              </span>
            </dt>
            <dd>{guildSettingsEntity.modLogId}</dd>
            <dt>
              <span id="serverLogId">
                <Translate contentKey="siaApp.guildSettings.serverLogId">Server Log Id</Translate>
              </span>
            </dt>
            <dd>{guildSettingsEntity.serverLogId}</dd>
            <dt>
              <span id="messageLogId">
                <Translate contentKey="siaApp.guildSettings.messageLogId">Message Log Id</Translate>
              </span>
            </dt>
            <dd>{guildSettingsEntity.messageLogId}</dd>
            <dt>
              <span id="voiceLogId">
                <Translate contentKey="siaApp.guildSettings.voiceLogId">Voice Log Id</Translate>
              </span>
            </dt>
            <dd>{guildSettingsEntity.voiceLogId}</dd>
            <dt>
              <span id="avatarLogId">
                <Translate contentKey="siaApp.guildSettings.avatarLogId">Avatar Log Id</Translate>
              </span>
            </dt>
            <dd>{guildSettingsEntity.avatarLogId}</dd>
            <dt>
              <span id="prefix">
                <Translate contentKey="siaApp.guildSettings.prefix">Prefix</Translate>
              </span>
            </dt>
            <dd>{guildSettingsEntity.prefix}</dd>
            <dt>
              <span id="timezone">
                <Translate contentKey="siaApp.guildSettings.timezone">Timezone</Translate>
              </span>
            </dt>
            <dd>{guildSettingsEntity.timezone}</dd>
            <dt>
              <span id="raidMode">
                <Translate contentKey="siaApp.guildSettings.raidMode">Raid Mode</Translate>
              </span>
            </dt>
            <dd>{guildSettingsEntity.raidMode}</dd>
            <dt>
              <span id="muteRole">
                <Translate contentKey="siaApp.guildSettings.muteRole">Mute Role</Translate>
              </span>
            </dt>
            <dd>{guildSettingsEntity.muteRole}</dd>
          </dl>
          <Button tag={Link} to="/entity/guild-settings" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/guild-settings/${guildSettingsEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ guildSettings }: IRootState) => ({
  guildSettingsEntity: guildSettings.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildSettingsDetail);
