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
import { getEntity, updateEntity, createEntity, reset } from './guild-music-settings.reducer';
import { IGuildMusicSettings } from 'app/shared/model/guild-music-settings.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGuildMusicSettingsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IGuildMusicSettingsUpdateState {
  isNew: boolean;
  playlistId: string;
}

export class GuildMusicSettingsUpdate extends React.Component<IGuildMusicSettingsUpdateProps, IGuildMusicSettingsUpdateState> {
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
      const { guildMusicSettingsEntity } = this.props;
      const entity = {
        ...guildMusicSettingsEntity,
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
    this.props.history.push('/entity/guild-music-settings');
  };

  render() {
    const { guildMusicSettingsEntity, playlists, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="siaApp.guildMusicSettings.home.createOrEditLabel">
              <Translate contentKey="siaApp.guildMusicSettings.home.createOrEditLabel">Create or edit a GuildMusicSettings</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : guildMusicSettingsEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="guild-music-settings-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="guildIdLabel" for="guildId">
                    <Translate contentKey="siaApp.guildMusicSettings.guildId">Guild Id</Translate>
                  </Label>
                  <AvField
                    id="guild-music-settings-guildId"
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
                  <Label id="textChannelIdLabel" for="textChannelId">
                    <Translate contentKey="siaApp.guildMusicSettings.textChannelId">Text Channel Id</Translate>
                  </Label>
                  <AvField
                    id="guild-music-settings-textChannelId"
                    type="string"
                    className="form-control"
                    name="textChannelId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="voiceChannelIdLabel" for="voiceChannelId">
                    <Translate contentKey="siaApp.guildMusicSettings.voiceChannelId">Voice Channel Id</Translate>
                  </Label>
                  <AvField
                    id="guild-music-settings-voiceChannelId"
                    type="string"
                    className="form-control"
                    name="voiceChannelId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="djRoleIdLabel" for="djRoleId">
                    <Translate contentKey="siaApp.guildMusicSettings.djRoleId">Dj Role Id</Translate>
                  </Label>
                  <AvField
                    id="guild-music-settings-djRoleId"
                    type="string"
                    className="form-control"
                    name="djRoleId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="volumeLabel" for="volume">
                    <Translate contentKey="siaApp.guildMusicSettings.volume">Volume</Translate>
                  </Label>
                  <AvField
                    id="guild-music-settings-volume"
                    type="string"
                    className="form-control"
                    name="volume"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="repeatLabel" check>
                    <AvInput id="guild-music-settings-repeat" type="checkbox" className="form-control" name="repeat" />
                    <Translate contentKey="siaApp.guildMusicSettings.repeat">Repeat</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="stayInChannel" check>
                    <AvInput id="guild-music-settings-stayInChannel" type="checkbox" className="form-control" name="stayInChannel" />
                    <Translate contentKey="siaApp.guildMusicSettings.stayInChannel">Stay In Channel</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="songInGame" check>
                    <AvInput id="guild-music-settings-songInGame" type="checkbox" className="form-control" name="songInGame" />
                    <Translate contentKey="siaApp.guildMusicSettings.songInGame">Song In Game</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="nowPlayingImages" check>
                    <AvInput id="guild-music-settings-nowPlayingImages" type="checkbox" className="form-control" name="nowPlayingImages" />
                    <Translate contentKey="siaApp.guildMusicSettings.nowPlayingImages">Now Playing Images</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="maxSeconds" for="maxSeconds">
                    <Translate contentKey="siaApp.guildMusicSettings.maxSeconds">Max Seconds</Translate>
                  </Label>
                  <AvField
                    id="guild-music-settings-maxSeconds"
                    type="string"
                    className="form-control"
                    name="maxSeconds"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="playlist.id">
                    <Translate contentKey="siaApp.guildMusicSettings.playlist">Playlist</Translate>
                  </Label>
                  <AvInput id="guild-music-settings-playlist" type="select" className="form-control" name="playlist.id">
                    <option value="" key="0" />
                    {playlists
                      ? playlists.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/guild-music-settings" replace color="info">
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
  guildMusicSettingsEntity: storeState.guildMusicSettings.entity,
  loading: storeState.guildMusicSettings.loading,
  updating: storeState.guildMusicSettings.updating,
  updateSuccess: storeState.guildMusicSettings.updateSuccess
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
)(GuildMusicSettingsUpdate);
