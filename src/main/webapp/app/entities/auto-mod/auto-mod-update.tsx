import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './auto-mod.reducer';
import { IAutoMod } from 'app/shared/model/auto-mod.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAutoModUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IAutoModUpdateState {
  isNew: boolean;
}

export class AutoModUpdate extends React.Component<IAutoModUpdateProps, IAutoModUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { autoModEntity } = this.props;
      const entity = {
        ...autoModEntity,
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
    this.props.history.push('/entity/auto-mod');
  };

  render() {
    const { autoModEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="siaApp.autoMod.home.createOrEditLabel">
              <Translate contentKey="siaApp.autoMod.home.createOrEditLabel">Create or edit a AutoMod</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : autoModEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="auto-mod-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="guildIdLabel" for="guildId">
                    <Translate contentKey="siaApp.autoMod.guildId">Guild Id</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-guildId"
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
                  <Label id="resolveUrlsLabel" check>
                    <AvInput id="auto-mod-resolveUrls" type="checkbox" className="form-control" name="resolveUrls" />
                    <Translate contentKey="siaApp.autoMod.resolveUrls">Resolve Urls</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="maxMentionsLabel" for="maxMentions">
                    <Translate contentKey="siaApp.autoMod.maxMentions">Max Mentions</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-maxMentions"
                    type="string"
                    className="form-control"
                    name="maxMentions"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="maxRoleMentionsLabel" for="maxRoleMentions">
                    <Translate contentKey="siaApp.autoMod.maxRoleMentions">Max Role Mentions</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-maxRoleMentions"
                    type="string"
                    className="form-control"
                    name="maxRoleMentions"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="maxLinesLabel" for="maxLines">
                    <Translate contentKey="siaApp.autoMod.maxLines">Max Lines</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-maxLines"
                    type="string"
                    className="form-control"
                    name="maxLines"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="raidModeNumberLabel" for="raidModeNumber">
                    <Translate contentKey="siaApp.autoMod.raidModeNumber">Raid Mode Number</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-raidModeNumber"
                    type="string"
                    className="form-control"
                    name="raidModeNumber"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="raidModeTimeLabel" for="raidModeTime">
                    <Translate contentKey="siaApp.autoMod.raidModeTime">Raid Mode Time</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-raidModeTime"
                    type="string"
                    className="form-control"
                    name="raidModeTime"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="inviteStrikesLabel" for="inviteStrikes">
                    <Translate contentKey="siaApp.autoMod.inviteStrikes">Invite Strikes</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-inviteStrikes"
                    type="string"
                    className="form-control"
                    name="inviteStrikes"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="refStrikesLabel" for="refStrikes">
                    <Translate contentKey="siaApp.autoMod.refStrikes">Ref Strikes</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-refStrikes"
                    type="string"
                    className="form-control"
                    name="refStrikes"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="copyPastaStrikesLabel" for="copyPastaStrikes">
                    <Translate contentKey="siaApp.autoMod.copyPastaStrikes">Copy Pasta Strikes</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-copyPastaStrikes"
                    type="string"
                    className="form-control"
                    name="copyPastaStrikes"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="everyoneStrikesLabel" for="everyoneStrikes">
                    <Translate contentKey="siaApp.autoMod.everyoneStrikes">Everyone Strikes</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-everyoneStrikes"
                    type="string"
                    className="form-control"
                    name="everyoneStrikes"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="dupeStrikesLabel" for="dupeStrikes">
                    <Translate contentKey="siaApp.autoMod.dupeStrikes">Dupe Strikes</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-dupeStrikes"
                    type="string"
                    className="form-control"
                    name="dupeStrikes"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="dupeDeleteThreshLabel" for="dupeDeleteThresh">
                    <Translate contentKey="siaApp.autoMod.dupeDeleteThresh">Dupe Delete Thresh</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-dupeDeleteThresh"
                    type="string"
                    className="form-control"
                    name="dupeDeleteThresh"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="dupeStrikesThreshLabel" for="dupeStrikesThresh">
                    <Translate contentKey="siaApp.autoMod.dupeStrikesThresh">Dupe Strikes Thresh</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-dupeStrikesThresh"
                    type="string"
                    className="form-control"
                    name="dupeStrikesThresh"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="dehoistCharLabel" for="dehoistChar">
                    <Translate contentKey="siaApp.autoMod.dehoistChar">Dehoist Char</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-dehoistChar"
                    type="string"
                    className="form-control"
                    name="dehoistChar"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/auto-mod" replace color="info">
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
  autoModEntity: storeState.autoMod.entity,
  loading: storeState.autoMod.loading,
  updating: storeState.autoMod.updating,
  updateSuccess: storeState.autoMod.updateSuccess
});

const mapDispatchToProps = {
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
)(AutoModUpdate);
