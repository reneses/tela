package io.reneses.tela.modules.instagram.actions;

import io.reneses.tela.core.dispatcher.annotations.Action;
import io.reneses.tela.core.dispatcher.annotations.Module;
import io.reneses.tela.modules.instagram.api.AbstractInstagramApiWrapper;
import io.reneses.tela.modules.instagram.api.exceptions.InstagramException;
import io.reneses.tela.modules.instagram.models.Comment;
import io.reneses.tela.modules.instagram.models.Media;
import io.reneses.tela.modules.instagram.models.User;

import java.util.List;

/**
 * Media actions
 */
@Module("instagram")
// TODO check cache
public class MediaActions extends InstagramActions {

    /**
     * Constructor with DI
     *
     * @param api API Wrapper
     */
    public MediaActions(AbstractInstagramApiWrapper api) {
        super(api);
    }

    /**
     * Default constructor, required by the action dispatcher
     */
    public MediaActions() {
    }

    /**
     * Retrieve the latest media of the logged user
     * Required scope: public_content
     *
     * @param accessToken Access token
     * @param limit       Maximum number of media to return
     * @return Media of the logged user
     * @throws InstagramException If any exception is thrown
     */
    @Action(name = "self-media",
            description = "Get the latest media of the logged user",
            parameters = {"token", "limit"})
    @Schedulable(minimumDelay = 3600)
    public List<Media> selfMedia(String accessToken, int limit) throws InstagramException {
        return api.selfMedia(accessToken, limit);
    }

    /**
     * Retrieve all the media of the logged user
     * Required scope: public_content
     *
     * @param accessToken Access token
     * @return Media of the logged user
     * @throws InstagramException If any exception is thrown
     */
    @Action(name = "self-media", description = "Get the latest media of the logged user", parameters = {"token"})
    @Schedulable(minimumDelay = 3600)
    public List<Media> selfMedia(String accessToken) throws InstagramException {
        return selfMedia(accessToken, -1);
    }

    /**
     * Retrieve the likes of a media
     * Required scope: public_content
     *
     * @param accessToken Access token
     * @param mediaId     Media ID
     * @return Likes of the media
     * @throws InstagramException If any exception is thrown
     */
    @Action(name = "likes", description = "Get the likes of a media", parameters = {"token", "mediaId"})
    @Schedulable(minimumDelay = 3600)
    public List<User> likes(String accessToken, String mediaId) throws InstagramException {
        return api.likes(accessToken, mediaId);
    }

    /**
     * Retrieve the comments of a media
     * Required scope: public_content
     *
     * @param accessToken Access token
     * @param mediaId     Media ID
     * @return Likes of the media
     * @throws InstagramException If any exception is thrown
     */
    @Action(name = "comments", description = "Get the comments of a media", parameters = {"token", "mediaId"})
    @Schedulable(minimumDelay = 3600)
    public List<Comment> comments(String accessToken, String mediaId) throws InstagramException {
        return api.comments(accessToken, mediaId);
    }

}
