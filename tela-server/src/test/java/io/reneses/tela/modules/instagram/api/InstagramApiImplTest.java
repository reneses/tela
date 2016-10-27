package io.reneses.tela.modules.instagram.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reneses.tela.modules.instagram.InstagramTestUtils;
import io.reneses.tela.modules.instagram.api.responses.*;
import io.reneses.tela.modules.instagram.models.Comment;
import io.reneses.tela.modules.instagram.models.Media;
import io.reneses.tela.modules.instagram.models.User;
import io.reneses.tela.modules.instagram.models.UserRelationship;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;


public class InstagramApiImplTest {

    private InstagramApi api;

    @Before
    public void setUp() throws Exception {
        api = new InstagramApiImpl(InstagramTestUtils.mockInstagramHttpClient());
    }

    @Test
    public void self() throws Exception {
        File file = new File(InstagramApiImplTest.class.getResource("/instagram/user.json").toURI());
        User self = new ObjectMapper().readValue(file, UserResponse.class).getUser();
        User retrievedSelf = api.self("");
        assertEquals(self, retrievedSelf);
    }

    @Test
    public void search() throws Exception {
        File file = new File(InstagramApiImplTest.class.getResource("/instagram/search.json").toURI());
        User user = new ObjectMapper().readValue(file, SearchResponse.class).getUser("jack");
        User retrievedSelf = api.search("", "jack");
        assertEquals(user, retrievedSelf);
    }

    @Test
    public void user() throws Exception {
        File file = new File(InstagramApiImplTest.class.getResource("/instagram/user.json").toURI());
        User user = new ObjectMapper().readValue(file, UserResponse.class).getUser();
        User retrievedSelf = api.user("", 1);
        assertEquals(user, retrievedSelf);
    }

    @Test
    public void followersWithLimit() throws Exception {
        File file = new File(InstagramApiImplTest.class.getResource("/instagram/relationship-1-1.json").toURI());
        List<User> users = new ObjectMapper().readValue(file, RelationshipResponse.class).getUsers().subList(0,1);
        List<User> retrieved = api.followers("", 1);
        assertNotNull(retrieved);
        assertEquals(users.size(), retrieved.size());
        assertEquals(users, retrieved);
    }

    @Test
    public void followersWithoutLimit() throws Exception {
        File file1 = new File(InstagramApiImplTest.class.getResource("/instagram/relationship-1-1.json").toURI());
        File file2 = new File(InstagramApiImplTest.class.getResource("/instagram/relationship-2.json").toURI());
        List<User> users = new ObjectMapper().readValue(file1, RelationshipResponse.class).getUsers();
        users.addAll(new ObjectMapper().readValue(file2, RelationshipResponse.class).getUsers());
        List<User> retrieved = api.followers("");
        assertNotNull(retrieved);
        assertEquals(users.size(), retrieved.size());
        assertEquals(users, retrieved);
    }

    @Test
    public void followingWithLimit() throws Exception {
        File file = new File(InstagramApiImplTest.class.getResource("/instagram/relationship-1-2.json").toURI());
        List<User> users = new ObjectMapper().readValue(file, RelationshipResponse.class).getUsers().subList(0,1);
        List<User> retrieved = api.following("", 1);
        assertNotNull(retrieved);
        assertEquals(users.size(), retrieved.size());
        assertEquals(users, retrieved);
    }

    @Test
    public void followingWithoutLimit() throws Exception {
        File file1 = new File(InstagramApiImplTest.class.getResource("/instagram/relationship-1-2.json").toURI());
        File file2 = new File(InstagramApiImplTest.class.getResource("/instagram/relationship-2.json").toURI());
        List<User> users = new ObjectMapper().readValue(file1, RelationshipResponse.class).getUsers();
        users.addAll(new ObjectMapper().readValue(file2, RelationshipResponse.class).getUsers());
        List<User> retrieved = api.following("");
        assertNotNull(retrieved);
        assertEquals(users.size(), retrieved.size());
        assertEquals(users, retrieved);
    }

    @Test
    public void relationship() throws Exception {
        File file = new File(InstagramApiImplTest.class.getResource("/instagram/user-relationship.json").toURI());
        UserRelationship relationship =
                new ObjectMapper().readValue(file, UserRelationshipResponse.class).getRelationship();
        UserRelationship retrieved = api.relationship("", 1);
        assertNotNull(retrieved);
        assertEquals(relationship, retrieved);
    }

    @Test
    public void selfMediaWithoutLimit() throws Exception {
        File file = new File(InstagramApiImplTest.class.getResource("/instagram/media.json").toURI());
        List<Media> media = new ObjectMapper().readValue(file, MediaResponse.class).getMedia();
        List<Media> retrieved = api.selfMedia("");
        assertNotNull(retrieved);
        assertEquals(media.size(), retrieved.size());
        assertEquals(media, retrieved);
    }

    @Test
    public void selfMediaWithLimit() throws Exception {
        File file = new File(InstagramApiImplTest.class.getResource("/instagram/media.json").toURI());
        List<Media> media = new ObjectMapper().readValue(file, MediaResponse.class).getMedia().subList(0,1);
        List<Media> retrieved = api.selfMedia("", 1);
        assertNotNull(retrieved);
        assertEquals(media.size(), retrieved.size());
        assertEquals(media, retrieved);
    }

    @Test
    public void likes() throws Exception {
        File file1 = new File(InstagramApiImplTest.class.getResource("/instagram/likes-1.json").toURI());
        File file2 = new File(InstagramApiImplTest.class.getResource("/instagram/likes-2.json").toURI());
        List<User> likes = new ObjectMapper().readValue(file1, LikesResponse.class).getLikes();
        likes.addAll(new ObjectMapper().readValue(file2, LikesResponse.class).getLikes());
        List<User> retrieved = api.likes("", "1");
        assertNotNull(retrieved);
        assertEquals(likes.size(), retrieved.size());
        assertEquals(likes, retrieved);
    }

    @Test
    public void comments() throws Exception {
        File file1 = new File(InstagramApiImplTest.class.getResource("/instagram/comments-1.json").toURI());
        File file2 = new File(InstagramApiImplTest.class.getResource("/instagram/comments-2.json").toURI());
        List<Comment> comments = new ObjectMapper().readValue(file1, CommentsResponse.class).getComments();
        comments.addAll(new ObjectMapper().readValue(file2, CommentsResponse.class).getComments());
        List<Comment> retrieved = api.comments("", "1");
        assertNotNull(retrieved);
        assertEquals(comments.size(), retrieved.size());
        assertEquals(comments, retrieved);
    }

}