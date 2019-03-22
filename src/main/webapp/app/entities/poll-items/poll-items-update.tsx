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
import { getEntity, updateEntity, createEntity, reset } from './poll-items.reducer';
import { IPollItems } from 'app/shared/model/poll-items.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPollItemsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPollItemsUpdateState {
  isNew: boolean;
  pollId: string;
}

export class PollItemsUpdate extends React.Component<IPollItemsUpdateProps, IPollItemsUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      pollId: '0',
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
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { pollItemsEntity } = this.props;
      const entity = {
        ...pollItemsEntity,
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
    this.props.history.push('/entity/poll-items');
  };

  render() {
    const { pollItemsEntity, polls, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="siaApp.pollItems.home.createOrEditLabel">
              <Translate contentKey="siaApp.pollItems.home.createOrEditLabel">Create or edit a PollItems</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : pollItemsEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="poll-items-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="itemNameLabel" for="itemName">
                    <Translate contentKey="siaApp.pollItems.itemName">Item Name</Translate>
                  </Label>
                  <AvField
                    id="poll-items-itemName"
                    type="text"
                    name="itemName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="reactionLabel" for="reaction">
                    <Translate contentKey="siaApp.pollItems.reaction">Reaction</Translate>
                  </Label>
                  <AvField id="poll-items-reaction" type="text" name="reaction" />
                </AvGroup>
                <AvGroup>
                  <Label id="votes" for="votes">
                    <Translate contentKey="siaApp.pollItems.votes">Votes</Translate>
                  </Label>
                  <AvField id="poll-items-votes" type="string" className="form-control" name="votes" />
                </AvGroup>
                <AvGroup>
                  <Label for="poll.id">
                    <Translate contentKey="siaApp.pollItems.poll">Poll</Translate>
                  </Label>
                  <AvInput id="poll-items-poll" type="select" className="form-control" name="poll.id">
                    <option value="" key="0" />
                    {polls
                      ? polls.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/poll-items" replace color="info">
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
  pollItemsEntity: storeState.pollItems.entity,
  loading: storeState.pollItems.loading,
  updating: storeState.pollItems.updating,
  updateSuccess: storeState.pollItems.updateSuccess
});

const mapDispatchToProps = {
  getPolls,
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
)(PollItemsUpdate);
