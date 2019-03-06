import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './strikes.reducer';
import { IStrikes } from 'app/shared/model/strikes.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStrikesProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Strikes extends React.Component<IStrikesProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { strikesList, match } = this.props;
    return (
      <div>
        <h2 id="strikes-heading">
          <Translate contentKey="siaApp.strikes.home.title">Strikes</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="siaApp.strikes.home.createLabel">Create new Strikes</Translate>
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
                  <Translate contentKey="siaApp.strikes.guildId">Guild Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.strikes.userId">User Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.strikes.strikes">Strikes</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {strikesList.map((strikes, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${strikes.id}`} color="link" size="sm">
                      {strikes.id}
                    </Button>
                  </td>
                  <td>{strikes.guildId}</td>
                  <td>{strikes.userId}</td>
                  <td>{strikes.strikes}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${strikes.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${strikes.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${strikes.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ strikes }: IRootState) => ({
  strikesList: strikes.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Strikes);
