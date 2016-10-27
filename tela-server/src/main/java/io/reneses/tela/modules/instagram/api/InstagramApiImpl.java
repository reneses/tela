package io.reneses.tela.modules.instagram.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reneses.tela.modules.instagram.api.exceptions.InstagramException;
import io.reneses.tela.modules.instagram.api.exceptions.InvalidAccessTokenException;
import io.reneses.tela.modules.instagram.api.exceptions.PrivateUserException;
import io.reneses.tela.modules.instagram.api.exceptions.UserNotExistsException;
import io.reneses.tela.modules.instagram.models.Comment;
import io.reneses.tela.modules.instagram.models.Media;
import io.reneses.tela.modules.instagram.api.models.Meta;
import io.reneses.tela.modules.instagram.models.User;
import io.reneses.tela.modules.instagram.models.UserRelationship;
import io.reneses.tela.modules.instagram.api.responses.*;
import io.reneses.tela.core.util.HttpClient;
import io.reneses.tela.core.util.JettyHttpClient;

import java.util.ArrayList;
import java.util.List;


/**
 * Implementation of the InstagramAPI
 */
class InstagramApiImpl implements InstagramApi {

    private static final String API_URL = "https://api.instagram.com/v1/";

    private HttpClient httpClient;

    InstagramApiImpl() {
        this.httpClient = new JettyHttpClient();
    }

    InstagramApiImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private void inspectMetaAndThrowExceptions(Meta meta, String info) throws InstagramException {

        // Error
        if (meta != null && meta.isError()) {

            // Invalid accessToken
            if (meta.isInvalidAccessToken())
                throw new InvalidAccessTokenException(info);

            // Not existing user
            if (meta.isNotExistingResource())
                throw new UserNotExistsException(info);

            // Private user
            if (meta.isRestrictedResource())
                throw new PrivateUserException(info);

            // Other error
            if (meta.isError())
                throw new InstagramException(String.format("%s (%d): %s", meta.getErrorType(), meta.getCode(), meta.getErrorMessage()), meta.getCode());
        }

    }

    private String getUrl(String endPoint) {
        return API_URL + endPoint;
    }


    //-------------------------------- API Methods --------------------------------//

