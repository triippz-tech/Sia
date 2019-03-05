import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './auto-mod.reducer';
import { IAutoMod } from 'app/shared/model/auto-mod.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAutoModProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class AutoMod extends React.Component<IAutoModProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { autoModList, match } = this.props;
    return (
      <div>
        <h2 id="auto-mod-heading">
          <Translate contentKey="siaApp.autoMod.home.title">Auto Mods</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="siaApp.autoMod.home.createLabel">Create new Auto Mod</Translate>
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
                  <Translate contentKey="siaApp.autoMod.guildId">Guild Id</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.autoMod.resolveUrls">Resolve Urls</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.autoMod.maxMentions">Max Mentions</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.autoMod.maxRoleMentions">Max Role Mentions</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.autoMod.maxLines">Max Lines</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.autoMod.raidModeNumber">Raid Mode Number</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.autoMod.raidModeTime">Raid Mode Time</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.autoMod.inviteStrikes">Invite Strikes</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.autoMod.refStrikes">Ref Strikes</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.autoMod.copyPastaStrikes">Copy Pasta Strikes</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.autoMod.everyoneStrikes">Everyone Strikes</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.autoMod.dupeStrikes">Dupe Strikes</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.autoMod.dupeDeleteThresh">Dupe Delete Thresh</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.autoMod.dupeStrikesThresh">Dupe Strikes Thresh</Translate>
                </th>
                <th>
                  <Translate contentKey="siaApp.autoMod.dehoistChar">Dehoist Char</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {autoModList.map((autoMod, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${autoMod.id}`} color="link" size="sm">
                      {autoMod.id}
                    </Button>
                  </td>
                  <td>{autoMod.guildId}</td>
                  <td>{autoMod.resolveUrls ? 'true' : 'false'}</td>
                  <td>{autoMod.maxMentions}</td>
                  <td>{autoMod.maxRoleMentions}</td>
                  <td>{autoMod.maxLines}</td>
                  <td>{autoMod.raidModeNumber}</td>
                  <td>{autoMod.raidModeTime}</td>
                  <td>{autoMod.inviteStrikes}</td>
                  <td>{autoMod.refStrikes}</td>
                  <td>{autoMod.copyPastaStrikes}</td>
                  <td>{autoMod.everyoneStrikes}</td>
                  <td>{autoMod.dupeStrikes}</td>
                  <td>{autoMod.dupeDeleteThresh}</td>
                  <td>{autoMod.dupeStrikesThresh}</td>
                  <td>{autoMod.dehoistChar}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${autoMod.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${autoMod.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${autoMod.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ autoMod }: IRootState) => ({
  autoModList: autoMod.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AutoMod);
