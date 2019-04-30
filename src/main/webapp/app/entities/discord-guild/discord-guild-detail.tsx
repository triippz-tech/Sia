import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './discord-guild.reducer';
import { IDiscordGuild } from 'app/shared/model/discord-guild.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDiscordGuildDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class DiscordGuildDetail extends React.Component<IDiscordGuildDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { discordGuildEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.discordGuild.detail.title">DiscordGuild</Translate> [<b>{discordGuildEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guildId">
                <Translate contentKey="siaApp.discordGuild.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{discordGuildEntity.guildId}</dd>
            <dt>
              <span id="guildName">
                <Translate contentKey="siaApp.discordGuild.guildName">Guild Name</Translate>
              </span>
            </dt>
            <dd>{discordGuildEntity.guildName}</dd>
            <dt>
              <span id="inviteLink">
                <Translate contentKey="siaApp.discordGuild.inviteLink">Invite Link</Translate>
              </span>
            </dt>
            <dd>{discordGuildEntity.inviteLink}</dd>
            <dt>
              <Translate contentKey="siaApp.discordGuild.guildSettings">Guild Settings</Translate>
            </dt>
            <dd>{discordGuildEntity.guildSettings ? discordGuildEntity.guildSettings.guildId : ''}</dd>
            <dt>
              <Translate contentKey="siaApp.discordGuild.auditCache">Audit Cache</Translate>
            </dt>
            <dd>{discordGuildEntity.auditCache ? discordGuildEntity.auditCache.id : ''}</dd>
            <dt>
              <Translate contentKey="siaApp.discordGuild.autoMod">Auto Mod</Translate>
            </dt>
            <dd>{discordGuildEntity.autoMod ? discordGuildEntity.autoMod.id : ''}</dd>
            <dt>
              <Translate contentKey="siaApp.discordGuild.guildMusicSettings">Guild Music Settings</Translate>
            </dt>
            <dd>{discordGuildEntity.guildMusicSettings ? discordGuildEntity.guildMusicSettings.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/discord-guild" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/discord-guild/${discordGuildEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ discordGuild }: IRootState) => ({
  discordGuildEntity: discordGuild.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DiscordGuildDetail);
