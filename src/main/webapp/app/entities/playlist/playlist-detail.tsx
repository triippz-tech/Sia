import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './playlist.reducer';
import { IPlaylist } from 'app/shared/model/playlist.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPlaylistDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PlaylistDetail extends React.Component<IPlaylistDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { playlistEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.playlist.detail.title">Playlist</Translate> [<b>{playlistEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guildId">
                <Translate contentKey="siaApp.playlist.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{playlistEntity.guildId}</dd>
            <dt>
              <span id="userId">
                <Translate contentKey="siaApp.playlist.userId">User Id</Translate>
              </span>
            </dt>
            <dd>{playlistEntity.userId}</dd>
            <dt>
              <span id="playlistName">
                <Translate contentKey="siaApp.playlist.playlistName">Playlist Name</Translate>
              </span>
            </dt>
            <dd>{playlistEntity.playlistName}</dd>
          </dl>
          <Button tag={Link} to="/entity/playlist" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/playlist/${playlistEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ playlist }: IRootState) => ({
  playlistEntity: playlist.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PlaylistDetail);
