package io.reneses.tela.modules.instagram.repositories;

import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapper;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.modules.instagram.databases.extensions.InstagramOrientDatabaseExtension;
import io.reneses.tela.modules.instagram.models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;


public class OrientMediaRepositoryTest {

    OrientGraphWrapper telaGraph;
    OrientGraphNoTx graph;
    OrientUserRepository userRepository;
    OrientMediaRepository mediaRepository;

    User u1, u2, u3;
    Comment caption, c1, c2;
    Media m1, m2, m3;

    @Before
    public void setUp() throws Exception {

        OrientGraphWrapperFactory.registerExtensions(new InstagramOrientDatabaseExtension());
        OrientGraphWrapperFactory.connectMemory("Test");

        telaGraph = OrientGraphWrapperFactory.get();
        userRepository = new OrientUserRepository();
        mediaRepository = new OrientMediaRepository();
        graph = telaGraph.getNoTxGraph();


        u1 = new User();
        u1.setId(3L);
        u1.setUsername("mikediaz");
        u1.setFullName("Mike Diaz");
        u1.setPicture("http://link.com/user.png");

        u2 = new User();
        u2.setId(4L);
        u2.setUsername("willsmith");
        u2.setFullName("Will Smith");
        u2.setPicture("http://link.com/will.png");

        u3 = new User();
        u3.setId(5L);
        u3.setUsername("axelrose");
        u3.setFullName("Axel Rose");
        u3.setPicture("http://link.com/axel.png");

        m1 = new Media();
        m1.setId("m1_id");
        m1.setCreatedTime(new Date());
        m1.setUserHasLiked(true);
        m1.setType(Media.IMAGE);
        m1.setFilter("Normal");
        m1.setLink("http://link.com/image.png");
        m1.setUser(u1);
        m1.setNumberOfComments(3);
        m1.setNumberOfLikes(4);

        m2 = new Media();
        m2.setId("m2");
        m2.setUser(u1);
        m2.setType(Media.IMAGE);
        m2.setCreatedTime(new Date(m1.getCreatedTime().getTime()+10000));
        m2.setFilter("Normal");
        m2.setLink("http://link.com/image.png");

        m3 = new Media();
        m3.setId("m3");
        m3.setUser(u1);
        m3.setType(Media.IMAGE);
        m3.setCreatedTime(new Date(m2.getCreatedTime().getTime()+10000));
        m3.setFilter("Normal");
        m3.setLink("http://link.com/image.png");

        caption = new Comment();
        caption.setUser(u1);
        caption.setCreatedTime(new Date());
        caption.setText("Morning!");
        caption.setId(1234);

        c1 = new Comment();
        c1.setUser(u2);
        c1.setCreatedTime(new Date());
        c1.setText("Afternoon!");
        c1.setId(1235);

        c2 = new Comment();
        c2.setUser(u1);
        c2.setCreatedTime(new Date());
        c2.setText("Night!");
        c2.setId(1236);

    }

    @After
    public void tearDown() throws Exception {
        graph.shutdown();
        OrientGraphWrapperFactory.dropAndDestroyInstance();
    }

    @Test
    public void testCreateSavesSimpleProperties() throws Exception {

        mediaRepository.createOrUpdate(m1);

        List<OrientVertex> vertices = telaGraph.getVertices(graph, InstagramOrientDatabaseExtension.Media.CLASS);
        assertEquals(1, vertices.size());

        io.reneses.tela.modules.instagram.models.Media retrieved = mediaRepository.find(m1.getId());
        assertEquals("id not matching", m1.getId(), retrieved.getId());
        assertEquals("createdTime not matching", m1.getCreatedTime(), retrieved.getCreatedTime());
        assertEquals("userHasLiked not matching", m1.getUserHasLiked(), retrieved.getUserHasLiked());
        assertEquals("type not matching", m1.getType(), retrieved.getType());
        assertEquals("filter not matching", m1.getFilter(), retrieved.getFilter());
        assertEquals("link not matching", m1.getLink(), retrieved.getLink());
        assertEquals(m1.getNumberOfComments(), retrieved.getNumberOfComments());
        assertEquals(m1.getNumberOfLikes(), retrieved.getNumberOfLikes());

    }

    @Test
    public void testCreateWithoutUserHasLiked() throws Exception {
        m1.setUserHasLiked(null);
        mediaRepository.createOrUpdate(m1);
        io.reneses.tela.modules.instagram.models.Media retrieved = mediaRepository.find(m1.getId());
        assertNull("userHasLiked not null", retrieved.getUserHasLiked());
    }

    @Test
    public void testCreateSavesNotExistingUser() throws Exception {
        mediaRepository.createOrUpdate(m1);
        User original = m1.getUser();
        User retrieved = mediaRepository.find(m1.getId()).getUser();
        assertNotNull("retrieved user is null", retrieved);
        assertEquals("users do not match", original, retrieved);
    }

    @Test
    public void testCreateSavesUpdatingExistingUser() throws Exception {
        User original = m1.getUser();
        userRepository.createOrUpdate(original);
        original.setFullName(original.getFullName() + "___");
        mediaRepository.createOrUpdate(m1);
        User retrieved = mediaRepository.find(m1.getId()).getUser();
        assertNotNull("retrieved user is null", retrieved);
        assertEquals("users do not match", original, retrieved);
    }

    @Test
    public void testCreateWithoutCaption() throws Exception {
        mediaRepository.createOrUpdate(m1);
        Comment retrieved = mediaRepository.find(m1.getId()).getCaption();
        assertNull("retrieved caption should be null", retrieved);
    }

