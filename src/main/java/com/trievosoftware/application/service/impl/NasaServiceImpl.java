/*
 *    Copyright 2019 Mark Tripoli
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.trievosoftware.application.service.impl;

import com.trievosoftware.application.config.ApplicationProperties;
import com.trievosoftware.application.service.NasaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NasaServiceImpl implements NasaService {

    private final Logger log = LoggerFactory.getLogger(NasaServiceImpl.class);
    ApplicationProperties applicationProperties;

    private final String NASA_API_KEY;
    private String NASA_ENDPOINT = "https://api.nasa.gov/planetary/apod?api_key=";

    public NasaServiceImpl(ApplicationProperties applicationProperties) {

        this.applicationProperties = applicationProperties;
        NASA_API_KEY = this.applicationProperties.getApi().getNasa();
    }


}
