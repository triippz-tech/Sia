import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './custom-command.reducer';
import { ICustomCommand } from 'app/shared/model/custom-command.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomCommandDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CustomCommandDetail extends React.Component<ICustomCommandDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { customCommandEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.customCommand.detail.title">CustomCommand</Translate> [<b>{customCommandEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guildId">
                <Translate contentKey="siaApp.customCommand.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{customCommandEntity.guildId}</dd>
            <dt>
              <span id="commandName">
                <Translate contentKey="siaApp.customCommand.commandName">Command Name</Translate>
              </span>
            </dt>
            <dd>{customCommandEntity.commandName}</dd>
            <dt>
              <span id="message">
                <Translate contentKey="siaApp.customCommand.message">Message</Translate>
              </span>
            </dt>
            <dd>{customCommandEntity.message}</dd>
          </dl>
          <Button tag={Link} to="/entity/custom-command" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/custom-command/${customCommandEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ customCommand }: IRootState) => ({
  customCommandEntity: customCommand.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomCommandDetail);
