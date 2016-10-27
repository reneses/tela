package io.reneses.tela.core.util;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * JettyHttpClient class.
 */
public class JettyHttpClient implements HttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(JettyHttpClient.class);

    private static org.eclipse.jetty.client.HttpClient httpClient;
    static {
        httpClient = new org.eclipse.jetty.client.HttpClient(new SslContextFactory());
        try {
            httpClient.start();
        } catch (Exception e) {
            LOGGER.error("The Jetty HTTP client could not be started", e);
            System.exit(-1);
        }
    }

    private Request createRequest(String url) {
        return httpClient
                .newRequest(url)
                .agent(USER_AGENT);
    }

    private Request createAuthorizedRequest(String bearerToken, String url) {
        return createRequest(url)
                .header(HttpHeader.AUTHORIZATION, "Bearer " + bearerToken);
    }

    private String sendRequest(HttpMethod httpMethod, String bearerToken, String url, String... params) throws InterruptedException, ExecutionException, TimeoutException {
        if (params.length % 2 != 0)
            throw new IllegalArgumentException("There has to be an even number of parameters!");
        Request request = bearerToken != null ? createAuthorizedRequest(bearerToken, url) : createRequest(url);
        request.method(httpMethod);
        if (params.length > 0) {
            for (int i = 0; i <= params.length / 2; i += 2)
                request.param(params[i], params[i + 1]);
        }
        LOGGER.debug("[{}] {}", httpMethod.name(), request.getURI());
        return request.send().getContentAsString();
    }

    /** {@inheritDoc} */
    @Override
    public String getRequest(String url, String... params) throws InterruptedException, ExecutionException, TimeoutException {
        return sendRequest(HttpMethod.GET, null, url, params);
    }

    /** {@inheritDoc} */
    @Override
    public String authorizedGetRequest(String bearerToken, String url, String... params) throws InterruptedException, ExecutionException, TimeoutException {
        return sendRequest(HttpMethod.GET, bearerToken, url, params);
    }

    /** {@inheritDoc} */
    @Override
    public String postRequest(String url, String... params) throws InterruptedException, ExecutionException, TimeoutException {
        return sendRequest(HttpMethod.POST, null, url, params);
    }

    /** {@inheritDoc} */
    @Override
    public String authorizedPostRequest(String bearerToken, String url, String... params) throws InterruptedException, ExecutionException, TimeoutException {
        return sendRequest(HttpMethod.POST, bearerToken, url, params);
    }

}