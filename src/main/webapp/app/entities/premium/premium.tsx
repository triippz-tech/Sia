import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './premium.reducer';
import { IPremium } from 'app/shared/model/premium.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPremiumProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Premium extends React.Component<IPremiumProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { premiumList, match } = this.props;
    return (
      <div>
        <h2 id="premium-heading">
          <Translate contentKey="siaApp.premium.home.title">Premiums</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="siaApp.premium.home.createLabel">Create new Premium</Translate>
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
                  <Translate contentKey="siaApp.premium.discordId">Guild/User Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.premium.until">Until</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.premium.level">Level</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {premiumList.map((premium, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${premium.id}`} color="link" size="sm">
                      {premium.id}
                    </Button>
                  </td>
                  <td>{premium.discordId}</td>
                  <td>
                    <TextFormat type="date" value={premium.until} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{premium.level}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${premium.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${premium.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${premium.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ premium }: IRootState) => ({
  premiumList: premium.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Premium);
