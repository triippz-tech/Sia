import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './playlist.reducer';
import { IPlaylist } from 'app/shared/model/playlist.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPlaylistProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Playlist extends React.Component<IPlaylistProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { playlistList, match } = this.props;
    return (
      <div>
        <h2 id="playlist-heading">
          <Translate contentKey="siaApp.playlist.home.title">Playlists</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="siaApp.playlist.home.createLabel">Create new Playlist</Translate>
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
                  <Translate contentKey="siaApp.playlist.guildId">Guild Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.playlist.userId">User Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.playlist.playlistName">Playlist Name</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {playlistList.map((playlist, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${playlist.id}`} color="link" size="sm">
                      {playlist.id}
                    </Button>
                  </td>
                  <td>{playlist.guildId}</td>
                  <td>{playlist.userId}</td>
                  <td>{playlist.playlistName}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${playlist.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${playlist.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${playlist.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ playlist }: IRootState) => ({
  playlistList: playlist.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Playlist);
