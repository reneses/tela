package io.reneses.tela.modules.twitter.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reneses.tela.core.util.HttpClient;
import io.reneses.tela.core.util.JettyHttpClient;
import io.reneses.tela.modules.twitter.api.exceptions.TwitterException;
import io.reneses.tela.modules.twitter.api.responses.UsersResponse;
import io.reneses.tela.modules.twitter.models.User;

import java.util.ArrayList;
import java.util.List;


/**
 * Twitter API implementation
 */
class TwitterApiImpl implements TwitterApi {

    private static final String BASE_URL = "https://api.twitter.com/1.1/";
    private HttpClient httpClient;

    TwitterApiImpl() {
        httpClient = new JettyHttpClient();
    }

    TwitterApiImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Construct a full URL given the desired endpoint
     *
     * @param endPoint API Endpoint
     * @return Full url
     */
    private String getUrl(String endPoint) {
        return BASE_URL + endPoint;
    }


    private List<User> relationshipList(String endpoint, String accessToken, String username, int limit, String cursor)
            throws TwitterException {

        try {
            int limitToUse = limit > 0? limit : Integer.MAX_VALUE;
            List<User> users = new ArrayList<>();
            String nextCursor = cursor;
            while (true) {
                String stringResponse = httpClient.authorizedGetRequest(
                        accessToken, getUrl(endpoint),
                        "screen_name", username,
                        "count", String.valueOf(limitToUse),
                        "cursor", nextCursor,
                        "skip_status", "true",
                        "include_user_entities", "false");
                UsersResponse response = new ObjectMapper().readValue(stringResponse, UsersResponse.class);
                users.addAll(response.getUsers());
                nextCursor = response.getNextCursor();
                if (nextCursor == null || nextCursor.isEmpty() || users.size() >= limitToUse)
                    break;
            }
            return users.size() > limitToUse ? users.subList(0, limitToUse) : users;
        } catch (Exception e) {
            throw new TwitterException("Unknown:" + e.getMessage(), 500);
        }
    }


    //-------------------------------- API Methods --------------------------------//

    /**
     * {@inheritDoc}
     */
    @Override
    public User self(String accessToken) throws TwitterException {
        try {
            String response = httpClient.authorizedGetRequest(
                    accessToken,
                    getUrl("account/verify_credentials.json"),
                    "skip_status", "true",
                    "include_user_entities", "false",
                    "include_email", "true");
            return new ObjectMapper().readValue(response, User.class);
        } catch (Exception e) {
            throw new TwitterException("Unknown:" + e.getMessage(), 500);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> followers(String accessToken, String username, int limit) throws TwitterException {
        return relationshipList("followers/list.json", accessToken, username, limit, "-1");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> followers(String accessToken, String username) throws TwitterException {
        return followers(accessToken, username, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> following(String accessToken, String username, int limit) throws TwitterException {
        return relationshipList("friends/list.json", accessToken, username, limit, "-1");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> following(String accessToken, String username) throws TwitterException {
        return following(accessToken, username, -1);
    }


}
