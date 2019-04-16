import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IDiscordUser } from 'app/shared/model/discord-user.model';
import { getEntities as getDiscordUsers } from 'app/entities/discord-user/discord-user.reducer';
import { IGuildSettings } from 'app/shared/model/guild-settings.model';
import { getEntities as getGuildSettings } from 'app/entities/guild-settings/guild-settings.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './give-away.reducer';
import { IGiveAway } from 'app/shared/model/give-away.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGiveAwayUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IGiveAwayUpdateState {
  isNew: boolean;
  idsdiscordUser: any[];
  guildsettingsId: string;
}

export class GiveAwayUpdate extends React.Component<IGiveAwayUpdateProps, IGiveAwayUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idsdiscordUser: [],
      guildsettingsId: '0',
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

    this.props.getDiscordUsers();
    this.props.getGuildSettings();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    values.finish = convertDateTimeToServer(values.finish);

    if (errors.length === 0) {
      const { giveAwayEntity } = this.props;
      const entity = {
        ...giveAwayEntity,
        ...values,
        discordUsers: mapIdList(values.discordUsers)
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/give-away');
  };

  render() {
    const { giveAwayEntity, discordUsers, guildSettings, loading, updating } = this.props;
    const { isNew } = this.state;

    const { message } = giveAwayEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="siaApp.giveAway.home.createOrEditLabel">
              <Translate contentKey="siaApp.giveAway.home.createOrEditLabel">Create or edit a GiveAway</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : giveAwayEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="give-away-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    <Translate contentKey="siaApp.giveAway.name">Name</Translate>
                  </Label>
                  <AvField
                    id="give-away-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 250, errorMessage: translate('entity.validation.maxlength', { max: 250 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="messageLabel" for="message">
                    <Translate contentKey="siaApp.giveAway.message">Message</Translate>
                  </Label>
                  <AvInput
                    id="give-away-message"
                    type="textarea"
                    name="message"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="messageIdLabel" for="messageId">
                    <Translate contentKey="siaApp.giveAway.messageId">Message Id</Translate>
                  </Label>
                  <AvField
                    id="give-away-messageId"
                    type="string"
                    className="form-control"
                    name="messageId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="textChannelIdLabel" for="textChannelId">
                    <Translate contentKey="siaApp.giveAway.textChannelId">Text Channel Id</Translate>
                  </Label>
                  <AvField
                    id="give-away-textChannelId"
                    type="string"
                    className="form-control"
                    name="textChannelId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="finishLabel" for="finish">
                    <Translate contentKey="siaApp.giveAway.finish">Finish</Translate>
                  </Label>
                  <AvInput
                    id="give-away-finish"
                    type="datetime-local"
                    className="form-control"
                    name="finish"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.giveAwayEntity.finish)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="winnerLabel" for="winner">
                    <Translate contentKey="siaApp.giveAway.winner">Winner</Translate>
                  </Label>
                  <AvField
                    id="give-away-winner"
                    type="string"
                    className="form-control"
                    name="winner"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="expiredLabel" check>
                    <AvInput id="give-away-expired" type="checkbox" className="form-control" name="expired" />
                    <Translate contentKey="siaApp.giveAway.expired">Expired</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="discordUsers">
                    <Translate contentKey="siaApp.giveAway.discordUser">Discord User</Translate>
                  </Label>
                  <AvInput
                    id="give-away-discordUser"
                    type="select"
                    multiple
                    className="form-control"
                    name="discordUsers"
                    value={giveAwayEntity.discordUsers && giveAwayEntity.discordUsers.map(e => e.id)}
                  >
                    <option value="" key="0" />
                    {discordUsers
                      ? discordUsers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.userId}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="guildsettings.guildId">
                    <Translate contentKey="siaApp.giveAway.guildsettings">Guildsettings</Translate>
                  </Label>
                  <AvInput id="give-away-guildsettings" type="select" className="form-control" name="guildsettings.id">
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
                <Button tag={Link} id="cancel-save" to="/entity/give-away" replace color="info">
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
  discordUsers: storeState.discordUser.entities,
  guildSettings: storeState.guildSettings.entities,
  giveAwayEntity: storeState.giveAway.entity,
  loading: storeState.giveAway.loading,
  updating: storeState.giveAway.updating,
  updateSuccess: storeState.giveAway.updateSuccess
});

const mapDispatchToProps = {
  getDiscordUsers,
  getGuildSettings,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GiveAwayUpdate);
