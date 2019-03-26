import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPoll } from 'app/shared/model/poll.model';
import { getEntities as getPolls } from 'app/entities/poll/poll.reducer';
import { IPollItems } from 'app/shared/model/poll-items.model';
import { getEntities as getPollItems } from 'app/entities/poll-items/poll-items.reducer';
import { getEntity, updateEntity, createEntity, reset } from './discord-user.reducer';
import { IDiscordUser } from 'app/shared/model/discord-user.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDiscordUserUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IDiscordUserUpdateState {
  isNew: boolean;
  pollId: string;
  pollitemsId: string;
}

export class DiscordUserUpdate extends React.Component<IDiscordUserUpdateProps, IDiscordUserUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      pollId: '0',
      pollitemsId: '0',
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

    this.props.getPolls();
    this.props.getPollItems();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { discordUserEntity } = this.props;
      const entity = {
        ...discordUserEntity,
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
    this.props.history.push('/entity/discord-user');
  };

  render() {
    const { discordUserEntity, polls, pollItems, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="siaApp.discordUser.home.createOrEditLabel">
              <Translate contentKey="siaApp.discordUser.home.createOrEditLabel">Create or edit a DiscordUser</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : discordUserEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="discord-user-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="userIdLabel" for="userId">
                    <Translate contentKey="siaApp.discordUser.userId">User Id</Translate>
                  </Label>
                  <AvField
                    id="discord-user-userId"
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
                  <Label id="commandsIssuedLabel" for="commandsIssued">
                    <Translate contentKey="siaApp.discordUser.commandsIssued">Commands Issued</Translate>
                  </Label>
                  <AvField
                    id="discord-user-commandsIssued"
                    type="string"
                    className="form-control"
                    name="commandsIssued"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="blacklistedLabel" check>
                    <AvInput id="discord-user-blacklisted" type="checkbox" className="form-control" name="blacklisted" />
                    <Translate contentKey="siaApp.discordUser.blacklisted">Blacklisted</Translate>
                  </Label>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/discord-user" replace color="info">
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
  polls: storeState.poll.entities,
  pollItems: storeState.pollItems.entities,
  discordUserEntity: storeState.discordUser.entity,
  loading: storeState.discordUser.loading,
  updating: storeState.discordUser.updating,
  updateSuccess: storeState.discordUser.updateSuccess
});

const mapDispatchToProps = {
  getPolls,
  getPollItems,
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
)(DiscordUserUpdate);
