import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name={translate('global.menu.entities.main')} id="entity-menu">
    <DropdownItem tag={Link} to="/entity/audit-cache">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;
      <Translate contentKey="global.menu.entities.auditCache" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/auto-mod">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;
      <Translate contentKey="global.menu.entities.autoMod" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/guild-settings">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;
      <Translate contentKey="global.menu.entities.guildSettings" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/ignored">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;
      <Translate contentKey="global.menu.entities.ignored" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/premium">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;
      <Translate contentKey="global.menu.entities.premium" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/actions">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;
      <Translate contentKey="global.menu.entities.actions" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/strikes">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;
      <Translate contentKey="global.menu.entities.strikes" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/temp-mutes">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;
      <Translate contentKey="global.menu.entities.tempMutes" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/temp-bans">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;
      <Translate contentKey="global.menu.entities.tempBans" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/guild-music-settings">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;
      <Translate contentKey="global.menu.entities.guildMusicSettings" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/playlist">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;
      <Translate contentKey="global.menu.entities.playlist" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/songs">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;
      <Translate contentKey="global.menu.entities.songs" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/poll">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;
      <Translate contentKey="global.menu.entities.poll" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/poll-items">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;
      <Translate contentKey="global.menu.entities.pollItems" />
    </DropdownItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
