import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './audit-cache.reducer';
import { IAuditCache } from 'app/shared/model/audit-cache.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAuditCacheDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AuditCacheDetail extends React.Component<IAuditCacheDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { auditCacheEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="siaApp.auditCache.detail.title">AuditCache</Translate> [<b>{auditCacheEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guildId">
                <Translate contentKey="siaApp.auditCache.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{auditCacheEntity.guildId}</dd>
            <dt>
              <span id="old">
                <Translate contentKey="siaApp.auditCache.old">Old</Translate>
              </span>
            </dt>
            <dd>{auditCacheEntity.old}</dd>
            <dt>
              <span id="older">
                <Translate contentKey="siaApp.auditCache.older">Older</Translate>
              </span>
            </dt>
            <dd>{auditCacheEntity.older}</dd>
            <dt>
              <span id="oldest">
                <Translate contentKey="siaApp.auditCache.oldest">Oldest</Translate>
              </span>
            </dt>
            <dd>{auditCacheEntity.oldest}</dd>
          </dl>
          <Button tag={Link} to="/entity/audit-cache" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/audit-cache/${auditCacheEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ auditCache }: IRootState) => ({
  auditCacheEntity: auditCache.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AuditCacheDetail);
