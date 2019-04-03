import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IGuildSettings } from 'app/shared/model/guild-settings.model';
import { getEntities as getGuildSettings } from 'app/entities/guild-settings/guild-settings.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './custom-command.reducer';
import { ICustomCommand } from 'app/shared/model/custom-command.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICustomCommandUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICustomCommandUpdateState {
  isNew: boolean;
  guildsettingsId: string;
}

export class CustomCommandUpdate extends React.Component<ICustomCommandUpdateProps, ICustomCommandUpdateState> {
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

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { customCommandEntity } = this.props;
      const entity = {
        ...customCommandEntity,
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
    this.props.history.push('/entity/custom-command');
  };

  render() {
    const { customCommandEntity, guildSettings, loading, updating } = this.props;
    const { isNew } = this.state;

    const { message } = customCommandEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="siaApp.customCommand.home.createOrEditLabel">
              <Translate contentKey="siaApp.customCommand.home.createOrEditLabel">Create or edit a CustomCommand</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : customCommandEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="custom-command-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="guildIdLabel" for="guildId">
                    <Translate contentKey="siaApp.customCommand.guildId">Guild Id</Translate>
                  </Label>
                  <AvField
                    id="custom-command-guildId"
                    type="string"
                    className="form-control"
                    name="guildId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="commandNameLabel" for="commandName">
                    <Translate contentKey="siaApp.customCommand.commandName">Command Name</Translate>
                  </Label>
                  <AvField
                    id="custom-command-commandName"
                    type="text"
                    name="commandName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="messageLabel" for="message">
                    <Translate contentKey="siaApp.customCommand.message">Message</Translate>
                  </Label>
                  <AvInput
                    id="custom-command-message"
                    type="textarea"
                    name="message"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="guildsettings.id">
                    <Translate contentKey="siaApp.customCommand.guildsettings">Guildsettings</Translate>
                  </Label>
                  <AvInput id="custom-command-guildsettings" type="select" className="form-control" name="guildsettings.id">
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
                <Button tag={Link} id="cancel-save" to="/entity/custom-command" replace color="info">
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
  customCommandEntity: storeState.customCommand.entity,
  loading: storeState.customCommand.loading,
  updating: storeState.customCommand.updating,
  updateSuccess: storeState.customCommand.updateSuccess
});

const mapDispatchToProps = {
  getGuildSettings,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomCommandUpdate);
