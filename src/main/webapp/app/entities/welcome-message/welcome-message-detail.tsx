import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './welcome-message.reducer';
import { IWelcomeMessage } from 'app/shared/model/welcome-message.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IWelcomeMessageDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class WelcomeMessageDetail extends React.Component<IWelcomeMessageDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { welcomeMessageEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.welcomeMessage.detail.title">WelcomeMessage</Translate> [<b>{welcomeMessageEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="siaApp.welcomeMessage.name">Name</Translate>
              </span>
            </dt>
            <dd>{welcomeMessageEntity.name}</dd>
            <dt>
              <span id="messageTitle">
                <Translate contentKey="siaApp.welcomeMessage.messageTitle">Message Title</Translate>
              </span>
            </dt>
            <dd>{welcomeMessageEntity.messageTitle}</dd>
            <dt>
              <span id="body">
                <Translate contentKey="siaApp.welcomeMessage.body">Body</Translate>
              </span>
            </dt>
            <dd>{welcomeMessageEntity.body}</dd>
            <dt>
              <span id="footer">
                <Translate contentKey="siaApp.welcomeMessage.footer">Footer</Translate>
              </span>
            </dt>
            <dd>{welcomeMessageEntity.footer}</dd>
            <dt>
              <span id="websiteUrl">
                <Translate contentKey="siaApp.welcomeMessage.websiteUrl">Website Url</Translate>
              </span>
            </dt>
            <dd>{welcomeMessageEntity.websiteUrl}</dd>
            <dt>
              <span id="logoUrl">
                <Translate contentKey="siaApp.welcomeMessage.logoUrl">Logo Url</Translate>
              </span>
            </dt>
            <dd>{welcomeMessageEntity.logoUrl}</dd>
            <dt>
              <span id="active">
                <Translate contentKey="siaApp.welcomeMessage.active">Active</Translate>
              </span>
            </dt>
            <dd>{welcomeMessageEntity.active ? 'true' : 'false'}</dd>
            <dt>
              <Translate contentKey="siaApp.welcomeMessage.guildsettings">Guildsettings</Translate>
            </dt>
            <dd>{welcomeMessageEntity.guildsettings ? welcomeMessageEntity.guildsettings.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/welcome-message" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/welcome-message/${welcomeMessageEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ welcomeMessage }: IRootState) => ({
  welcomeMessageEntity: welcomeMessage.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WelcomeMessageDetail);
