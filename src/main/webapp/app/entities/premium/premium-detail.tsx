import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './premium.reducer';
import { IPremium } from 'app/shared/model/premium.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPremiumDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PremiumDetail extends React.Component<IPremiumDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { premiumEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.premium.detail.title">Premium</Translate> [<b>{premiumEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="discordId">
                <Translate contentKey="siaApp.premium.discordId">Guild/User Id</Translate>
              </span>
            </dt>
            <dd>{premiumEntity.discordId}</dd>
            <dt>
              <span id="until">
                <Translate contentKey="siaApp.premium.until">Until</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={premiumEntity.until} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="level">
                <Translate contentKey="siaApp.premium.level">Level</Translate>
              </span>
            </dt>
            <dd>{premiumEntity.level}</dd>
          </dl>
          <Button tag={Link} to="/entity/premium" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/premium/${premiumEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ premium }: IRootState) => ({
  premiumEntity: premium.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PremiumDetail);
