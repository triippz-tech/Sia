import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './give-away.reducer';
import { IGiveAway } from 'app/shared/model/give-away.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGiveAwayDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class GiveAwayDetail extends React.Component<IGiveAwayDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { giveAwayEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.giveAway.detail.title">GiveAway</Translate> [<b>{giveAwayEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="siaApp.giveAway.name">Name</Translate>
              </span>
            </dt>
            <dd>{giveAwayEntity.name}</dd>
            <dt>
              <span id="message">
                <Translate contentKey="siaApp.giveAway.message">Message</Translate>
              </span>
            </dt>
            <dd>{giveAwayEntity.message}</dd>
            <dt>
              <span id="messageId">
                <Translate contentKey="siaApp.giveAway.messageId">Message Id</Translate>
              </span>
            </dt>
            <dd>{giveAwayEntity.messageId}</dd>
            <dt>
              <span id="textChannelId">
                <Translate contentKey="siaApp.giveAway.textChannelId">Text Channel Id</Translate>
              </span>
            </dt>
            <dd>{giveAwayEntity.textChannelId}</dd>
            <dt>
              <span id="finish">
                <Translate contentKey="siaApp.giveAway.finish">Finish</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={giveAwayEntity.finish} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="winner">
                <Translate contentKey="siaApp.giveAway.winner">Winner</Translate>
              </span>
            </dt>
            <dd>{giveAwayEntity.winner}</dd>
            <dt>
              <span id="expired">
                <Translate contentKey="siaApp.giveAway.expired">Expired</Translate>
              </span>
            </dt>
            <dd>{giveAwayEntity.expired ? 'true' : 'false'}</dd>
            <dt>
              <Translate contentKey="siaApp.giveAway.discordUser">Discord User</Translate>
            </dt>
            <dd>
              {giveAwayEntity.discordUsers
                ? giveAwayEntity.discordUsers.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.userId}</a>
                      {i === giveAwayEntity.discordUsers.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </dd>
            <dt>
              <Translate contentKey="siaApp.giveAway.guildsettings">Guildsettings</Translate>
            </dt>
            <dd>{giveAwayEntity.guildsettings ? giveAwayEntity.guildsettings.guildId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/give-away" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/give-away/${giveAwayEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ giveAway }: IRootState) => ({
  giveAwayEntity: giveAway.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GiveAwayDetail);
