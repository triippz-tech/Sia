import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './strikes.reducer';
import { IStrikes } from 'app/shared/model/strikes.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStrikesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class StrikesDetail extends React.Component<IStrikesDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { strikesEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.strikes.detail.title">Strikes</Translate> [<b>{strikesEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guildId">
                <Translate contentKey="siaApp.strikes.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{strikesEntity.guildId}</dd>
            <dt>
              <span id="userId">
                <Translate contentKey="siaApp.strikes.userId">User Id</Translate>
              </span>
            </dt>
            <dd>{strikesEntity.userId}</dd>
            <dt>
              <span id="strikes">
                <Translate contentKey="siaApp.strikes.strikes">Strikes</Translate>
              </span>
            </dt>
            <dd>{strikesEntity.strikes}</dd>
          </dl>
          <Button tag={Link} to="/entity/strikes" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/strikes/${strikesEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ strikes }: IRootState) => ({
  strikesEntity: strikes.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(StrikesDetail);
