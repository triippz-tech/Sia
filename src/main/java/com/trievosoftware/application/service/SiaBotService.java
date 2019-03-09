package com.trievosoftware.application.service;

import
    com.trievosoftware.application.config.ApplicationProperties;
import com.trievosoftware.discord.Sia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SiaBotService {

    private final Logger log = LoggerFactory.getLogger(SiaBotService.class);

    private final ServiceManagers databaseManagers;

    private final ApplicationProperties applicationProperties;

    public SiaBotService(ServiceManagers databaseManagers, ApplicationProperties applicationProperties) throws Exception {
        this.databaseManagers = databaseManagers;
        this.applicationProperties = applicationProperties;

        log.info("Launching Sia Bot . . .");
        new Sia(this.databaseManagers, this.applicationProperties);
    }
}
