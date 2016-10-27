package io.reneses.tela.modules.twitter.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reneses.tela.modules.twitter.TwitterTestUtils;
import io.reneses.tela.modules.twitter.api.responses.UsersResponse;
import io.reneses.tela.modules.twitter.models.User;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;


public class TwitterApiImplTest {

    private static TwitterApi api;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        api = new TwitterApiImpl(TwitterTestUtils.mockTwitterHttpClient());
    }

    @Test
    public void self() throws Exception {
        File file = new File(User.class.getResource("/twitter/self.json").toURI());
        User self = new ObjectMapper().readValue(file, User.class);
        User retrievedSelf = api.self("");
        assertEquals(self, retrievedSelf);
    }

    @Test
    public void followersWithLimit() throws Exception {
        File file = new File(User.class.getResource("/twitter/relationship-1-1.json").toURI());
        UsersResponse response = new ObjectMapper().readValue(file, UsersResponse.class);
        List<User> followers = response.getUsers().subList(0, 5);
        List<User> retrievedUsers = api.followers("", "", 5);
        assertEquals(followers.size(), retrievedUsers.size());
        assertEquals(followers, retrievedUsers);
    }

    @Test
    public void followersWithoutLimit() throws Exception {
        File file1 = new File(User.class.getResource("/twitter/relationship-1-1.json").toURI());
        File file2 = new File(User.class.getResource("/twitter/relationship-2.json").toURI());
        UsersResponse response1 = new ObjectMapper().readValue(file1, UsersResponse.class);
        UsersResponse response2 = new ObjectMapper().readValue(file2, UsersResponse.class);
        List<User> followers = response1.getUsers();
        followers.addAll(response2.getUsers());
        List<User> retrievedUsers = api.followers("", "");
        assertEquals(followers.size(), retrievedUsers.size());
        assertEquals(followers, retrievedUsers);
    }

    @Test
    public void followingWithLimit() throws Exception {
        File file = new File(User.class.getResource("/twitter/relationship-1-2.json").toURI());
        UsersResponse response = new ObjectMapper().readValue(file, UsersResponse.class);
        List<User> following = response.getUsers().subList(0, 5);
        List<User> retrievedUsers = api.following("", "", 5);
        assertEquals(following.size(), retrievedUsers.size());
        assertEquals(following, retrievedUsers);
    }

    @Test
    public void followingWithoutLimit() throws Exception {
        File file1 = new File(User.class.getResource("/twitter/relationship-1-2.json").toURI());
        File file2 = new File(User.class.getResource("/twitter/relationship-2.json").toURI());
        UsersResponse response1 = new ObjectMapper().readValue(file1, UsersResponse.class);
        UsersResponse response2 = new ObjectMapper().readValue(file2, UsersResponse.class);
        List<User> following = response1.getUsers();
        following.addAll(response2.getUsers());
        List<User> retrievedUsers = api.following("", "");
        assertEquals(following.size(), retrievedUsers.size());
        assertEquals(following, retrievedUsers);
    }

}