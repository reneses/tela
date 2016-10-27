package io.reneses.tela.modules.twitter.repositories;

import io.reneses.tela.TestUtils;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.modules.twitter.database.extensions.TwitterOrientDatabaseExtension;
import io.reneses.tela.modules.twitter.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


public class OrientUserRepositoryTest {

    private UserRepository repository;
    private User u1, u2, u3, u4;


    @Before
    public void setUp() throws Exception {
        OrientGraphWrapperFactory.registerExtensions(new TwitterOrientDatabaseExtension());
        OrientGraphWrapperFactory.connectMemory("test");
        repository = new OrientUserRepository();

        u1 = new User();
        u1.setId(1);
        u1.setScreenName("user1");
        u1.setPicture("user1.png");

        u2 = new User();
        u2.setId(2);
        u2.setScreenName("user2");
        u2.setPicture("user2.png");

        u3 = new User();
        u3.setId(3);
        u3.setScreenName("user3");
        u3.setPicture("user3.png");

        u4 = new User();
        u4.setId(4);
        u4.setScreenName("user4");
        u4.setPicture("user4.png");

    }

    @After
    public void tearDown() throws Exception {
        TestUtils.destroyDatabase();
    }

    @Test
    public void create() throws Exception {
        repository.create(u1);
        assertEquals(u1, repository.find(u1.getScreenName()));
    }

    @Test
    public void setFollowers() throws Exception {
        List<User> followers = Arrays.asList(u2, u2);
        repository.setFollowers(followers, u1);
        assertEquals(followers, repository.findFollowers(u1.getScreenName()));
    }

    @Test
    public void setFollowing() throws Exception {
        List<User> following = Arrays.asList(u2, u3);
        repository.setFollowing(u1, following);
        assertEquals(following, repository.findFollowing(u1.getScreenName()));
    }

    @Test
    public void findFriends() throws Exception {
        List<User> followers = Arrays.asList(u2, u4);
        List<User> following = Arrays.asList(u2, u3);
        repository.setFollowers(followers, u1);
        repository.setFollowing(u1, following);
        List<User> friends = repository.findFriends(u1.getScreenName());
        assertNotNull(friends);
        assertEquals(1, friends.size());
        assertTrue(friends.contains(u2));
    }

}