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

import com.trievosoftware.application.rest.RestConstants;
import com.trievosoftware.application.rest.http.Request;
import com.trievosoftware.application.rest.http.Response;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestSender implements RequestSender {

    @Override
    public Response sendRequest(Request request) throws IOException {

        HttpURLConnection connection = createConnection(request);

        return send(connection);
    }

    private HttpURLConnection createConnection(Request request) throws IOException {
        URL url = new URL(request.getUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(RestConstants.TIMEOUT);
        connection.setReadTimeout(RestConstants.TIMEOUT);

        connection.setRequestMethod(RestConstants.SEND_METHOD);

        return connection;
    }

    private Response send(HttpURLConnection connection) throws IOException {
        connection.connect();

        if (connection.getResponseCode() != 200) {
            throw new IOException("Bad response! Code: " + connection.getResponseCode());
        }

        Map<String, String> headers = new HashMap<>();
        for (String key : connection.getHeaderFields().keySet()) {
            headers.put(key, connection.getHeaderFields().get(key).get(0));
        }

        String body;

        try (InputStream inputStream = connection.getInputStream()) {

            String encoding = connection.getContentEncoding();
            encoding = encoding == null ? RestConstants.ENCODING : encoding;

            body = IOUtils.toString(inputStream, encoding);
        } catch (IOException e) {
            throw new IOException(e);
        }

        if (body == null) {
            throw new IOException("Unparseable response body! \n {" + body + "}");
        }

        return new Response(headers, body);
    }
}
