import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPlaylist } from 'app/shared/model/playlist.model';
import { getEntities as getPlaylists } from 'app/entities/playlist/playlist.reducer';
import { getEntity, updateEntity, createEntity, reset } from './songs.reducer';
import { ISongs } from 'app/shared/model/songs.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISongsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ISongsUpdateState {
  isNew: boolean;
  playlistId: string;
}

export class SongsUpdate extends React.Component<ISongsUpdateProps, ISongsUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      playlistId: '0',
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

    this.props.getPlaylists();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { songsEntity } = this.props;
      const entity = {
        ...songsEntity,
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
    this.props.history.push('/entity/songs');
  };

  render() {
    const { songsEntity, playlists, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="siaApp.songs.home.createOrEditLabel">
              <Translate contentKey="siaApp.songs.home.createOrEditLabel">Create or edit a Songs</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : songsEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="songs-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="songNameLabel" for="songName">
                    <Translate contentKey="siaApp.songs.songName">Song Name</Translate>
                  </Label>
                  <AvField id="songs-songName" type="text" name="songName" />
                </AvGroup>
                <AvGroup>
                  <Label id="songQueryLabel" for="songQuery">
                    <Translate contentKey="siaApp.songs.songQuery">Song Query</Translate>
                  </Label>
                  <AvField
                    id="songs-songQuery"
                    type="text"
                    name="songQuery"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="playlist.id">
                    <Translate contentKey="siaApp.songs.playlist">Playlist</Translate>
                  </Label>
                  <AvInput
                    id="songs-playlist"
                    type="select"
                    className="form-control"
                    name="playlist.id"
                    value={isNew ? playlists[0] && playlists[0].id : songsEntity.playlist.id}
                  >
                    {playlists
                      ? playlists.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/songs" replace color="info">
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
  playlists: storeState.playlist.entities,
  songsEntity: storeState.songs.entity,
  loading: storeState.songs.loading,
  updating: storeState.songs.updating,
  updateSuccess: storeState.songs.updateSuccess
});

const mapDispatchToProps = {
  getPlaylists,
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
)(SongsUpdate);
