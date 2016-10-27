package io.reneses.tela.modules.instagram.api;

import io.reneses.tela.TestUtils;
import io.reneses.tela.core.cache.CacheManagerFactory;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.core.history.History;
import io.reneses.tela.core.history.HistoryFactory;
import io.reneses.tela.modules.instagram.InstagramTestUtils;
import io.reneses.tela.modules.instagram.databases.extensions.InstagramOrientDatabaseExtension;
import io.reneses.tela.modules.instagram.models.Comment;
import io.reneses.tela.modules.instagram.models.Media;
import io.reneses.tela.modules.instagram.models.User;
import io.reneses.tela.modules.instagram.models.UserRelationship;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class CachedInstagramApiWrapperTest {

    private CachedInstagramApiWrapper apiWrapper;
    private InstagramApi api;

    @Before
    public void setUp() throws Exception {

        OrientGraphWrapperFactory.registerExtensions(new InstagramOrientDatabaseExtension());
        OrientGraphWrapperFactory.connectMemory("test");
        CacheManagerFactory.setMemoryMode();
        History history = HistoryFactory.create();

        api = spy(new InstagramApiImpl(InstagramTestUtils.mockInstagramHttpClient()));
        apiWrapper = new CachedInstagramApiWrapper(api, history);

    }

    @After
    public void tearDown() throws Exception {
        CacheManagerFactory.create().clear();
        TestUtils.destroyDatabase();
    }

    @Test
    public void self() throws Exception {

        User self = api.self("1");
        verify(api, times(1)).self("1");

        // Cached
        User result = apiWrapper.self("1");
        assertNotNull(result);
        assertEquals(self, result);
        verify(api, times(2)).self("1");

        // Cached
        result = apiWrapper.self("1");
        assertNotNull(result);
        assertEquals(self, result);
        verify(api, times(2)).self("1");
    }

    @Test
    public void search() throws Exception {

        User user = api.search("", "jack");
        verify(api, times(1)).search("", "jack");

        // Cached
        User result = apiWrapper.search("", "jack");
        assertNotNull(result);
        assertEquals(user, result);
        verify(api, times(2)).search("", "jack");

        // Cached
        result = apiWrapper.search("", "jack");
        assertNotNull(result);
        assertEquals(user, result);
        verify(api, times(2)).search("", "jack");

    }

    @Test
    public void user() throws Exception {

        User user = api.user("1", 1);
        verify(api, times(1)).user("1", 1);

        // Cached
        User result = apiWrapper.user("1", 1);
        assertNotNull(result);
        assertEquals(user, result);
        verify(api, times(2)).user("1", 1);

        // Cached
        result = apiWrapper.user("1", 1);
        assertNotNull(result);
        assertEquals(user, result);
        verify(api, times(2)).user("1", 1);

    }

    @Test
    public void followers() throws Exception {

        List<User> users = api.followers("1");
        verify(api, times(1)).followers("1", -1);

        // Cached
        List<User> result = apiWrapper.followers("1");
        assertNotNull(result);
        assertEquals(users, result);
        verify(api, times(2)).followers("1", -1);

        // Cached
        result = apiWrapper.followers("1");
        assertNotNull(result);
        assertEquals(users, result);
        verify(api, times(2)).followers("1", -1);

    }

    @Test
    public void following() throws Exception {

        List<User> users = api.following("1");
        verify(api, times(1)).following("1", -1);

        // Cached
        List<User> result = apiWrapper.following("1");
        assertNotNull(result);
        assertEquals(users, result);
        verify(api, times(2)).following("1", -1);

        // Cached
        result = apiWrapper.following("1");
        assertNotNull(result);
        assertEquals(users, result);
        verify(api, times(2)).following("1", -1);

    }

    @Test
    public void friends() throws Exception {

        List<User> users = api.followers("1");
        users.retainAll(api.following("1"));
        verify(api, times(1)).following("1", -1);
        verify(api, times(1)).followers("1", -1);

        // Cached
        List<User> result = apiWrapper.friends("1");
        assertNotNull(result);
        assertEquals(users, result);
        verify(api, times(2)).following("1", -1);
        verify(api, times(2)).followers("1", -1);

        // Cached
        result = apiWrapper.friends("1");
        assertNotNull(result);
        assertEquals(users, result);
        verify(api, times(2)).following("1", -1);
        verify(api, times(2)).followers("1", -1);

    }

    @Test
    public void selfMedia() throws Exception {

        List<Media> media = api.selfMedia("");
        verify(api, times(1)).selfMedia("", -1);

        // Cached
        List<Media> result = apiWrapper.selfMedia("");
        assertNotNull(result);
        assertEquals(media, result);
        verify(api, times(2)).selfMedia("", -1);

        // Cached
        result = apiWrapper.selfMedia("");
        assertNotNull(result);
        assertEquals(media, result);
        verify(api, times(2)).selfMedia("", -1);

    }

    @Test
    public void likes() throws Exception {

        apiWrapper.selfMedia("");
        List<User> users = api.likes("", "1");
        verify(api, times(1)).likes("", "1");

        // Cached
        List<User> result = apiWrapper.likes("", "1");
        assertNotNull(result);
        assertEquals(users, result);
        verify(api, times(2)).likes("", "1");

        // Cached
        result = apiWrapper.likes("", "1");
        assertNotNull(result);
        assertEquals(users, result);
        verify(api, times(2)).likes("", "1");

    }

    @Test
    public void comments() throws Exception {

//        apiWrapper.self("");

        apiWrapper.selfMedia("");
        List<Comment> comments = api.comments("", "1");
        verify(api, times(1)).comments("", "1");

        // Cached
        List<Comment> result = apiWrapper.comments("", "1");
        assertNotNull(result);
        assertEquals(comments, result);
        verify(api, times(2)).comments("", "1");

        // Cached
        result = apiWrapper.comments("", "1");
        assertNotNull(result);
        assertEquals(comments, result);
        verify(api, times(2)).comments("", "1");

    }

    @Test
    public void relationship() throws Exception {

        apiWrapper.self("");
        UserRelationship relationship = api.relationship("", 1);
        verify(api, times(1)).relationship("", 1);

        // Cached
        UserRelationship result = apiWrapper.relationship("", 1);
        assertNotNull(result);
        assertEquals(relationship, result);
        verify(api, times(2)).relationship("", 1);

        // Cached
        result = apiWrapper.relationship("", 1);
        assertNotNull(result);
        assertEquals(relationship, result);
        verify(api, times(2)).relationship("", 1);

    }

}