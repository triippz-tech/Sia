import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IDiscordUser } from 'app/shared/model/discord-user.model';
import { getEntities as getDiscordUsers } from 'app/entities/discord-user/discord-user.reducer';
import { getEntity, updateEntity, createEntity, reset } from './poll.reducer';
import { IPoll } from 'app/shared/model/poll.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPollUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPollUpdateState {
  isNew: boolean;
  idsdiscorduser: any[];
}

export class PollUpdate extends React.Component<IPollUpdateProps, IPollUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idsdiscorduser: [],
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
  }

  saveEntity = (event, errors, values) => {
    values.finishTime = convertDateTimeToServer(values.finishTime);

    if (errors.length === 0) {
      const { pollEntity } = this.props;
      const entity = {
        ...pollEntity,
        ...values,
        discordusers: mapIdList(values.discordusers)
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/poll');
  };

  render() {
    const { pollEntity, discordUsers, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="siaApp.poll.home.createOrEditLabel">
              <Translate contentKey="siaApp.poll.home.createOrEditLabel">Create or edit a Poll</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : pollEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="poll-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="guildIdLabel" for="guildId">
                    <Translate contentKey="siaApp.poll.guildId">Guild Id</Translate>
                  </Label>
                  <AvField
                    id="poll-guildId"
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
                  <Label id="userIdLabel" for="userId">
                    <Translate contentKey="siaApp.poll.userId">User Id</Translate>
                  </Label>
                  <AvField
                    id="poll-userId"
                    type="string"
                    className="form-control"
                    name="userId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="textChannelIdLabel" for="textChannelId">
                    <Translate contentKey="siaApp.poll.textChannelId">Text Channel Id</Translate>
                  </Label>
                  <AvField id="poll-textChannelId" type="string" className="form-control" name="textChannelId" />
                </AvGroup>
                <AvGroup>
                  <Label id="messageId" for="messageId">
                    <Translate contentKey="siaApp.poll.messageId">Message ID</Translate>
                  </Label>
                  <AvField id="poll-messageId" type="string" className="form-control" name="messageId" />
                </AvGroup>
                <AvGroup>
                  <Label id="titleLabel" for="title">
                    <Translate contentKey="siaApp.poll.title">Title</Translate>
                  </Label>
                  <AvField
                    id="poll-title"
                    type="text"
                    name="title"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="expired" check>
                    <AvInput id="poll-expired" type="checkbox" className="form-control" name="expired" />
                    <Translate contentKey="siaApp.poll.expired">Is Expired</Translate>
                  </Label>
                  {/*<AvField id="poll-expired" type="boolean" className="form-control" name="expired" />*/}
                </AvGroup>
                <AvGroup>
                  <Label id="finishTimeLabel" for="finishTime">
                    <Translate contentKey="siaApp.poll.finishTime">Finish Time</Translate>
                  </Label>
                  <AvInput
                    id="poll-finishTime"
                    type="datetime-local"
                    className="form-control"
                    name="finishTime"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.pollEntity.finishTime)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="discordUsers">
                    <Translate contentKey="siaApp.poll.discorduser">Discorduser</Translate>
                  </Label>
                  <AvInput
                    id="poll-discorduser"
                    type="select"
                    multiple
                    className="form-control"
                    name="discordusers"
                    value={pollEntity.discordusers && pollEntity.discordusers.map(e => e.id)}
                  >
                    <option value="" key="0" />
                    {discordUsers
                      ? discordUsers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/poll" replace color="info">
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
  pollEntity: storeState.poll.entity,
  loading: storeState.poll.loading,
  updating: storeState.poll.updating,
  updateSuccess: storeState.poll.updateSuccess
});

const mapDispatchToProps = {
  getDiscordUsers,
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
)(PollUpdate);
