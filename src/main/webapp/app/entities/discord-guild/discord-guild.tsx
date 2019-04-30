import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction, getSortState, IPaginationBaseState, getPaginationItemsNumber, JhiPagination } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './discord-guild.reducer';
import { IDiscordGuild } from 'app/shared/model/discord-guild.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IDiscordGuildProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IDiscordGuildState = IPaginationBaseState;

export class DiscordGuild extends React.Component<IDiscordGuildProps, IDiscordGuildState> {
  state: IDiscordGuildState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getEntities();
  }

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { discordGuildList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="discord-guild-heading">
          <Translate contentKey="siaApp.discordGuild.home.title">Discord Guilds</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="siaApp.discordGuild.home.createLabel">Create new Discord Guild</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={this.sort('id')}>
                  <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('guildId')}>
                  <Translate contentKey="siaApp.discordGuild.guildId">Guild Id</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('guildName')}>
                  <Translate contentKey="siaApp.discordGuild.guildName">Guild Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('inviteLink')}>
                  <Translate contentKey="siaApp.discordGuild.inviteLink">Invite Link</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="siaApp.discordGuild.guildSettings">Guild Settings</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="siaApp.discordGuild.auditCache">Audit Cache</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="siaApp.discordGuild.autoMod">Auto Mod</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="siaApp.discordGuild.guildMusicSettings">Guild Music Settings</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {discordGuildList.map((discordGuild, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${discordGuild.id}`} color="link" size="sm">
                      {discordGuild.id}
                    </Button>
                  </td>
                  <td>{discordGuild.guildId}</td>
                  <td>{discordGuild.guildName}</td>
                  <td>{discordGuild.inviteLink}</td>
                  <td>
                    {discordGuild.guildSettings ? (
                      <Link to={`guild-settings/${discordGuild.guildSettings.id}`}>{discordGuild.guildSettings.guildId}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {discordGuild.auditCache ? (
                      <Link to={`audit-cache/${discordGuild.auditCache.id}`}>{discordGuild.auditCache.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{discordGuild.autoMod ? <Link to={`auto-mod/${discordGuild.autoMod.id}`}>{discordGuild.autoMod.id}</Link> : ''}</td>
                  <td>
                    {discordGuild.guildMusicSettings ? (
                      <Link to={`guild-music-settings/${discordGuild.guildMusicSettings.id}`}>{discordGuild.guildMusicSettings.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${discordGuild.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${discordGuild.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${discordGuild.id}/delete`} color="danger" size="sm">
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
        <Row className="justify-content-center">
          <JhiPagination
            items={getPaginationItemsNumber(totalItems, this.state.itemsPerPage)}
            activePage={this.state.activePage}
            onSelect={this.handlePagination}
            maxButtons={5}
          />
        </Row>
      </div>
    );
  }
}

const mapStateToProps = ({ discordGuild }: IRootState) => ({
  discordGuildList: discordGuild.entities,
  totalItems: discordGuild.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DiscordGuild);
