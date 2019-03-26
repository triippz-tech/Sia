import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './poll-items.reducer';
import { IPollItems } from 'app/shared/model/poll-items.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPollItemsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class PollItems extends React.Component<IPollItemsProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { pollItemsList, match } = this.props;
    return (
      <div>
        <h2 id="poll-items-heading">
          <Translate contentKey="siaApp.pollItems.home.title">Poll Items</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="siaApp.pollItems.home.createLabel">Create new Poll Items</Translate>
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
                  <Translate contentKey="siaApp.pollItems.itemName">Item Name</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.pollItems.reaction">Reaction String</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.pollItems.votes">Votes</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.pollItems.discorduser">Discorduser</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.pollItems.poll">Poll</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {pollItemsList.map((pollItems, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${pollItems.id}`} color="link" size="sm">
                      {pollItems.id}
                    </Button>
                  </td>
                  <td>{pollItems.itemName}</td>
                  <td>{pollItems.reaction}</td>
                  <td>{pollItems.votes}</td>
                  <td>
                    {pollItems.discordusers
                      ? pollItems.discordusers.map((val, j) => (
                          <span key={j}>
                            <Link to={`discord-user/${val.id}`}>{val.id}</Link>
                            {j === pollItems.discordusers.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>{pollItems.poll ? <Link to={`poll/${pollItems.poll.id}`}>{pollItems.poll.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${pollItems.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${pollItems.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${pollItems.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ pollItems }: IRootState) => ({
  pollItemsList: pollItems.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PollItems);
