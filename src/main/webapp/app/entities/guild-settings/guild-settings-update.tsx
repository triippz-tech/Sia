import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './guild-settings.reducer';
import { IGuildSettings } from 'app/shared/model/guild-settings.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGuildSettingsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IGuildSettingsUpdateState {
  isNew: boolean;
}

export class GuildSettingsUpdate extends React.Component<IGuildSettingsUpdateProps, IGuildSettingsUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { guildSettingsEntity } = this.props;
      const entity = {
        ...guildSettingsEntity,
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
    this.props.history.push('/entity/guild-settings');
  };

  render() {
    const { guildSettingsEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="siaApp.guildSettings.home.createOrEditLabel">
              <Translate contentKey="siaApp.guildSettings.home.createOrEditLabel">Create or edit a GuildSettings</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : guildSettingsEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="guild-settings-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="guildIdLabel" for="guildId">
                    <Translate contentKey="siaApp.guildSettings.guildId">Guild Id</Translate>
                  </Label>
                  <AvField
                    id="guild-settings-guildId"
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
                  <Label id="modRoleIdLabel" for="modRoleId">
                    <Translate contentKey="siaApp.guildSettings.modRoleId">Mod Role Id</Translate>
                  </Label>
                  <AvField
                    id="guild-settings-modRoleId"
                    type="string"
                    className="form-control"
                    name="modRoleId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="modLogIdLabel" for="modLogId">
                    <Translate contentKey="siaApp.guildSettings.modLogId">Mod Log Id</Translate>
                  </Label>
                  <AvField
                    id="guild-settings-modLogId"
                    type="string"
                    className="form-control"
                    name="modLogId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="serverLogIdLabel" for="serverLogId">
                    <Translate contentKey="siaApp.guildSettings.serverLogId">Server Log Id</Translate>
                  </Label>
                  <AvField
                    id="guild-settings-serverLogId"
                    type="string"
                    className="form-control"
                    name="serverLogId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="messageLogIdLabel" for="messageLogId">
                    <Translate contentKey="siaApp.guildSettings.messageLogId">Message Log Id</Translate>
                  </Label>
                  <AvField
                    id="guild-settings-messageLogId"
                    type="string"
                    className="form-control"
                    name="messageLogId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="voiceLogIdLabel" for="voiceLogId">
                    <Translate contentKey="siaApp.guildSettings.voiceLogId">Voice Log Id</Translate>
                  </Label>
                  <AvField
                    id="guild-settings-voiceLogId"
                    type="string"
                    className="form-control"
                    name="voiceLogId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="avatarLogIdLabel" for="avatarLogId">
                    <Translate contentKey="siaApp.guildSettings.avatarLogId">Avatar Log Id</Translate>
                  </Label>
                  <AvField
                    id="guild-settings-avatarLogId"
                    type="string"
                    className="form-control"
                    name="avatarLogId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="prefixLabel" for="prefix">
                    <Translate contentKey="siaApp.guildSettings.prefix">Prefix</Translate>
                  </Label>
                  <AvField
                    id="guild-settings-prefix"
                    type="text"
                    name="prefix"
                    validate={{
                      maxLength: { value: 40, errorMessage: translate('entity.validation.maxlength', { max: 40 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="timezoneLabel" for="timezone">
                    <Translate contentKey="siaApp.guildSettings.timezone">Timezone</Translate>
                  </Label>
                  <AvField
                    id="guild-settings-timezone"
                    type="text"
                    name="timezone"
                    validate={{
                      maxLength: { value: 32, errorMessage: translate('entity.validation.maxlength', { max: 32 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="raidModeLabel" for="raidMode">
                    <Translate contentKey="siaApp.guildSettings.raidMode">Raid Mode</Translate>
                  </Label>
                  <AvField
                    id="guild-settings-raidMode"
                    type="string"
                    className="form-control"
                    name="raidMode"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="muteRoleLabel" for="muteRole">
                    <Translate contentKey="siaApp.guildSettings.muteRole">Mute Role</Translate>
                  </Label>
                  <AvField
                    id="guild-settings-muteRole"
                    type="string"
                    className="form-control"
                    name="muteRole"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/guild-settings" replace color="info">
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
  guildSettingsEntity: storeState.guildSettings.entity,
  loading: storeState.guildSettings.loading,
  updating: storeState.guildSettings.updating,
  updateSuccess: storeState.guildSettings.updateSuccess
});

const mapDispatchToProps = {
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
)(GuildSettingsUpdate);
