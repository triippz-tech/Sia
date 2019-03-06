import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './temp-mutes.reducer';
import { ITempMutes } from 'app/shared/model/temp-mutes.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITempMutesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class TempMutesDetail extends React.Component<ITempMutesDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { tempMutesEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.tempMutes.detail.title">TempMutes</Translate> [<b>{tempMutesEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guildId">
                <Translate contentKey="siaApp.tempMutes.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{tempMutesEntity.guildId}</dd>
            <dt>
              <span id="userId">
                <Translate contentKey="siaApp.tempMutes.userId">User Id</Translate>
              </span>
            </dt>
            <dd>{tempMutesEntity.userId}</dd>
            <dt>
              <span id="finish">
                <Translate contentKey="siaApp.tempMutes.finish">Finish</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={tempMutesEntity.finish} type="date" format={APP_DATE_FORMAT} />
            </dd>
          </dl>
          <Button tag={Link} to="/entity/temp-mutes" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/temp-mutes/${tempMutesEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ tempMutes }: IRootState) => ({
  tempMutesEntity: tempMutes.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TempMutesDetail);
