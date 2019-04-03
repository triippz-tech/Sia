import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './guild-roles.reducer';
import { IGuildRoles } from 'app/shared/model/guild-roles.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildRolesProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class GuildRoles extends React.Component<IGuildRolesProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { guildRolesList, match } = this.props;
    return (
      <div>
        <h2 id="guild-roles-heading">
          <Translate contentKey="siaApp.guildRoles.home.title">Guild Roles</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="siaApp.guildRoles.home.createLabel">Create new Guild Roles</Translate>
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
                  <Translate contentKey="siaApp.guildRoles.guildId">Guild Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildRoles.roleId">Role Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.guildRoles.roleName">Role Name</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {guildRolesList.map((guildRoles, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${guildRoles.id}`} color="link" size="sm">
                      {guildRoles.id}
                    </Button>
                  </td>
                  <td>{guildRoles.guildId}</td>
                  <td>{guildRoles.roleId}</td>
                  <td>{guildRoles.roleName}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${guildRoles.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${guildRoles.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${guildRoles.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ guildRoles }: IRootState) => ({
  guildRolesList: guildRoles.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildRoles);
