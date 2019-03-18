import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './guild-music-settings.reducer';
import { IGuildMusicSettings } from 'app/shared/model/guild-music-settings.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildMusicSettingsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class GuildMusicSettings extends React.Component<IGuildMusicSettingsProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { guildMusicSettingsList, match } = this.props;
    return (
      <div>
        <h2 id="guild-music-settings-heading">
          <Translate contentKey="siaApp.guildMusicSettings.home.title">Guild Music Settings</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="siaApp.guildMusicSettings.home.createLabel">Create new Guild Music Settings</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildMusicSettings.guildId">Guild Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildMusicSettings.textChannelId">Text Channel Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildMusicSettings.voiceChannelId">Voice Channel Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildMusicSettings.djRoleId">Dj Role Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildMusicSettings.volume">Volume</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildMusicSettings.repeat">Repeat</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildMusicSettings.stayInChannel">Stay In Channel</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildMusicSettings.songInGame">Song In Game</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildMusicSettings.nowPlayingImages">Now Playing Images</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildMusicSettings.maxSeconds">Max Seconds</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildMusicSettings.playlist">Playlist</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {guildMusicSettingsList.map((guildMusicSettings, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${guildMusicSettings.id}`} color="link" size="sm">
                      {guildMusicSettings.id}
                    </Button>
                  </td>
                  <td>{guildMusicSettings.guildId}</td>
                  <td>{guildMusicSettings.textChannelId}</td>
                  <td>{guildMusicSettings.voiceChannelId}</td>
                  <td>{guildMusicSettings.djRoleId}</td>
                  <td>{guildMusicSettings.volume}</td>
                  <td>{guildMusicSettings.stayInChannel ? 'true' : 'false'}</td>
                  <td>{guildMusicSettings.songInGame ? 'true' : 'false'}</td>
                  <td>{guildMusicSettings.nowPlayingImages ? 'true' : 'false'}</td>
                  <td>{guildMusicSettings.maxSeconds}</td>
                  <td>{guildMusicSettings.repeat ? 'true' : 'false'}</td>
                  <td>
                    {guildMusicSettings.playlist ? (
                      <Link to={`playlist/${guildMusicSettings.playlist.id}`}>{guildMusicSettings.playlist.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${guildMusicSettings.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${guildMusicSettings.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${guildMusicSettings.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ guildMusicSettings }: IRootState) => ({
  guildMusicSettingsList: guildMusicSettings.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildMusicSettings);
