import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './auto-mod.reducer';
import { IAutoMod } from 'app/shared/model/auto-mod.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAutoModDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AutoModDetail extends React.Component<IAutoModDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { autoModEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.autoMod.detail.title">AutoMod</Translate> [<b>{autoModEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guildId">
                <Translate contentKey="siaApp.autoMod.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{autoModEntity.guildId}</dd>
            <dt>
              <span id="resolveUrls">
                <Translate contentKey="siaApp.autoMod.resolveUrls">Resolve Urls</Translate>
              </span>
            </dt>
            <dd>{autoModEntity.resolveUrls ? 'true' : 'false'}</dd>
            <dt>
              <span id="maxMentions">
                <Translate contentKey="siaApp.autoMod.maxMentions">Max Mentions</Translate>
              </span>
            </dt>
            <dd>{autoModEntity.maxMentions}</dd>
            <dt>
              <span id="maxRoleMentions">
                <Translate contentKey="siaApp.autoMod.maxRoleMentions">Max Role Mentions</Translate>
              </span>
            </dt>
            <dd>{autoModEntity.maxRoleMentions}</dd>
            <dt>
              <span id="maxLines">
                <Translate contentKey="siaApp.autoMod.maxLines">Max Lines</Translate>
              </span>
            </dt>
            <dd>{autoModEntity.maxLines}</dd>
            <dt>
              <span id="raidModeNumber">
                <Translate contentKey="siaApp.autoMod.raidModeNumber">Raid Mode Number</Translate>
              </span>
            </dt>
            <dd>{autoModEntity.raidModeNumber}</dd>
            <dt>
              <span id="raidModeTime">
                <Translate contentKey="siaApp.autoMod.raidModeTime">Raid Mode Time</Translate>
              </span>
            </dt>
            <dd>{autoModEntity.raidModeTime}</dd>
            <dt>
              <span id="inviteStrikes">
                <Translate contentKey="siaApp.autoMod.inviteStrikes">Invite Strikes</Translate>
              </span>
            </dt>
            <dd>{autoModEntity.inviteStrikes}</dd>
            <dt>
              <span id="refStrikes">
                <Translate contentKey="siaApp.autoMod.refStrikes">Ref Strikes</Translate>
              </span>
            </dt>
            <dd>{autoModEntity.refStrikes}</dd>
            <dt>
              <span id="copyPastaStrikes">
                <Translate contentKey="siaApp.autoMod.copyPastaStrikes">Copy Pasta Strikes</Translate>
              </span>
            </dt>
            <dd>{autoModEntity.copyPastaStrikes}</dd>
            <dt>
              <span id="everyoneStrikes">
                <Translate contentKey="siaApp.autoMod.everyoneStrikes">Everyone Strikes</Translate>
              </span>
            </dt>
            <dd>{autoModEntity.everyoneStrikes}</dd>
            <dt>
              <span id="dupeStrikes">
                <Translate contentKey="siaApp.autoMod.dupeStrikes">Dupe Strikes</Translate>
              </span>
            </dt>
            <dd>{autoModEntity.dupeStrikes}</dd>
            <dt>
              <span id="dupeDeleteThresh">
                <Translate contentKey="siaApp.autoMod.dupeDeleteThresh">Dupe Delete Thresh</Translate>
              </span>
            </dt>
            <dd>{autoModEntity.dupeDeleteThresh}</dd>
            <dt>
              <span id="dupeStrikesThresh">
                <Translate contentKey="siaApp.autoMod.dupeStrikesThresh">Dupe Strikes Thresh</Translate>
              </span>
            </dt>
            <dd>{autoModEntity.dupeStrikesThresh}</dd>
            <dt>
              <span id="dehoistChar">
                <Translate contentKey="siaApp.autoMod.dehoistChar">Dehoist Char</Translate>
              </span>
            </dt>
            <dd>{autoModEntity.dehoistChar}</dd>
          </dl>
          <Button tag={Link} to="/entity/auto-mod" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/auto-mod/${autoModEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ autoMod }: IRootState) => ({
  autoModEntity: autoMod.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AutoModDetail);
