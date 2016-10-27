package io.reneses.tela.modules.instagram.repositories;

import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.modules.instagram.databases.extensions.InstagramOrientDatabaseExtension;
import io.reneses.tela.modules.instagram.models.Counts;
import io.reneses.tela.modules.instagram.models.User;
import io.reneses.tela.modules.instagram.models.UserRelationship;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;


public class OrientUserRepositoryTest {

    private OrientUserRepository repository;

    private User obama, trump, bush;

    @Before
    public void setUp() throws Exception {

        OrientGraphWrapperFactory.registerExtensions(new InstagramOrientDatabaseExtension());
        OrientGraphWrapperFactory.connectMemory("Test");

        repository = new OrientUserRepository();

        obama = new io.reneses.tela.modules.instagram.models.User();
        obama.setId(1);
        obama.setUsername("obama");
        obama.setPicture("obama.jpg");
        obama.setFullName("Barack Obama");
        obama.setBio("President!");
        obama.setWebsite("www.obama.usa");
        obama.setCounts(new Counts(10, 100, 2000));

        trump = new io.reneses.tela.modules.instagram.models.User();
        trump.setId(2);
        trump.setUsername("trump");
        trump.setPicture("trump.jpg");
        trump.setFullName("Donald Trump");

        bush = new io.reneses.tela.modules.instagram.models.User();
        bush.setId(3);
        bush.setUsername("bush");
        bush.setFullName("George Bush");
        bush.setPicture("bush.jpg");

    }

    @After
    public void tearDown() throws Exception {
        OrientGraphWrapperFactory.dropAndDestroyInstance();
    }

    @Test
    public void createBasic() throws Exception {
        repository.createOrUpdate(bush);
        User user = repository.find(bush.getId(), false);
        assertEquals(bush, user);
    }

    @Test
    public void createFull() throws Exception {
        repository.createOrUpdate(obama);
        assertEquals(obama, repository.find(obama.getId(), true));
    }

    @Test
    public void createUpdating() throws Exception {
        repository.createOrUpdate(obama);
        obama.setUsername(obama.getUsername() + "2");
        repository.createOrUpdate(obama);
        assertEquals(obama.getUsername(), repository.find(obama.getId(), false).getUsername());
    }

    @Test
    public void findFollowers() throws Exception {

        List<User> followers = Arrays.asList(obama, trump);
        repository.setFollowers(followers, bush);

        List<User> currentFollowers = repository.findFollowers(bush.getId());
        assertEquals(2, currentFollowers.size());
        assertTrue(currentFollowers.contains(obama));
        assertTrue(currentFollowers.contains(trump));

    }

    @Test
    public void findFollowing() throws Exception {

        List<User> following = Arrays.asList(obama, trump);
        repository.setFollowing(bush, following);

        List<User> retrieved = repository.findFollowing(bush.getId());
        assertEquals(2, retrieved.size());
        assertTrue(retrieved.contains(obama));
        assertTrue(retrieved.contains(trump));

    }

    @Test
    public void findFriends() throws Exception {
        repository.setFollowing(bush, Arrays.asList(obama, trump));
        repository.setFollowing(obama, Arrays.asList(bush));
        List<User> bushFriends = repository.findFriends(bush.getId());
        assertEquals(1, bushFriends.size());
        assertTrue(bushFriends.contains(obama));
        List<User> obamaFriends = repository.findFriends(obama.getId());
        assertEquals(1, obamaFriends.size());
        assertTrue(obamaFriends.contains(bush));
        List<User> trumpFriends = repository.findFriends(trump.getId());
        assertEquals(0, trumpFriends.size());
    }

    @Test
    public void counts() throws Exception {

        Counts count1 = new Counts(30, 200, 1);
        Counts count2 = new Counts(50, 250, 5);
        count2.setCreatedAt(new Date(count1.getCreatedAt().getTime()+1000));
        Counts count3 = new Counts(100, 300, 10);
        count3.setCreatedAt(new Date(count2.getCreatedAt().getTime()+1000));

        trump.setCounts(count1);
        repository.createOrUpdate(trump);

        trump.setCounts(count3);
        repository.createOrUpdate(trump);

        trump.setCounts(count2);
        repository.createOrUpdate(trump);

        List<Counts> counts = repository.counts(trump.getId());
        assertEquals(3, counts.size());
        assertTrue(counts.contains(count1));
        assertTrue(counts.contains(count2));
        assertTrue(counts.contains(count3));

    }

    @Test
    public void findLatestCount() throws Exception {

        Counts count1 = new Counts(30, 200, 1);
        Counts count2 = new Counts(50, 250, 5);
        count2.setCreatedAt(new Date(count1.getCreatedAt().getTime()+1000));
        Counts count3 = new Counts(100, 300, 10);
        count3.setCreatedAt(new Date(count2.getCreatedAt().getTime()+1000));

        trump.setCounts(count1);
        repository.createOrUpdate(trump);

        trump.setCounts(count3);
        repository.createOrUpdate(trump);

        trump.setCounts(count2);
        repository.createOrUpdate(trump);

        Counts retrieved = repository.find(trump.getId(), true).getCounts();
        assertNotNull(retrieved);
        assertEquals(count3, retrieved);

    }

    @Test
    public void findLatestRelationship() throws Exception {

        UserRelationship r1 = new UserRelationship();
        r1.setIncoming(UserRelationship.Incoming.BLOCKED);
        r1.setOutgoing(UserRelationship.Outgoing.FOLLOWS);
        r1.setTargetUserIsPrivate(true);
        r1.setCreatedAt(new Date());
        repository.createRelationship(obama, r1, trump.getId());

        UserRelationship r2 = new UserRelationship();
        r2.setIncoming(UserRelationship.Incoming.BLOCKED);
        r2.setOutgoing(UserRelationship.Outgoing.FOLLOWS);
        r2.setTargetUserIsPrivate(true);
        r2.setCreatedAt(new Date(r1.getCreatedAt().getTime()-1000));
        repository.createRelationship(obama, r2, trump.getId());

        assertEquals(r1, repository.findLatestRelationship(obama.getId(), trump.getId()));
    }

    @Test
    public void findRelationships() throws Exception {

        UserRelationship r1 = new UserRelationship();
        r1.setIncoming(UserRelationship.Incoming.BLOCKED);
        r1.setOutgoing(UserRelationship.Outgoing.FOLLOWS);
        r1.setTargetUserIsPrivate(true);
        r1.setCreatedAt(new Date());
        repository.createRelationship(obama, r1, trump.getId());

        UserRelationship r2 = new UserRelationship();
        r2.setIncoming(UserRelationship.Incoming.BLOCKED);
        r2.setOutgoing(UserRelationship.Outgoing.FOLLOWS);
        r2.setTargetUserIsPrivate(true);
        r2.setCreatedAt(new Date(r1.getCreatedAt().getTime()-1000));
        repository.createRelationship(obama, r2, trump.getId());

        List<UserRelationship> relationships = repository.findRelationships(obama.getId(), trump.getId());
        assertNotNull(relationships);
        assertEquals(2, relationships.size());
        assertTrue(relationships.contains(r1));
        assertTrue(relationships.contains(r2));

    }

}
