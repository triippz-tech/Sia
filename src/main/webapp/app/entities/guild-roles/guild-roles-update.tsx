import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICustomCommand } from 'app/shared/model/custom-command.model';
import { getEntities as getCustomCommands } from 'app/entities/custom-command/custom-command.reducer';
import { getEntity, updateEntity, createEntity, reset } from './guild-roles.reducer';
import { IGuildRoles } from 'app/shared/model/guild-roles.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGuildRolesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IGuildRolesUpdateState {
  isNew: boolean;
  customcommandId: string;
}

export class GuildRolesUpdate extends React.Component<IGuildRolesUpdateProps, IGuildRolesUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      customcommandId: '0',
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

    this.props.getCustomCommands();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { guildRolesEntity } = this.props;
      const entity = {
        ...guildRolesEntity,
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
    this.props.history.push('/entity/guild-roles');
  };

  render() {
    const { guildRolesEntity, customCommands, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="siaApp.guildRoles.home.createOrEditLabel">
              <Translate contentKey="siaApp.guildRoles.home.createOrEditLabel">Create or edit a GuildRoles</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : guildRolesEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="guild-roles-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="guildIdLabel" for="guildId">
                    <Translate contentKey="siaApp.guildRoles.guildId">Guild Id</Translate>
                  </Label>
                  <AvField
                    id="guild-roles-guildId"
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
                  <Label id="roleIdLabel" for="roleId">
                    <Translate contentKey="siaApp.guildRoles.roleId">Role Id</Translate>
                  </Label>
                  <AvField
                    id="guild-roles-roleId"
                    type="string"
                    className="form-control"
                    name="roleId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="roleNameLabel" for="roleName">
                    <Translate contentKey="siaApp.guildRoles.roleName">Role Name</Translate>
                  </Label>
                  <AvField id="guild-roles-roleName" type="text" name="roleName" />
                </AvGroup>
                <AvGroup>
                  <Label for="customcommand.id">
                    <Translate contentKey="siaApp.guildRoles.customcommand">Customcommand</Translate>
                  </Label>
                  <AvInput id="guild-roles-customcommand" type="select" className="form-control" name="customcommand.id">
                    <option value="" key="0" />
                    {customCommands
                      ? customCommands.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/guild-roles" replace color="info">
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
  customCommands: storeState.customCommand.entities,
  guildRolesEntity: storeState.guildRoles.entity,
  loading: storeState.guildRoles.loading,
  updating: storeState.guildRoles.updating,
  updateSuccess: storeState.guildRoles.updateSuccess
});

const mapDispatchToProps = {
  getCustomCommands,
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
)(GuildRolesUpdate);
