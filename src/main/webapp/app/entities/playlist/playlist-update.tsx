import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IGuildMusicSettings } from 'app/shared/model/guild-music-settings.model';
import { getEntities as getGuildMusicSettings } from 'app/entities/guild-music-settings/guild-music-settings.reducer';
import { getEntity, updateEntity, createEntity, reset } from './playlist.reducer';
import { IPlaylist } from 'app/shared/model/playlist.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPlaylistUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPlaylistUpdateState {
  isNew: boolean;
  guildmusicsettingsId: string;
}

export class PlaylistUpdate extends React.Component<IPlaylistUpdateProps, IPlaylistUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      guildmusicsettingsId: '0',
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

    this.props.getGuildMusicSettings();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { playlistEntity } = this.props;
      const entity = {
        ...playlistEntity,
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
    this.props.history.push('/entity/playlist');
  };

  render() {
    const { playlistEntity, guildMusicSettings, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="siaApp.playlist.home.createOrEditLabel">
              <Translate contentKey="siaApp.playlist.home.createOrEditLabel">Create or edit a Playlist</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : playlistEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="playlist-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="guildIdLabel" for="guildId">
                    <Translate contentKey="siaApp.playlist.guildId">Guild Id</Translate>
                  </Label>
                  <AvField id="playlist-guildId" type="string" className="form-control" name="guildId" />
                </AvGroup>
                <AvGroup>
                  <Label id="userIdLabel" for="userId">
                    <Translate contentKey="siaApp.playlist.userId">User Id</Translate>
                  </Label>
                  <AvField id="playlist-userId" type="string" className="form-control" name="userId" />
                </AvGroup>
                <AvGroup>
                  <Label id="playlistNameLabel" for="playlistName">
                    <Translate contentKey="siaApp.playlist.playlistName">Playlist Name</Translate>
                  </Label>
                  <AvField
                    id="playlist-playlistName"
                    type="text"
                    name="playlistName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/playlist" replace color="info">
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
  guildMusicSettings: storeState.guildMusicSettings.entities,
  playlistEntity: storeState.playlist.entity,
  loading: storeState.playlist.loading,
  updating: storeState.playlist.updating,
  updateSuccess: storeState.playlist.updateSuccess
});

const mapDispatchToProps = {
  getGuildMusicSettings,
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
)(PlaylistUpdate);
