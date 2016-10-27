package io.reneses.tela.modules.instagram.api;

import io.reneses.tela.TestUtils;
import io.reneses.tela.core.cache.CacheManagerFactory;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.core.history.History;
import io.reneses.tela.core.history.HistoryFactory;
import io.reneses.tela.modules.instagram.InstagramTestUtils;
import io.reneses.tela.modules.instagram.databases.extensions.InstagramOrientDatabaseExtension;
import io.reneses.tela.modules.instagram.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;


public class InstagramApiWrapperTest {

    private AbstractInstagramApiWrapper apiWrapper;
    private InstagramApi api;


    @Before
    public void setUp() throws Exception {

        OrientGraphWrapperFactory.registerExtensions(new InstagramOrientDatabaseExtension());
        OrientGraphWrapperFactory.connectMemory("test");
        CacheManagerFactory.setMemoryMode();
        History history = HistoryFactory.create();

        api = new InstagramApiImpl(InstagramTestUtils.mockInstagramHttpClient());
        apiWrapper = new InstagramApiWrapper(api, history);

    }

    @After
    public void tearDown() throws Exception {
        CacheManagerFactory.create().clear();
        TestUtils.destroyDatabase();
    }

    @Test
    public void self() throws Exception {
        assertEquals(api.self(""), apiWrapper.self(""));
    }

    @Test
    public void search() throws Exception {
        assertEquals(api.search("", "jack"), apiWrapper.search("", "jack"));
    }

    @Test
    public void userById() throws Exception {
        assertEquals(api.user("", 1), apiWrapper.user("", 1));
    }

    @Test
    public void userByUsername() throws Exception {
        assertEquals(api.user("", 1), apiWrapper.user("", "jack"));
    }

    @Test
    public void followersWithoutLimit() throws Exception {
        assertEquals(api.followers(""), apiWrapper.followers(""));
    }

    @Test
    public void followersWithLimit() throws Exception {
        assertEquals(api.followers("", 1), apiWrapper.followers("", 1));

    }

    @Test
    public void followingWithoutLimit() throws Exception {
        assertEquals(api.following(""), apiWrapper.following(""));
    }

    @Test
    public void followingWithLimit() throws Exception {
        assertEquals(api.following("", 1), apiWrapper.following("", 1));
    }

    @Test
    public void friends() throws Exception {
        List<User> friends = api.followers("");
        friends.retainAll(api.following(""));
        assertEquals(friends, apiWrapper.friends(""));
    }

    @Test
    public void countsById() throws Exception {
        User user = apiWrapper.user("", 1);
        assertTrue(apiWrapper.counts("", 1).contains(user.getCounts()));
    }

    @Test
    public void countsByUsername() throws Exception {
        User user = apiWrapper.user("", 1);
        assertTrue(apiWrapper.counts("", "jack").contains(user.getCounts()));
    }

    @Test
    public void relationshipById() throws Exception {
        assertEquals(api.relationship("", 1), apiWrapper.relationship("", 1));
    }

    @Test
    public void relationshipByUsername() throws Exception {
        assertEquals(api.relationship("", 1), apiWrapper.relationship("", "jack"));
    }

    @Test
    public void selfMediaWithLimit() throws Exception {
        assertEquals(api.selfMedia("", 1), apiWrapper.selfMedia("", 1));
    }

    @Test
    public void selfMediaWithoutLimit() throws Exception {
        assertEquals(api.selfMedia(""), apiWrapper.selfMedia(""));
    }

    @Test
    public void likes() throws Exception {
        api.selfMedia("");
        assertEquals(api.likes("", "1"), apiWrapper.likes("", "1"));
    }

    @Test
    public void comments() throws Exception {
        api.selfMedia("");
        assertEquals(api.comments("", "1"), apiWrapper.comments("", "1"));
    }

}