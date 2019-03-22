import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './poll.reducer';
import { IPoll } from 'app/shared/model/poll.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPollProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Poll extends React.Component<IPollProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { pollList, match } = this.props;
    return (
      <div>
        <h2 id="poll-heading">
          <Translate contentKey="siaApp.poll.home.title">Polls</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="siaApp.poll.home.createLabel">Create new Poll</Translate>
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
                  <Translate contentKey="siaApp.poll.guildId">Guild Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.poll.userId">User Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.poll.textChannelId">Text Channel Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.poll.messageId">Message Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.poll.title">Title</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.poll.expired">Is Expired</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.poll.finishTime">Finish Time</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {pollList.map((poll, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${poll.id}`} color="link" size="sm">
                      {poll.id}
                    </Button>
                  </td>
                  <td>{poll.guildId}</td>
                  <td>{poll.userId}</td>
                  <td>{poll.textChannelId}</td>
                  <td>{poll.messageId}</td>
                  <td>{poll.title}</td>
                  <td>{poll.expired}</td>
                  <td>
                    <TextFormat type="date" value={poll.finishTime} format={APP_DATE_FORMAT} />
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${poll.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${poll.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${poll.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ poll }: IRootState) => ({
  pollList: poll.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Poll);
