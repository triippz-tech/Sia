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

package com.trievosoftware.application.rest.dao;

import com.trievosoftware.application.rest.http.Request;
import com.trievosoftware.application.rest.http.Response;

import java.io.IOException;

public interface RequestSender {
    /**
     * Sends the request and returns the received response.
     *
     * @param request
     *            the request which will be send
     * @return the received response
     * @throws IOException
     *             if an error occurred
     */
    public Response sendRequest(Request request) throws IOException;
}
