import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IGuildSettings } from 'app/shared/model/guild-settings.model';
import { getEntities as getGuildSettings } from 'app/entities/guild-settings/guild-settings.reducer';
import { getEntity, updateEntity, createEntity, reset } from './welcome-message.reducer';
import { IWelcomeMessage } from 'app/shared/model/welcome-message.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IWelcomeMessageUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IWelcomeMessageUpdateState {
  isNew: boolean;
  guildsettingsId: string;
}

export class WelcomeMessageUpdate extends React.Component<IWelcomeMessageUpdateProps, IWelcomeMessageUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      guildsettingsId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getGuildSettings();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { welcomeMessageEntity } = this.props;
      const entity = {
        ...welcomeMessageEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/welcome-message');
  };

  render() {
    const { welcomeMessageEntity, guildSettings, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="siaApp.welcomeMessage.home.createOrEditLabel">
              <Translate contentKey="siaApp.welcomeMessage.home.createOrEditLabel">Create or edit a WelcomeMessage</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : welcomeMessageEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="welcome-message-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    <Translate contentKey="siaApp.welcomeMessage.name">Name</Translate>
                  </Label>
                  <AvField
                    id="welcome-message-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="messageTitleLabel" for="messageTitle">
                    <Translate contentKey="siaApp.welcomeMessage.messageTitle">Message Title</Translate>
                  </Label>
                  <AvField
                    id="welcome-message-messageTitle"
                    type="text"
                    name="messageTitle"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="bodyLabel" for="body">
                    <Translate contentKey="siaApp.welcomeMessage.body">Body</Translate>
                  </Label>
                  <AvField
                    id="welcome-message-body"
                    type="text"
                    name="body"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="footerLabel" for="footer">
                    <Translate contentKey="siaApp.welcomeMessage.footer">Footer</Translate>
                  </Label>
                  <AvField
                    id="welcome-message-footer"
                    type="text"
                    name="footer"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="websiteUrlLabel" for="websiteUrl">
                    <Translate contentKey="siaApp.welcomeMessage.websiteUrl">Website Url</Translate>
                  </Label>
                  <AvField id="welcome-message-websiteUrl" type="text" name="websiteUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="logoUrlLabel" for="logoUrl">
                    <Translate contentKey="siaApp.welcomeMessage.logoUrl">Logo Url</Translate>
                  </Label>
                  <AvField id="welcome-message-logoUrl" type="text" name="logoUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="activeLabel" check>
                    <AvInput id="welcome-message-active" type="checkbox" className="form-control" name="active" />
                    <Translate contentKey="siaApp.welcomeMessage.active">Active</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="guildsettings.id">
                    <Translate contentKey="siaApp.welcomeMessage.guildsettings">Guildsettings</Translate>
                  </Label>
                  <AvInput id="welcome-message-guildsettings" type="select" className="form-control" name="guildsettings.id">
                    <option value="" key="0" />
                    {guildSettings
                      ? guildSettings.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/welcome-message" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  guildSettings: storeState.guildSettings.entities,
  welcomeMessageEntity: storeState.welcomeMessage.entity,
  loading: storeState.welcomeMessage.loading,
  updating: storeState.welcomeMessage.updating,
  updateSuccess: storeState.welcomeMessage.updateSuccess
});

const mapDispatchToProps = {
  getGuildSettings,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WelcomeMessageUpdate);
