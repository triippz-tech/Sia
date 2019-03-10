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

package com.trievosoftware.application.rest.http;

import java.util.Map;

public class Response {
    private Map<String, String> headers;
    private String body;

    /**
     * Constructs a new response.
     *
     * @param headers
     *            the header map
     * @param body
     *            the body.
     */
    public Response(Map<String, String> headers, String body) {
        this.headers = headers;
        this.body = body;
    }

    /**
     * Returns the header map.
     *
     * @return the header map
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Returns the body.
     *
     * @return the body.
     */
    public String getBody() {
        return body;
    }
}