    /**
     * {@inheritDoc}
     */
    @Override
    public User self(String accessToken) throws InstagramException {
        try {
            String stringResponse = httpClient.getRequest(
                    getUrl("users/self"),
                    "access_token", accessToken
            );
            UserResponse response = new ObjectMapper().readValue(stringResponse, UserResponse.class);
            inspectMetaAndThrowExceptions(response.getMeta(), "Access token: " + accessToken);
            User user = response.getUser();
            if (user == null)
                throw new UserNotExistsException("Access token: " + accessToken);
            return user;
        }
        // Upstream Instagram exceptions
        catch (InstagramException e) {
            throw e;
        }
        // Catch other exceptions
        catch (Exception e) {
            throw new InstagramException("Unknown:" + e.getMessage(), 500);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User search(String accessToken, String username) throws InstagramException {
        try {
            String stringResponse = httpClient.getRequest(
                    getUrl("users/search"),
                    "access_token", accessToken,
                    "q", username
            );
            SearchResponse response = new ObjectMapper().readValue(stringResponse, SearchResponse.class);
            inspectMetaAndThrowExceptions(response.getMeta(), "Username: " + username);
            User user = response.getUser(username);
            if (user == null)
                throw new UserNotExistsException(username);
            return user;
        }
        // Upstream Instagram exceptions
        catch (InstagramException e) {
            throw e;
        }
        // Catch other exceptions
        catch (Exception e) {
            throw new InstagramException("Unknown:" + e.getMessage(), 500);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User user(String accessToken, long userId) throws InstagramException {

        try {
            String stringResponse = httpClient.getRequest(
                    getUrl("users/" + userId),
                    "access_token", accessToken
            );
            UserResponse response = new ObjectMapper().readValue(stringResponse, UserResponse.class);
            inspectMetaAndThrowExceptions(response.getMeta(), "User ID: " + userId);
            User user = response.getUser();
            if (user == null)
                throw new UserNotExistsException("ID: " + userId);
            return user;
        }
        // Upstream Instagram exceptions
        catch (InstagramException e) {
            throw e;
        }
        // Catch other exceptions
        catch (Exception e) {
            throw new InstagramException("Unknown: " + e.getMessage(), 500);
        }
    }


    private List<User> userRelationship(String accessToken, String endpoint, int limit) throws InstagramException {

        if (limit < 0)
            limit = Integer.MAX_VALUE;
        List<User> users = new ArrayList<>();

        // Go through every page
        String nextCursor = "";
        RelationshipResponse response;
        try {
            while (users.size() < limit) {

                // Obtain the response
                String stringResponse = httpClient.getRequest(
                        getUrl("users/self/" + endpoint),
                        "access_token", accessToken,
                        "cursor", nextCursor
                );
                response = new ObjectMapper().readValue(stringResponse, RelationshipResponse.class);
                inspectMetaAndThrowExceptions(response.getMeta(), "Access token: " + accessToken);

                // Add the users
                users.addAll(response.getUsers());

                // Go to the next page
                if (response.getPagination() == null || !response.getPagination().hasNext())
                    break;
                nextCursor = response.getPagination().getNextCursor();

            }
            return (users.size() > limit) ? users.subList(0, limit) : users;

        }
        // Upstream Instagram exceptions
        catch (InstagramException e) {
            throw e;
        }
        // Catch other exceptions
        catch (Exception e) {
            throw new InstagramException("Unknown: " + e.getMessage(), 500);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> followers(String accessToken, int limit) throws InstagramException {
        return userRelationship(accessToken, "followed-by", limit);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> followers(String accessToken) throws InstagramException {
        return followers(accessToken, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> following(String accessToken, int limit) throws InstagramException {
        return userRelationship(accessToken, "follows", limit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> following(String accessToken) throws InstagramException {
        return following(accessToken, -1);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public UserRelationship relationship(String accessToken, long userId) throws InstagramException {

        try {
            String stringResponse = httpClient.getRequest(
                    getUrl("users/" + userId + "/relationship"),
                    "access_token", accessToken
            );
            UserRelationshipResponse response = new ObjectMapper().readValue(stringResponse, UserRelationshipResponse.class);
            inspectMetaAndThrowExceptions(response.getMeta(), "User ID: " + userId);
            return response.getRelationship();


        }

        // Upstream Instagram exceptions
        catch (InstagramException e) {
            throw e;
        }
        // Catch other exceptions
        catch (Exception e) {
            throw new InstagramException("Unknown: " + e.getMessage(), 500);
        }
    }


    // Media

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Media> selfMedia(String accessToken, int limit) throws InstagramException {

        if (limit < 0)
            limit = Integer.MAX_VALUE;

        List<Media> media = new ArrayList<>();

        try {

            String nextUrl = String.format("%s?access_token=%s&count=%d",
                    getUrl("users/self/media/recent"), accessToken, limit);

            do {
                String stringResponse = httpClient.getRequest(nextUrl);
                MediaResponse response = new ObjectMapper().readValue(stringResponse, MediaResponse.class);
                inspectMetaAndThrowExceptions(response.getMeta(), "Access token: " + accessToken);
                media.addAll(response.getMedia());
                nextUrl = response.getPagination() == null? null : response.getPagination().getNextUrl();
            } while (nextUrl != null && media.size() < limit);

            return (media.size() > limit) ? media.subList(0, limit) : media;
        }
        // Upstream Instagram exceptions
        catch (InstagramException e) {
            throw e;
        }
        // Catch other exceptions
        catch (Exception e) {
            throw new InstagramException("Unknown: " + e.getMessage(), 500);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Media> selfMedia(String accessToken) throws InstagramException {
        return selfMedia(accessToken, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> likes(String accessToken, String mediaId) throws InstagramException {
        List<User> likes = new ArrayList<>();
        try {
            String nextUrl = String.format("%s?access_token=%s", getUrl("media/" + mediaId + "/likes"), accessToken);
            do {
                String stringResponse = httpClient.getRequest(nextUrl);
                LikesResponse response = new ObjectMapper().readValue(stringResponse, LikesResponse.class);
                inspectMetaAndThrowExceptions(response.getMeta(), "Media ID: " + mediaId);
                likes.addAll(response.getLikes());
                nextUrl = response.getPagination() == null? null : response.getPagination().getNextUrl();
            } while (nextUrl != null && !nextUrl.isEmpty());
            return likes;
        }
        // Upstream Instagram exceptions
        catch (InstagramException e) {
            throw e;
        }
        // Catch other exceptions
        catch (Exception e) {
            throw new InstagramException("Unknown: " + e.getMessage(), 500);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> comments(String accessToken, String mediaId) throws InstagramException {
        List<Comment> comments = new ArrayList<>();
        try {
            String nextUrl = String.format("%s?access_token=%s", getUrl("media/" + mediaId + "/comments"), accessToken);
            do {
                String stringResponse = httpClient.getRequest(nextUrl);
                CommentsResponse response = new ObjectMapper().readValue(stringResponse, CommentsResponse.class);
                inspectMetaAndThrowExceptions(response.getMeta(), "Media ID: " + mediaId);
                comments.addAll(response.getComments());
                nextUrl = response.getPagination() == null? null : response.getPagination().getNextUrl();
            } while (nextUrl != null && !nextUrl.isEmpty());
            return comments;
        }
        // Upstream Instagram exceptions
        catch (InstagramException e) {
            throw e;
        }
        // Catch other exceptions
        catch (Exception e) {
            throw new InstagramException("Unknown: " + e.getMessage(), 500);
        }
    }

}