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

package com.trievosoftware.application.rest.util;

import com.trievosoftware.application.rest.RestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * This class provides methods to build an URL String.
 * 
 * @author Mark Tripoli
 */
public class UrlUtil {
    private static final Logger log = LoggerFactory.getLogger(UrlUtil.class);


    private static final String PAIR_SEPARATOR = "=";

    private static final String PARAM_SEPARATOR = "&";

    private static final char QUERY_STRING_SEPARATOR = '?';

    /**
     * Builds the URL String.
     * 
     * @param baseUrl
     *            the base URL.
     * @param params
     *            the URL parameter.
     * @return the URL as String.
     */
    public static String buildUrlQuery(String baseUrl, Map<String, String> params) {
	if (baseUrl.isEmpty() || params.isEmpty()) {
	    return baseUrl;
	} else {
	    StringBuilder query = new StringBuilder(baseUrl);
	    query.append(QUERY_STRING_SEPARATOR);

	    for (String key : params.keySet()) {
		query.append(key);
		query.append(PAIR_SEPARATOR);
		query.append(encodeString(params.get(key)));
		query.append(PARAM_SEPARATOR);
	    }

	    return query.substring(0, query.length() - 1);
	}
    }

    private static String encodeString(String text) {
	try {
	    return URLEncoder.encode(text, RestConstants.ENCODING);
	} catch (UnsupportedEncodingException e) {
	    log.error("Error encoding url string: {}", e.getMessage());
	}
	return text;
    }
}
