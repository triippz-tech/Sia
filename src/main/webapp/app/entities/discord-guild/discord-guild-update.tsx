import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IGuildSettings } from 'app/shared/model/guild-settings.model';
import { getEntities as getGuildSettings } from 'app/entities/guild-settings/guild-settings.reducer';
import { IAuditCache } from 'app/shared/model/audit-cache.model';
import { getEntities as getAuditCaches } from 'app/entities/audit-cache/audit-cache.reducer';
import { IAutoMod } from 'app/shared/model/auto-mod.model';
import { getEntities as getAutoMods } from 'app/entities/auto-mod/auto-mod.reducer';
import { IGuildMusicSettings } from 'app/shared/model/guild-music-settings.model';
import { getEntities as getGuildMusicSettings } from 'app/entities/guild-music-settings/guild-music-settings.reducer';
import { getEntity, updateEntity, createEntity, reset } from './discord-guild.reducer';
import { IDiscordGuild } from 'app/shared/model/discord-guild.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDiscordGuildUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IDiscordGuildUpdateState {
  isNew: boolean;
  guildSettingsId: string;
  auditCacheId: string;
  autoModId: string;
  guildMusicSettingsId: string;
}

export class DiscordGuildUpdate extends React.Component<IDiscordGuildUpdateProps, IDiscordGuildUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      guildSettingsId: '0',
      auditCacheId: '0',
      autoModId: '0',
      guildMusicSettingsId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getGuildSettings();
    this.props.getAuditCaches();
    this.props.getAutoMods();
    this.props.getGuildMusicSettings();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { discordGuildEntity } = this.props;
      const entity = {
        ...discordGuildEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/discord-guild');
  };

  render() {
    const { discordGuildEntity, guildSettings, auditCaches, autoMods, guildMusicSettings, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="siaApp.discordGuild.home.createOrEditLabel">
              <Translate contentKey="siaApp.discordGuild.home.createOrEditLabel">Create or edit a DiscordGuild</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : discordGuildEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="discord-guild-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="guildIdLabel" for="guildId">
                    <Translate contentKey="siaApp.discordGuild.guildId">Guild Id</Translate>
                  </Label>
                  <AvField
                    id="discord-guild-guildId"
                    type="string"
                    className="form-control"
                    name="guildId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="guildNameLabel" for="guildName">
                    <Translate contentKey="siaApp.discordGuild.guildName">Guild Name</Translate>
                  </Label>
                  <AvField
                    id="discord-guild-guildName"
                    type="text"
                    name="guildName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="inviteLinkLabel" for="inviteLink">
                    <Translate contentKey="siaApp.discordGuild.inviteLink">Invite Link</Translate>
                  </Label>
                  <AvField id="discord-guild-inviteLink" type="text" name="inviteLink" />
                </AvGroup>
                <AvGroup>
                  <Label for="guildSettings.guildId">
                    <Translate contentKey="siaApp.discordGuild.guildSettings">Guild Settings</Translate>
                  </Label>
                  <AvInput id="discord-guild-guildSettings" type="select" className="form-control" name="guildSettings.id">
                    <option value="" key="0" />
                    {guildSettings
                      ? guildSettings.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.guildId}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="auditCache.id">
                    <Translate contentKey="siaApp.discordGuild.auditCache">Audit Cache</Translate>
                  </Label>
                  <AvInput id="discord-guild-auditCache" type="select" className="form-control" name="auditCache.id">
                    <option value="" key="0" />
                    {auditCaches
                      ? auditCaches.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="autoMod.id">
                    <Translate contentKey="siaApp.discordGuild.autoMod">Auto Mod</Translate>
                  </Label>
                  <AvInput id="discord-guild-autoMod" type="select" className="form-control" name="autoMod.id">
                    <option value="" key="0" />
                    {autoMods
                      ? autoMods.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="guildMusicSettings.id">
                    <Translate contentKey="siaApp.discordGuild.guildMusicSettings">Guild Music Settings</Translate>
                  </Label>
                  <AvInput id="discord-guild-guildMusicSettings" type="select" className="form-control" name="guildMusicSettings.id">
                    <option value="" key="0" />
                    {guildMusicSettings
                      ? guildMusicSettings.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/discord-guild" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  guildSettings: storeState.guildSettings.entities,
  auditCaches: storeState.auditCache.entities,
  autoMods: storeState.autoMod.entities,
  guildMusicSettings: storeState.guildMusicSettings.entities,
  discordGuildEntity: storeState.discordGuild.entity,
  loading: storeState.discordGuild.loading,
  updating: storeState.discordGuild.updating,
  updateSuccess: storeState.discordGuild.updateSuccess
});

const mapDispatchToProps = {
  getGuildSettings,
  getAuditCaches,
  getAutoMods,
  getGuildMusicSettings,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DiscordGuildUpdate);
