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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Request {
    private static final Logger log = LoggerFactory.getLogger(Request.class);

    private String url;

    /**
     * Constructs a new request object.
     *
     * @param url the URL.
     */
    public Request(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

//    public static Object getResponse(String url, HashMap<String, String> params,
//                                     RequestSender sender, Gson gson, Class tClass) throws NasaApi4JException {
//        Request request = new Request(UrlUtil.buildUrlQuery(url, params));
//        try {
//            Response response = sender.sendRequest(request);
//            TypeToken<Object> token = new TypeToken<Object>() {};
//            Object obj = gson.fromJson(response.getBody(), tClass);
//            return obj;
//        } catch (JsonSyntaxException | IOException e) {
//            log.error(e.getMessage(), e);
//            throw new NasaApi4JException(e);
//        }
//
//    }
}
