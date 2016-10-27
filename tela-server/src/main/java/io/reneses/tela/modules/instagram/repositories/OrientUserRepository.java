package io.reneses.tela.modules.instagram.repositories;

import com.orientechnologies.orient.core.storage.ORecordDuplicatedException;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapper;
import io.reneses.tela.modules.instagram.models.Counts;
import io.reneses.tela.modules.instagram.databases.extensions.InstagramOrientDatabaseExtension;
import io.reneses.tela.modules.instagram.models.User;
import io.reneses.tela.modules.instagram.models.UserRelationship;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * OrientDB implementation of a UserRepository
 */
public class OrientUserRepository extends AbstractOrientRepository implements UserRepository {

    //------------------------ AUX ------------------------//

    private static Map<String, Object> getUserProperties(User user) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(InstagramOrientDatabaseExtension.User.USERNAME, user.getUsername());
        properties.put(InstagramOrientDatabaseExtension.User.ID, user.getId());
        properties.put(InstagramOrientDatabaseExtension.User.PICTURE, user.getPicture());
        properties.put(InstagramOrientDatabaseExtension.User.FULL_NAME, user.getFullName());
        if (user.getBio() != null)
            properties.put(InstagramOrientDatabaseExtension.User.BIO, user.getBio());
        if (user.getWebsite() != null)
            properties.put(InstagramOrientDatabaseExtension.User.WEBSITE, user.getWebsite());
        return properties;
    }

    static OrientVertex findUserVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph, long userId) {
        return telaGraph.getVertex(graph,
                InstagramOrientDatabaseExtension.User.CLASS,
                InstagramOrientDatabaseExtension.User.ID, userId);
    }

    private static OrientVertex createUserVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph, User user) {
        return telaGraph.addVertex(
                graph,
                InstagramOrientDatabaseExtension.User.CLASS,
                getUserProperties(user));
    }

    private static OrientVertex updateUserVertex(OrientVertex userVertex, User user) {
        return userVertex.setProperties(getUserProperties(user));
    }

    static OrientVertex createOrUpdateUserVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph, User user) {
        OrientVertex userVertex = findUserVertex(telaGraph, graph, user.getId());
        if (userVertex != null)
            return updateUserVertex(userVertex, user);
        try {
            return createUserVertex(telaGraph, graph, user);
        } catch (ORecordDuplicatedException e) { // If other thread insert it in the meanwhile
            return findUserVertex(telaGraph, graph, user.getId());
        }
    }

    private static OrientVertex createUserCountVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph, OrientVertex userVertex, User user) {
        Counts counts = user.getCounts();
        OrientVertex countVertex = null;
        if (user.getCounts() != null) {
            Map<String, Object> countProperties = new HashMap<>();
            countProperties.put(InstagramOrientDatabaseExtension.User.Count.CREATED_AT, counts.getCreatedAt());
            countProperties.put(InstagramOrientDatabaseExtension.User.Count.FOLLOWERS, counts.getNumberOfFollowers());
            countProperties.put(InstagramOrientDatabaseExtension.User.Count.FOLLOWING, counts.getNumberOfFollowing());
            countProperties.put(InstagramOrientDatabaseExtension.User.Count.MEDIA, counts.getNumberOfMedia());
            countVertex = telaGraph.addVertex(graph, InstagramOrientDatabaseExtension.User.Count.CLASS, countProperties);
            telaGraph.addEdge(graph, InstagramOrientDatabaseExtension.User.Count.COUNTS, userVertex, countVertex);
        }
        return countVertex;
    }

    private static Counts mapCountsVertex(Vertex vertex) {
        Counts counts = new Counts();
        counts.setNumberOfFollowers(vertex.getProperty(InstagramOrientDatabaseExtension.User.Count.FOLLOWERS));
        counts.setNumberOfMedia(vertex.getProperty(InstagramOrientDatabaseExtension.User.Count.MEDIA));
        counts.setNumberOfFollowing(vertex.getProperty(InstagramOrientDatabaseExtension.User.Count.FOLLOWING));
        counts.setCreatedAt(vertex.getProperty(InstagramOrientDatabaseExtension.User.Count.CREATED_AT));
        return counts;
    }

    static Counts findLatestCounts(OrientGraphWrapper telaGraph, OrientBaseGraph graph, long userId) {
        String command = String.format("SELECT EXPAND(OUT('%s')) FROM %s WHERE %s = ? ORDER BY %s DESC LIMIT 1",
                InstagramOrientDatabaseExtension.User.Count.COUNTS,
                InstagramOrientDatabaseExtension.User.CLASS,
                InstagramOrientDatabaseExtension.User.ID,
                InstagramOrientDatabaseExtension.User.Count.CREATED_AT);
        Iterable<Object> iterable = telaGraph.executeAndFetch(graph, command, userId);
        if (iterable.iterator().hasNext()) {
            return  mapCountsVertex((Vertex) iterable.iterator().next());
        }
        return null;
    }

    static User mapUserVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph, Vertex vertex, boolean crawlCount) {

        User user = new User();

        // Default crawling
        user.setId(vertex.getProperty(InstagramOrientDatabaseExtension.User.ID));
        user.setUsername(vertex.getProperty(InstagramOrientDatabaseExtension.User.USERNAME));
        user.setPicture(vertex.getProperty(InstagramOrientDatabaseExtension.User.PICTURE));
        user.setFullName(vertex.getProperty(InstagramOrientDatabaseExtension.User.FULL_NAME));
        user.setWebsite(vertex.getProperty(InstagramOrientDatabaseExtension.User.WEBSITE));
        user.setBio(vertex.getProperty(InstagramOrientDatabaseExtension.User.BIO));

        // Counts crawling
        if (crawlCount) {
            user.setCounts(findLatestCounts(telaGraph, graph, user.getId()));
        }

        return user;
    }

    //------------------------ AUX RELATIONSHIPS ------------------------//


    private List<User> findRelationship(OrientGraphWrapper telaGraph, OrientBaseGraph graph, long userId, Direction direction) {
        OrientVertex userVertex = findUserVertex(telaGraph, graph, userId);
        Iterable<Edge> usersEdges = userVertex.getEdges(direction, InstagramOrientDatabaseExtension.User.FOLLOWS);
        List<User> users = StreamSupport.stream(usersEdges.spliterator(), false)
                .map(edge -> edge.getVertex(direction.opposite()))
                .map(vertex -> mapUserVertex(telaGraph, graph, vertex, false))
                .collect(Collectors.toList());
        return users;
    }


    private void deleteRelationship(OrientGraphWrapper telaGraph, OrientBaseGraph graph, long userId, Direction direction) {
        if (direction == Direction.BOTH) {
            deleteRelationship(telaGraph, graph, userId, Direction.IN);
            deleteRelationship(telaGraph, graph, userId, Direction.OUT);
            return;
        }
        String command = String.format("DELETE EDGE %s %s (SELECT FROM %s where %s = ?) ",
                InstagramOrientDatabaseExtension.User.FOLLOWS,
                direction == Direction.IN ? "TO" : "FROM",
                InstagramOrientDatabaseExtension.User.CLASS,
                InstagramOrientDatabaseExtension.User.ID
        );
        telaGraph.execute(graph, command, userId);
    }

    private void deleteFollowers(OrientGraphWrapper telaGraph, OrientBaseGraph graph, long userId) {
        deleteRelationship(telaGraph, graph, userId, Direction.IN);
    }

    private void deleteFollowing(OrientGraphWrapper telaGraph, OrientBaseGraph graph, long userId) {
        deleteRelationship(telaGraph, graph, userId, Direction.OUT);
    }

    private void follow(OrientGraphWrapper telaGraph, OrientBaseGraph graph, List<User> followers, List<User> followed) {
        for (User follower : followers) {
            OrientVertex followerVertex = createOrUpdateUserVertex(telaGraph, graph, follower);
            for (User f : followed) {
                OrientVertex followedVertex = createOrUpdateUserVertex(telaGraph, graph, f);
                telaGraph.addEdge(graph, InstagramOrientDatabaseExtension.User.FOLLOWS, followerVertex, followedVertex);
            }
        }
    }


    //------------------------ OVERRIDE ------------------------//

    /** {@inheritDoc} */
    @Override
    public User find(long userId, boolean withCount) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex vertex = findUserVertex(telaGraph, graph, userId);
        User user = (vertex == null) ? null : mapUserVertex(telaGraph, graph, vertex, withCount);
        graph.shutdown();
        return user;
    }

    /** {@inheritDoc} */
    @Override
    public User find(long userId) {
        return find(userId, false);
    }

    /** {@inheritDoc} */
    @Override
    public User find(String username, boolean withCount) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex vertex = telaGraph.getVertex(graph, InstagramOrientDatabaseExtension.User.CLASS, InstagramOrientDatabaseExtension.User.USERNAME, username);
        User user = (vertex == null) ? null : mapUserVertex(telaGraph, graph, vertex, withCount);
        graph.shutdown();
        return user;
    }

    /** {@inheritDoc} */
    @Override
    public User find(String username) {
        return find(username, false);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> findAll(boolean withCount) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        List<OrientVertex> vertices = telaGraph.getVertices(graph, InstagramOrientDatabaseExtension.User.CLASS);
        List<User> users = vertices
                .stream()
                .map(v -> mapUserVertex(telaGraph, graph, v, withCount))
                .collect(Collectors.toList());
        graph.shutdown();
        return users;
    }

    /** {@inheritDoc} */
    @Override
    public List<User> findAll() {
        return findAll(false);
    }

    /** {@inheritDoc} */
    @Override
    public void createOrUpdate(User user) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        try {
            OrientVertex userVertex = createOrUpdateUserVertex(telaGraph, graph, user);
            if (user.getCounts() != null)
                createUserCountVertex(telaGraph, graph, userVertex, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        graph.shutdown();
    }

    /** {@inheritDoc} */
    @Override
    public void setFollowers(List<User> followers, User followed) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        deleteFollowers(telaGraph, graph, followed.getId());
        follow(telaGraph, graph, followers, Arrays.asList(followed));
        graph.shutdown();
    }

    /** {@inheritDoc} */
    @Override
    public void setFollowing(User follower, List<User> following) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        deleteFollowing(telaGraph, graph, follower.getId());
        follow(telaGraph, graph, Arrays.asList(follower), following);
        graph.shutdown();
    }

    /** {@inheritDoc} */
    @Override
    public List<User> findFollowers(long followedId) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        List<User> result = findRelationship(telaGraph, graph, followedId, Direction.IN);
        graph.shutdown();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public List<User> findFollowing(long followingId) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        List<User> result = findRelationship(telaGraph, graph, followingId, Direction.OUT);
        graph.shutdown();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public List<User> findFriends(long userId) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        String command = String.format("SELECT EXPAND(INTERSECT(IN('%s'), OUT('%s'))) FROM %s WHERE %s = ?",
                InstagramOrientDatabaseExtension.User.FOLLOWS,
                InstagramOrientDatabaseExtension.User.FOLLOWS,
                InstagramOrientDatabaseExtension.User.CLASS,
                InstagramOrientDatabaseExtension.User.ID);
        Iterable<Object> iterable = telaGraph.executeAndFetch(graph, command, userId);
        List<User> friends = new ArrayList<>();
        for (Object o : iterable) {
            friends.add(mapUserVertex(telaGraph, graph, (Vertex) o, false));
        }
        graph.shutdown();
        return friends;
    }

    /** {@inheritDoc} */
    @Override
    public List<Counts> counts(long userId) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        String command = String.format("SELECT EXPAND(OUT('%s')) FROM %s WHERE %s = ?",
                InstagramOrientDatabaseExtension.User.Count.COUNTS,
                InstagramOrientDatabaseExtension.User.CLASS,
                InstagramOrientDatabaseExtension.User.ID);
        Iterable<Object> iterable = telaGraph.executeAndFetch(graph, command, userId);
        List<Counts> output = new ArrayList<>();
        for (Object o : iterable) {
            output.add(mapCountsVertex((Vertex) o));
        }
        graph.shutdown();
        return output;
    }

    /** {@inheritDoc} */
    @Override
    public void createRelationship(User source, UserRelationship relationship, long targetId) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        try {

            Map<String, Object> properties = new HashMap<>();
            properties.put(InstagramOrientDatabaseExtension.Relationship.INCOMING, relationship.getIncoming());
            properties.put(InstagramOrientDatabaseExtension.Relationship.OUTGOING, relationship.getOutgoing());
            properties.put(InstagramOrientDatabaseExtension.Relationship.TARGET_USER_IS_PRIVATE, relationship.isTargetUserIsPrivate());
            OrientVertex relationshipVertex = telaGraph.addVertex(
                    graph,
                    InstagramOrientDatabaseExtension.Relationship.CLASS,
                    properties);

            Map<String, Object> edgeProperties = new HashMap<>();
            edgeProperties.put(InstagramOrientDatabaseExtension.Relationship.RELATED_CREATED_AT, relationship.getCreatedAt());
            edgeProperties.put(InstagramOrientDatabaseExtension.Relationship.RELATE_TARGET_ID, targetId);

            OrientVertex sourceVertex = createOrUpdateUserVertex(telaGraph, graph, source);
            telaGraph.addEdge(graph, InstagramOrientDatabaseExtension.Relationship.RELATE,
                    sourceVertex, relationshipVertex, edgeProperties);


        } catch (Exception e) {
            e.printStackTrace();
        }
        graph.shutdown();
    }

    private UserRelationship mapVertexRelationship(OrientVertex vertex) {
        UserRelationship relationship = new UserRelationship();
        relationship.setIncoming((String) vertex.getProperty(InstagramOrientDatabaseExtension.Relationship.INCOMING));
        relationship.setOutgoing((String) vertex.getProperty(InstagramOrientDatabaseExtension.Relationship.OUTGOING));
        relationship.setTargetUserIsPrivate(vertex.getProperty(InstagramOrientDatabaseExtension.Relationship.TARGET_USER_IS_PRIVATE));
        return relationship;
    }

    /** {@inheritDoc} */
    @Override
    public UserRelationship findLatestRelationship(long sourceUserId, long targetUserId) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex sourceVertex = findUserVertex(telaGraph, graph, sourceUserId);
        Edge latestEdge = null;
        Date latestEdgeDate = null;
        for (Edge edge : sourceVertex.getEdges(Direction.OUT, InstagramOrientDatabaseExtension.Relationship.RELATE)) {
            long edgeTargetId = edge.getProperty(InstagramOrientDatabaseExtension.Relationship.RELATE_TARGET_ID);
            if (edgeTargetId != targetUserId)
                continue;
            Date createdAt = edge.getProperty(InstagramOrientDatabaseExtension.Relationship.RELATED_CREATED_AT);
            if (latestEdgeDate == null || latestEdgeDate.after(createdAt)) {
                latestEdge = edge;
                latestEdgeDate = createdAt;
            }
        }
        if (latestEdge == null)
            return null;
        UserRelationship relationship = mapVertexRelationship((OrientVertex) latestEdge.getVertex(Direction.IN));
        graph.shutdown();
        return relationship;
    }


    /** {@inheritDoc} */
    @Override
    public List<UserRelationship> findRelationships(long sourceUserId, long targetUserId) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex sourceVertex = findUserVertex(telaGraph, graph, sourceUserId);
        List<UserRelationship> relationships = new ArrayList<>();
        for (Edge edge : sourceVertex.getEdges(Direction.OUT, InstagramOrientDatabaseExtension.Relationship.RELATE)) {
            long edgeTargetId = edge.getProperty(InstagramOrientDatabaseExtension.Relationship.RELATE_TARGET_ID);
            if (edgeTargetId != targetUserId)
                continue;
            relationships.add(mapVertexRelationship((OrientVertex) edge.getVertex(Direction.IN)));
        }
        graph.shutdown();
        return relationships;
    }

}