    @Test
    public void testCreateSavesCaption() throws Exception {
        m1.setCaption(caption);
        mediaRepository.createOrUpdate(m1);
        Comment retrieved = mediaRepository.find(m1.getId()).getCaption();
        assertNotNull("retrieved caption is null", retrieved);
        assertEquals("captions do not match", caption, retrieved);
    }

    @Test
    public void testCreateWithComments() throws Exception {
        m1.setComments(Arrays.asList(c1, c2));
        mediaRepository.createOrUpdate(m1);
        List<Comment> retrievedComments = mediaRepository.find(m1.getId()).getComments();
        assertEquals(2, retrievedComments.size());
        assertEquals(c1, retrievedComments.get(0));
        assertEquals(c2, retrievedComments.get(1));
    }

    @Test
    public void testCreateWithLikes() throws Exception {
        m1.setLikes(Arrays.asList(u2, u3));
        mediaRepository.createOrUpdate(m1);
        List<User> retrievedLikes = mediaRepository.find(m1.getId()).getLikes();
        assertEquals(2, retrievedLikes.size());
        assertEquals(u2, retrievedLikes.get(0));
        assertEquals(u3, retrievedLikes.get(1));
    }

    @Test
    public void testCreateWithTags() throws Exception {

        TaggedUser tag1 = new TaggedUser();
        tag1.setUser(u2);
        tag1.setPosition(new Position(1,2));
        TaggedUser tag2 = new TaggedUser();
        tag2.setUser(u3);
        tag2.setPosition(new Position(3,4));
        m1.setTaggedUsers(Arrays.asList(tag1, tag2));
        mediaRepository.createOrUpdate(m1);

        List<TaggedUser> retrieved = mediaRepository.find(m1.getId()).getTaggedUsers();
        assertEquals(2, retrieved.size());
        assertTrue(retrieved.contains(tag1));
        assertTrue(retrieved.contains(tag2));

    }

    @Test
    public void testCreateWithHashtags() throws Exception {
        m1.setTags(new HashSet<>(Arrays.asList("test", "tela", "music")));
        mediaRepository.createOrUpdate(m1);
        Set<String> retrieved = mediaRepository.find(m1.getId()).getTags();
        assertEquals(m1.getTags().size(), retrieved.size());
        assertTrue(m1.getTags().containsAll(retrieved));
    }

    @Test
    public void testCreateLocation() throws Exception {
        Location location = new Location();
        location.setId(4);
        location.setName("HQ");
        location.setStreetAddress("Uria");
        location.setLatitude(0.2);
        location.setLongitude(0.1);
        m1.setLocation(location);
        mediaRepository.createOrUpdate(m1);
        assertEquals(location, mediaRepository.find(m1.getId()).getLocation());
    }

    @Test
    public void testCreateImages() throws Exception {
        MediaResource r1 = new MediaResource();
        r1.setUrl("1.png");
        r1.setHeight(20);
        r1.setWidth(60);
        MediaResource r2 = new MediaResource();
        r2.setUrl("2.png");
        r2.setHeight(30);
        r2.setWidth(40);
        Map<String, MediaResource> resources = new HashMap<>();
        resources.put("size-1", r1);
        resources.put("size-2", r2);
        m1.setImages(resources);
        mediaRepository.createOrUpdate(m1);
        Map<String, MediaResource> retrieved = mediaRepository.find(m1.getId()).getImages();
        assertNotNull(resources);
        assertTrue(retrieved.containsKey("size-1"));
        assertTrue(resources.containsValue(r1));
        assertTrue(retrieved.containsKey("size-2"));
        assertTrue(resources.containsValue(r2));
    }

    @Test
    public void testCreateVideos() throws Exception {
        MediaResource r1 = new MediaResource();
        r1.setUrl("1.png");
        r1.setHeight(20);
        r1.setWidth(60);
        MediaResource r2 = new MediaResource();
        r2.setUrl("2.png");
        r2.setHeight(30);
        r2.setWidth(40);
        Map<String, MediaResource> resources = new HashMap<>();
        resources.put("size-1", r1);
        resources.put("size-2", r2);
        m1.setVideos(resources);
        mediaRepository.createOrUpdate(m1);
        Map<String, MediaResource> retrieved = mediaRepository.find(m1.getId()).getVideos();
        assertNotNull(resources);
        assertTrue(retrieved.containsKey("size-1"));
        assertTrue(resources.containsValue(r1));
        assertTrue(retrieved.containsKey("size-2"));
        assertTrue(resources.containsValue(r2));
    }

    @Test
    public void findAll() throws Exception {
        mediaRepository.createOrUpdate(m1);
        mediaRepository.createOrUpdate(m2);
        mediaRepository.createOrUpdate(m3);

        List<Media> retrieved = mediaRepository.findAll(u1.getId());
        assertEquals(3, retrieved.size());
        assertTrue(retrieved.contains(m1));
        assertTrue(retrieved.contains(m2));
        assertTrue(retrieved.contains(m3));
    }

    @Test
    public void findLatest() throws Exception {
        mediaRepository.createOrUpdate(m3);
        mediaRepository.createOrUpdate(m1);
        mediaRepository.createOrUpdate(m2);

        List<Media> retrieved = mediaRepository.findLatest(u1.getId(), 2);
        assertEquals(2, retrieved.size());
        assertTrue(retrieved.contains(m2));
        assertTrue(retrieved.contains(m3));
    }
}