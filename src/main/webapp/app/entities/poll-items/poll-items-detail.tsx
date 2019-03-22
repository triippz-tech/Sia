import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './poll-items.reducer';
import { IPollItems } from 'app/shared/model/poll-items.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPollItemsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PollItemsDetail extends React.Component<IPollItemsDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { pollItemsEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.pollItems.detail.title">PollItems</Translate> [<b>{pollItemsEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="itemName">
                <Translate contentKey="siaApp.pollItems.itemName">Item Name</Translate>
              </span>
            </dt>
            <dd>{pollItemsEntity.itemName}</dd>
            <dt>
              <span id="reaction">
                <Translate contentKey="siaApp.pollItems.reaction">Reaction</Translate>
              </span>
            </dt>
            <dd>{pollItemsEntity.reaction}</dd>
            <dt>
              <span id="votes">
                <Translate contentKey="siaApp.pollItems.votes">Votes</Translate>
              </span>
            </dt>
            <dd>{pollItemsEntity.votes}</dd>
            <dt>
              <Translate contentKey="siaApp.pollItems.poll">Poll</Translate>
            </dt>
            <dd>{pollItemsEntity.poll ? pollItemsEntity.poll.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/poll-items" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/poll-items/${pollItemsEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ pollItems }: IRootState) => ({
  pollItemsEntity: pollItems.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PollItemsDetail);
