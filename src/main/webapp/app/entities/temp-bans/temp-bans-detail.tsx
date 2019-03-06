import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './temp-bans.reducer';
import { ITempBans } from 'app/shared/model/temp-bans.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITempBansDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class TempBansDetail extends React.Component<ITempBansDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { tempBansEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.tempBans.detail.title">TempBans</Translate> [<b>{tempBansEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guildId">
                <Translate contentKey="siaApp.tempBans.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{tempBansEntity.guildId}</dd>
            <dt>
              <span id="userId">
                <Translate contentKey="siaApp.tempBans.userId">User Id</Translate>
              </span>
            </dt>
            <dd>{tempBansEntity.userId}</dd>
            <dt>
              <span id="finish">
                <Translate contentKey="siaApp.tempBans.finish">Finish</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={tempBansEntity.finish} type="date" format={APP_DATE_FORMAT} />
            </dd>
          </dl>
          <Button tag={Link} to="/entity/temp-bans" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/temp-bans/${tempBansEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ tempBans }: IRootState) => ({
  tempBansEntity: tempBans.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TempBansDetail);
