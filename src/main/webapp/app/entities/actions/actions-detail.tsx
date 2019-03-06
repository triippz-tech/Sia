import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './actions.reducer';
import { IActions } from 'app/shared/model/actions.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IActionsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ActionsDetail extends React.Component<IActionsDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { actionsEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.actions.detail.title">Actions</Translate> [<b>{actionsEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guildId">
                <Translate contentKey="siaApp.actions.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{actionsEntity.guildId}</dd>
            <dt>
              <span id="numStrikes">
                <Translate contentKey="siaApp.actions.numStrikes">Num Strikes</Translate>
              </span>
            </dt>
            <dd>{actionsEntity.numStrikes}</dd>
            <dt>
              <span id="action">
                <Translate contentKey="siaApp.actions.action">Action</Translate>
              </span>
            </dt>
            <dd>{actionsEntity.action}</dd>
            <dt>
              <span id="time">
                <Translate contentKey="siaApp.actions.time">Time</Translate>
              </span>
            </dt>
            <dd>{actionsEntity.time}</dd>
          </dl>
          <Button tag={Link} to="/entity/actions" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/actions/${actionsEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ actions }: IRootState) => ({
  actionsEntity: actions.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ActionsDetail);
