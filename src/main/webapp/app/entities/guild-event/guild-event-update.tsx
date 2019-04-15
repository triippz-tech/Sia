import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IGuildSettings } from 'app/shared/model/guild-settings.model';
import { getEntities as getGuildSettings } from 'app/entities/guild-settings/guild-settings.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './guild-event.reducer';
import { IGuildEvent } from 'app/shared/model/guild-event.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGuildEventUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IGuildEventUpdateState {
  isNew: boolean;
  guildsettingsId: string;
}

export class GuildEventUpdate extends React.Component<IGuildEventUpdateProps, IGuildEventUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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

    this.props.getGuildSettings();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    values.eventStart = convertDateTimeToServer(values.eventStart);

    if (errors.length === 0) {
      const { guildEventEntity } = this.props;
      const entity = {
        ...guildEventEntity,
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
    this.props.history.push('/entity/guild-event');
  };

  render() {
    const { guildEventEntity, guildSettings, loading, updating } = this.props;
    const { isNew } = this.state;

    const { eventMessage } = guildEventEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="siaApp.guildEvent.home.createOrEditLabel">
              <Translate contentKey="siaApp.guildEvent.home.createOrEditLabel">Create or edit a GuildEvent</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : guildEventEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="guild-event-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="eventNameLabel" for="eventName">
                    <Translate contentKey="siaApp.guildEvent.eventName">Event Name</Translate>
                  </Label>
                  <AvField
                    id="guild-event-eventName"
                    type="text"
                    name="eventName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 250, errorMessage: translate('entity.validation.maxlength', { max: 250 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="eventImageUrlLabel" for="eventImageUrl">
                    <Translate contentKey="siaApp.guildEvent.eventImageUrl">Event Image Url</Translate>
                  </Label>
                  <AvField
                    id="guild-event-eventImageUrl"
                    type="text"
                    name="eventImageUrl"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 250, errorMessage: translate('entity.validation.maxlength', { max: 250 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="eventMessageLabel" for="eventMessage">
                    <Translate contentKey="siaApp.guildEvent.eventMessage">Event Message</Translate>
                  </Label>
                  <AvInput
                    id="guild-event-eventMessage"
                    type="textarea"
                    name="eventMessage"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="textChannelIdLabel" for="textChannelId">
                    <Translate contentKey="siaApp.guildEvent.textChannelId">Text Channel</Translate>
                  </Label>
                  <AvField
                    id="guild-event-textChannelId"
                    type="text"
                    name="textChannelId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="eventStartLabel" for="eventStart">
                    <Translate contentKey="siaApp.guildEvent.eventStart">Event Start</Translate>
                  </Label>
                  <AvInput
                    id="guild-event-eventStart"
                    type="datetime-local"
                    className="form-control"
                    name="eventStart"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.guildEventEntity.eventStart)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="expired" check>
                    <AvInput id="guild-event-expired" type="checkbox" className="form-control" name="expired" />
                    <Translate contentKey="siaApp.poll.expired">Is Expired</Translate>
                  </Label>
                  {/*<AvField id="guild-event-expired" type="boolean" className="form-control" name="expired" />*/}
                </AvGroup>
                <AvGroup>
                  <Label for="guildsettings.id">
                    <Translate contentKey="siaApp.guildEvent.guildsettings">Guildsettings</Translate>
                  </Label>
                  <AvInput id="guild-event-guildsettings" type="select" className="form-control" name="guildsettings.id">
                    <option value="" key="0" />
                    {guildSettings
                      ? guildSettings.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/guild-event" replace color="info">
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
  guildEventEntity: storeState.guildEvent.entity,
  loading: storeState.guildEvent.loading,
  updating: storeState.guildEvent.updating,
  updateSuccess: storeState.guildEvent.updateSuccess
});

const mapDispatchToProps = {
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
)(GuildEventUpdate);
