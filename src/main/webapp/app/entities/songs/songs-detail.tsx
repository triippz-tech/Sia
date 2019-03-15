import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './songs.reducer';
import { ISongs } from 'app/shared/model/songs.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISongsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class SongsDetail extends React.Component<ISongsDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { songsEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.songs.detail.title">Songs</Translate> [<b>{songsEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="songName">
                <Translate contentKey="siaApp.songs.songName">Song Name</Translate>
              </span>
            </dt>
            <dd>{songsEntity.songName}</dd>
            <dt>
              <span id="songQuery">
                <Translate contentKey="siaApp.songs.songQuery">Song Query</Translate>
              </span>
            </dt>
            <dd>{songsEntity.songQuery}</dd>
            <dt>
              <Translate contentKey="siaApp.songs.playlist">Playlist</Translate>
            </dt>
            <dd>{songsEntity.playlist ? songsEntity.playlist.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/songs" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/songs/${songsEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ songs }: IRootState) => ({
  songsEntity: songs.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SongsDetail);
