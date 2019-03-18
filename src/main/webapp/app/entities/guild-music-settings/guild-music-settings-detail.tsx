import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './guild-music-settings.reducer';
import { IGuildMusicSettings } from 'app/shared/model/guild-music-settings.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildMusicSettingsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class GuildMusicSettingsDetail extends React.Component<IGuildMusicSettingsDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { guildMusicSettingsEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.guildMusicSettings.detail.title">GuildMusicSettings</Translate> [
            <b>{guildMusicSettingsEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guildId">
                <Translate contentKey="siaApp.guildMusicSettings.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{guildMusicSettingsEntity.guildId}</dd>
            <dt>
              <span id="textChannelId">
                <Translate contentKey="siaApp.guildMusicSettings.textChannelId">Text Channel Id</Translate>
              </span>
            </dt>
            <dd>{guildMusicSettingsEntity.textChannelId}</dd>
            <dt>
              <span id="voiceChannelId">
                <Translate contentKey="siaApp.guildMusicSettings.voiceChannelId">Voice Channel Id</Translate>
              </span>
            </dt>
            <dd>{guildMusicSettingsEntity.voiceChannelId}</dd>
            <dt>
              <span id="djRoleId">
                <Translate contentKey="siaApp.guildMusicSettings.djRoleId">Dj Role Id</Translate>
              </span>
            </dt>
            <dd>{guildMusicSettingsEntity.djRoleId}</dd>
            <dt>
              <span id="volume">
                <Translate contentKey="siaApp.guildMusicSettings.volume">Volume</Translate>
              </span>
            </dt>
            <dd>{guildMusicSettingsEntity.volume}</dd>
            <dt>
              <span id="repeat">
                <Translate contentKey="siaApp.guildMusicSettings.repeat">Repeat</Translate>
              </span>
            </dt>
            <dd>{guildMusicSettingsEntity.repeat ? 'true' : 'false'}</dd>
            <dt>
              <span id="stayInChannel">
                <Translate contentKey="siaApp.guildMusicSettings.stayInChannel">Stay In Channel</Translate>
              </span>
            </dt>
            <dd>{guildMusicSettingsEntity.stayInChannel ? 'true' : 'false'}</dd>
            <dt>
              <span id="songInGame">
                <Translate contentKey="siaApp.guildMusicSettings.songInGame">Song In Game</Translate>
              </span>
            </dt>
            <dd>{guildMusicSettingsEntity.songInGame ? 'true' : 'false'}</dd>
            <dt>
              <span id="nowPlayingImages">
                <Translate contentKey="siaApp.guildMusicSettings.nowPlayingImages">Now Playing Images</Translate>
              </span>
            </dt>
            <dd>{guildMusicSettingsEntity.nowPlayingImages ? 'true' : 'false'}</dd>
            <dd>{guildMusicSettingsEntity.voiceChannelId}</dd>
            <dt>
              <span id="maxSeconds">
                <Translate contentKey="siaApp.guildMusicSettings.maxSeconds">Max Seconds</Translate>
              </span>
            </dt>
            <dd>{guildMusicSettingsEntity.maxSeconds}</dd>
            <dt>
              <Translate contentKey="siaApp.guildMusicSettings.playlist">Playlist</Translate>
            </dt>
            <dd>{guildMusicSettingsEntity.playlist ? guildMusicSettingsEntity.playlist.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/guild-music-settings" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/guild-music-settings/${guildMusicSettingsEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ guildMusicSettings }: IRootState) => ({
  guildMusicSettingsEntity: guildMusicSettings.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildMusicSettingsDetail);
