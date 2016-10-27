package io.reneses.tela.core.util;

/**
 * Http Client
 */
public interface HttpClient {

    /** Constant <code>USER_AGENT="Tela Server"</code> */
    String USER_AGENT = "Tela Server";

    /**
     * Execute a GET request
     *
     * @param url Url
     * @param params Parameters, in the form: key1, value1, key2, value2, ...
     * @return Request result
     * @throws Exception During the request
     */
    String getRequest(String url, String... params) throws Exception;

    /**
     * Execute an authorized GET request
     *
     * @param bearerToken Bearer token
     * @param url Url
     * @param params Parameters, in the form: key1, value1, key2, value2, ...
     * @return Request result
     * @throws java.lang.Exception During the request
     */
    String authorizedGetRequest(String bearerToken, String url, String... params) throws Exception;

    /**
     * Execute a POST request
     *
     * @param url Url
     * @param params Parameters, in the form: key1, value1, key2, value2, ...
     * @return Request result
     * @throws Exception During the request
     */
    String postRequest(String url, String... params) throws Exception;

    /**
     * Execute an authorized POST request
     *
     * @param bearerToken Bearer token
     * @param url Url
     * @param params Parameters, in the form: key1, value1, key2, value2, ...
     * @return Request result
     * @throws Exception During the request
     */
    String authorizedPostRequest(String bearerToken, String url, String... params) throws Exception;

}
