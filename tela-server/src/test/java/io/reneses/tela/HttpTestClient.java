package io.reneses.tela;


import io.reneses.tela.core.sessions.models.Session;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.ws.rs.core.MediaType;

public class HttpTestClient {

    private HttpClient httpClient;

    private String server;

    public HttpTestClient(int port) throws Exception {
        this("http://127.0.0.1", port);
    }

    public HttpTestClient(String host, int port) {
        if (host.endsWith("/"))
            host = host.substring(0, host.lastIndexOf("/"));
        server = host + ":" + port;
        httpClient = new HttpClient(new SslContextFactory());
    }

    public String buildUrl(String endpoint) {
        if (!endpoint.startsWith("/"))
            endpoint = "/" + endpoint;
        return server + endpoint;
    }

    public HttpTestClient start() throws Exception {
        httpClient.start();
        return this;
    }

    public void stop() throws Exception {
        httpClient.stop();
    }

    public ContentResponse getRequest(String endpoint) throws Exception {
        return httpClient.GET(buildUrl(endpoint));
    }

    public ContentResponse getAuthorizedRequest(Session session, String endpoint) throws Exception {
        return httpClient
                .newRequest(buildUrl(endpoint))
                .method(HttpMethod.GET)
                .header(HttpHeader.AUTHORIZATION, "Bearer " + session.getAccessToken())
                .send();
    }

    public ContentResponse postRequest(String endpoint, String ... params) throws Exception {
        Request request = httpClient
                .newRequest(buildUrl(endpoint))
                .method(HttpMethod.POST)
                .header(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED);
        for (int i=0; i<params.length; i += 2)
            request.param(params[i], params[i+1]);
        return request.send();
    }

//    public ContentResponse postRequest(String endpoint, String ... params) throws Exception {
//        Request request = httpClient
//                .newRequest(buildUrl(endpoint))
//                .method(HttpMethod.POST);
//        for (int i=0; i<params.length; i += 2)
//            request.param(params[i], params[i+1]);
//        return request.send();
//    }

    public ContentResponse postAuthorizedRequest(Session session, String endpoint, String ... params) throws Exception {
        Request request = httpClient
                .newRequest(buildUrl(endpoint))
                .method(HttpMethod.POST)
                .header(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)
                .header(HttpHeader.AUTHORIZATION, "Bearer " + session.getAccessToken());
        for (int i=0; i<params.length; i += 2)
            request.param(params[i], params[i+1]);
        return request.send();
    }

    public ContentResponse deleteRequest(String endpoint) throws Exception {
        return httpClient
                .newRequest(buildUrl(endpoint))
                .method(HttpMethod.DELETE)
                .send();
    }

    public ContentResponse deleteAuthorizedRequest(Session session, String endpoint) throws Exception {
        return httpClient
                .newRequest(buildUrl(endpoint))
                .method(HttpMethod.DELETE)
                .header(HttpHeader.AUTHORIZATION, "Bearer " + session.getAccessToken())
                .send();
    }

}
