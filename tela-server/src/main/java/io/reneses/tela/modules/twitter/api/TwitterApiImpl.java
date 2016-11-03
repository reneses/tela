package io.reneses.tela.modules.twitter.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import io.reneses.tela.modules.twitter.api.responses.UserResponse;
import io.reneses.tela.modules.twitter.api.exceptions.TwitterException;
import io.reneses.tela.modules.twitter.api.responses.UsersResponse;
import io.reneses.tela.modules.twitter.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Twitter API implementation
 */
class TwitterApiImpl implements TwitterApi {

    String oauthGetRequest(String apiKey, String apiSecret, String token, String tokenSecret,
                           String endpoint, String... params) throws IOException {

        OAuth10aService service = new ServiceBuilder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build(com.github.scribejava.apis.TwitterApi.instance());
        OAuth1AccessToken accessToken = new OAuth1AccessToken(token, tokenSecret);
        OAuthRequest request = new OAuthRequest(Verb.GET, endpoint, service);
        if (params.length > 0) {
            for (int i = 0; i <= params.length / 2; i += 2)
                request.addParameter(params[i], params[i + 1]);
        }
        service.signRequest(accessToken, request);
        return request.send().getBody();
    }


    private List<User> relationshipList(String apiKey, String apiSecret, String token, String tokenSecret,
                                        String endpoint, String username, int limit, String cursor)
            throws TwitterException {

        try {
            int limitToUse = limit > 0 ? limit : Integer.MAX_VALUE;
            List<User> users = new ArrayList<>();
            String nextCursor = cursor;
            while (true) {

                String stringResponse = oauthGetRequest(
                        apiKey, apiSecret, token, tokenSecret, endpoint,
                        "screen_name", username,
                        "count", limitToUse > 200 ? "200" : String.valueOf(limitToUse),
                        "cursor", nextCursor,
                        "skip_status", "true",
                        "include_user_entities", "false");

                UsersResponse response = new ObjectMapper().readValue(stringResponse, UsersResponse.class);
                if (response.hasErrors()) {
                    throw new TwitterException(response.getErrors().toString(), 400);
                }
                users.addAll(response.getUsers());
                nextCursor = response.getNextCursor();
                if (nextCursor == null || nextCursor.isEmpty() || users.size() >= limitToUse)
                    break;
            }
            return users.size() > limitToUse ? users.subList(0, limitToUse) : users;
        } catch (TwitterException e) {
            throw e;
        } catch (Exception e) {
            throw new TwitterException("Unknown:" + e.getMessage(), 500);
        }
    }


    //-------------------------------- API Methods --------------------------------//

    /**
     * {@inheritDoc}
     */
    @Override
    public User self(String apiKey, String apiSecret, String token, String tokenSecret) throws TwitterException {
        try {
            String endpoint = "https://api.twitter.com/1.1/account/verify_credentials.json";
            String stringResponse = oauthGetRequest(apiKey, apiSecret, token, tokenSecret, endpoint,
                    "skip_status", "true", "include_user_entities", "false", "include_email", "true");
            UserResponse response =  new ObjectMapper().readValue(stringResponse, UserResponse.class);
            if (response.hasErrors()) {
                throw new TwitterException(response.getErrors().toString(), 400);
            }
            return response.getUser();
        } catch (TwitterException e) {
            throw e;
        } catch (Exception e) {
            throw new TwitterException("Unknown:" + e.getMessage(), 500);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User user(String apiKey, String apiSecret, String token, String tokenSecret,
                     String username) throws TwitterException {

        try {
            String endpoint = "https://api.twitter.com/1.1/users/show.json";
            String stringResponse = oauthGetRequest(apiKey, apiSecret, token, tokenSecret, endpoint,
                    "screen_name", username, "include_entities", "false");
            UserResponse response =  new ObjectMapper().readValue(stringResponse, UserResponse.class);
            if (response.hasErrors()) {
                throw new TwitterException(response.getErrors().toString(), 400);
            }
            return response.getUser();
        } catch (TwitterException e) {
            throw e;
        } catch (Exception e) {
            throw new TwitterException("Unknown:" + e.getMessage(), 500);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> followers(String apiKey, String apiSecret, String token, String tokenSecret,
                                String username, int limit) throws TwitterException {

        String endpoint = "https://api.twitter.com/1.1/followers/list.json";
        return relationshipList(apiKey, apiSecret, token, tokenSecret, endpoint, username, limit, "-1");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> followers(String apiKey, String apiSecret, String token, String tokenSecret,
                                String username) throws TwitterException {

        return followers(apiKey, apiSecret, token, tokenSecret, username, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> following(String apiKey, String apiSecret, String token, String tokenSecret,
                                String username, int limit) throws TwitterException {

        String endpoint = "https://api.twitter.com/1.1/friends/list.json";
        return relationshipList(apiKey, apiSecret, token, tokenSecret, endpoint, username, limit, "-1");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> following(String apiKey, String apiSecret, String token, String tokenSecret,
                                String username) throws TwitterException {

        return following(apiKey, apiSecret, token, tokenSecret, username, -1);
    }


}
