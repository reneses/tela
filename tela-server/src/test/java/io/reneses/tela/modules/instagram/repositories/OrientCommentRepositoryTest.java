package io.reneses.tela.modules.instagram.repositories;

import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapper;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.modules.instagram.databases.extensions.InstagramOrientDatabaseExtension;
import io.reneses.tela.modules.instagram.models.Comment;
import io.reneses.tela.modules.instagram.models.Media;
import io.reneses.tela.modules.instagram.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class OrientCommentRepositoryTest {

    OrientGraphNoTx graph;
    OrientUserRepository userRepository;
    OrientCommentRepository commentRepository;
    MediaRepository mediaRepository;

    User u1, u2;
    Comment c1, c2, c3;
    Media m1;

    @Before
    public void setUp() throws Exception {

        OrientGraphWrapperFactory.registerExtensions(new InstagramOrientDatabaseExtension());
        OrientGraphWrapperFactory.connectMemory("Test");

        OrientGraphWrapper telaGraph = OrientGraphWrapperFactory.get();
        userRepository = new OrientUserRepository();
        commentRepository = new OrientCommentRepository();
        mediaRepository = new OrientMediaRepository();
        graph = telaGraph.getNoTxGraph();


        u1 = new User();
        u1.setId(3L);
        u1.setUsername("mikediaz");
        u1.setFullName("Mike Diaz");
        u1.setPicture("http://link.com/mike.png");

        u2 = new User();
        u2.setId(5L);
        u2.setUsername("willsmith");
        u2.setFullName("Will Smith");
        u2.setPicture("http://link.com/will.png");

        c1 = new Comment();
        c1.setUser(u1);
        c1.setCreatedTime(new Date());
        c1.setText("Morning!");
        c1.setId(1234);

        c2 = new Comment();
        c2.setUser(u2);
        c2.setCreatedTime(new Date());
        c2.setText("Afternoon!");
        c2.setId(1235);

        c3 = new Comment();
        c3.setUser(u2);
        c3.setCreatedTime(new Date());
        c3.setText("Night!");
        c3.setId(1236);

        m1 = new Media();
        m1.setId("m1_id");
        m1.setCreatedTime(new Date());
        m1.setUserHasLiked(true);
        m1.setType(Media.IMAGE);
        m1.setFilter("Normal");
        m1.setLink("http://link.com/image.png");
        m1.setUser(u1);

    }

    @After
    public void tearDown() throws Exception {
        graph.shutdown();
        OrientGraphWrapperFactory.dropAndDestroyInstance();
    }

    @Test
    public void testCreateComment() throws Exception {
        commentRepository.createOrUpdate(c1);
        Comment retrieved1 = commentRepository.find(c1.getId());
        assertNotNull("The retrieved comment is null ", retrieved1);
        assertEquals("The comments do not match", c1, retrieved1);
    }

    @Test
    public void testCreateSeveralComment() throws Exception {
        commentRepository.createOrUpdate(c1);
        commentRepository.createOrUpdate(c2);
        commentRepository.createOrUpdate(c3);
        Comment retrieved1 = commentRepository.find(c1.getId());
        Comment retrieved2 = commentRepository.find(c2.getId());
        Comment retrieved3 = commentRepository.find(c3.getId());
        assertNotNull("The retrieved comment is null ", retrieved1);
        assertNotNull("The retrieved comment is null ", retrieved2);
        assertNotNull("The retrieved comment is null ", retrieved3);
        assertEquals("The comments do not match", c1, retrieved1);
        assertEquals("The comments do not match", c2, retrieved2);
        assertEquals("The comments do not match", c3, retrieved3);
    }

    @Test
    public void testUpdateComment() throws Exception {
        commentRepository.createOrUpdate(c1);
        c1.setText(c1.getText() + "__");
        commentRepository.createOrUpdate(c1);
        Comment retrieved = commentRepository.find(c1.getId());
        assertNotNull("The retrieved comment is null ", retrieved);
        assertEquals("The comments do not match", c1, retrieved);
    }

    @Test
    public void testFindAll() throws Exception {
        m1.setComments(Arrays.asList(c1, c2));
        mediaRepository.createOrUpdate(m1);
        List<Comment> comments = commentRepository.findAll(m1.getId());
        assertEquals(2, comments.size());
        assertTrue(comments.contains(c1));
        assertTrue(comments.contains(c2));
    }

}