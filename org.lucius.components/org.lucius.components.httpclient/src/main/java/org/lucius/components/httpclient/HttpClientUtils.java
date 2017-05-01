/**
 * @(#)HttpClientUtils.java 1.0 2017年4月26日
 * @Copyright:  Copyright 2007 - 2017 
 * @Description: 
 * 
 * Modification History:
 * Date:        2017年4月26日
 * Author:      lucius.lv
 * Version:     1.0.0.0
 * Description: (Initialize)
 * Reviewer:    
 * Review Date: 
 */
package org.lucius.components.httpclient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.lucius.components.data.converter.json.JsonUtils;

import com.fasterxml.jackson.core.type.TypeReference;

public class HttpClientUtils {

    public <T> T doGet(String url, Class<T> clazz) throws ClientProtocolException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        ResponseHandler<T> rh = new ResponseHandler<T>() {
            @Override
            public T handleResponse(final HttpResponse response)
                    throws IOException {
                StatusLine statusLine = response.getStatusLine();
                HttpEntity entity = response.getEntity();
                if (statusLine.getStatusCode() >= 300) {
                    throw new HttpResponseException(statusLine.getStatusCode(),
                            statusLine.getReasonPhrase());
                }
                if (entity == null) {
                    throw new ClientProtocolException(
                            "Response contains no content");
                }
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                Reader reader = new InputStreamReader(entity.getContent(),
                        charset);
                return JsonUtils.fromJson(reader, clazz);
            }
        };
        T myjson = httpclient.execute(httpget, rh);
        return myjson;
    }
    
    public <T> T doGet(String url, TypeReference<T> typeReference) throws ClientProtocolException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        ResponseHandler<T> rh = new ResponseHandler<T>() {
            @Override
            public T handleResponse(final HttpResponse response)
                    throws IOException {
                StatusLine statusLine = response.getStatusLine();
                HttpEntity entity = response.getEntity();
                if (statusLine.getStatusCode() >= 300) {
                    throw new HttpResponseException(statusLine.getStatusCode(),
                            statusLine.getReasonPhrase());
                }
                if (entity == null) {
                    throw new ClientProtocolException(
                            "Response contains no content");
                }
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                Reader reader = new InputStreamReader(entity.getContent(),
                        charset);
                return JsonUtils.fromJson(reader, typeReference);
            }
        };
        T myjson = httpclient.execute(httpget, rh);
        return myjson;
    }

}
