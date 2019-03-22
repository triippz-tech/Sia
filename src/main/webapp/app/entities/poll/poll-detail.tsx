import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './poll.reducer';
import { IPoll } from 'app/shared/model/poll.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPollDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PollDetail extends React.Component<IPollDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { pollEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.poll.detail.title">Poll</Translate> [<b>{pollEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guildId">
                <Translate contentKey="siaApp.poll.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{pollEntity.guildId}</dd>
            <dt>
              <span id="userId">
                <Translate contentKey="siaApp.poll.userId">User Id</Translate>
              </span>
            </dt>
            <dd>{pollEntity.userId}</dd>
            <dt>
              <span id="textChannelId">
                <Translate contentKey="siaApp.poll.textChannelId">Text Channel Id</Translate>
              </span>
            </dt>
            <dd>{pollEntity.textChannelId}</dd>
            <dt>
              <span id="title">
                <Translate contentKey="siaApp.poll.title">Title</Translate>
              </span>
            </dt>
            <dd>{pollEntity.messageId}</dd>
            <dt>
              <span id="messageId">
                <Translate contentKey="siaApp.poll.messageId">Message ID</Translate>
              </span>
            </dt>
            <dd>{pollEntity.title}</dd>
            <dt>
              <span id="expired">
                <Translate contentKey="siaApp.poll.expired">Is Expired</Translate>
              </span>
            </dt>
            <dd>{pollEntity.expired ? 'true' : 'false'}</dd>
            <dt>
              <span id="finishTime">
                <Translate contentKey="siaApp.poll.finishTime">Finish Time</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={pollEntity.finishTime} type="date" format={APP_DATE_FORMAT} />
            </dd>
          </dl>
          <Button tag={Link} to="/entity/poll" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/poll/${pollEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ poll }: IRootState) => ({
  pollEntity: poll.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PollDetail);
