import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './guild-roles.reducer';
import { IGuildRoles } from 'app/shared/model/guild-roles.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildRolesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class GuildRolesDetail extends React.Component<IGuildRolesDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { guildRolesEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.guildRoles.detail.title">GuildRoles</Translate> [<b>{guildRolesEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guildId">
                <Translate contentKey="siaApp.guildRoles.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{guildRolesEntity.guildId}</dd>
            <dt>
              <span id="roleId">
                <Translate contentKey="siaApp.guildRoles.roleId">Role Id</Translate>
              </span>
            </dt>
            <dd>{guildRolesEntity.roleId}</dd>
            <dt>
              <span id="roleName">
                <Translate contentKey="siaApp.guildRoles.roleName">Role Name</Translate>
              </span>
            </dt>
            <dd>{guildRolesEntity.roleName}</dd>
          </dl>
          <Button tag={Link} to="/entity/guild-roles" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/guild-roles/${guildRolesEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ guildRoles }: IRootState) => ({
  guildRolesEntity: guildRoles.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildRolesDetail);
