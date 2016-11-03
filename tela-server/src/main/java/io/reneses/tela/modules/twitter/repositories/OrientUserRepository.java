package io.reneses.tela.modules.twitter.repositories;

import com.orientechnologies.orient.core.storage.ORecordDuplicatedException;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapper;
import io.reneses.tela.modules.twitter.database.extensions.TwitterOrientDatabaseExtension;
import io.reneses.tela.modules.twitter.models.User;

import java.util.*;

/**
 * OrientDB user repository implementation
 */
public class OrientUserRepository implements UserRepository {

    private OrientGraphWrapper telaGraph = OrientGraphWrapperFactory.get();

    //------------------------ AUX ------------------------//

    private Map<String, Object> getProperties(User user) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(TwitterOrientDatabaseExtension.User.ID, user.getId());
        if (user.getName() != null)
            properties.put(TwitterOrientDatabaseExtension.User.NAME, user.getName());
        if (user.getUrl() != null)
            properties.put(TwitterOrientDatabaseExtension.User.URL, user.getUrl());
        properties.put(TwitterOrientDatabaseExtension.User.PICTURE, user.getPicture());
        properties.put(TwitterOrientDatabaseExtension.User.SCREEN_NAME, user.getScreenName());
        properties.put(TwitterOrientDatabaseExtension.User.NUMBER_OF_FOLLOWERS, user.getNumberOfFollowers());
        properties.put(TwitterOrientDatabaseExtension.User.NUMBER_OF_FOLLOWING, user.getNumberOfFollowing());
        return properties;
    }

    OrientVertex createUserVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph, User user) {
        return telaGraph.addVertex(
                graph,
                TwitterOrientDatabaseExtension.User.CLASS,
                getProperties(user));
    }

    OrientVertex findUserVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph, String screenName) {
        return telaGraph.getVertex(
                graph,
                TwitterOrientDatabaseExtension.User.CLASS,
                TwitterOrientDatabaseExtension.User.SCREEN_NAME, screenName);
    }

    OrientVertex updateUserVertex(OrientVertex vertex, User user) {
        return vertex.setProperties(getProperties(user));
    }


    OrientVertex createOrUpdateUserVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph, User user) {
        OrientVertex userVertex = findUserVertex(telaGraph, graph, user.getScreenName());
        if (userVertex != null) {
            return updateUserVertex(userVertex, user);
        }
        try {
            return createUserVertex(telaGraph, graph, user);
        } catch (ORecordDuplicatedException e) {
            return findUserVertex(telaGraph, graph, user.getScreenName());
        }
    }

    User mapVertex(Vertex vertex) {
        User user = new User();
        user.setId(vertex.getProperty(TwitterOrientDatabaseExtension.User.ID));
        user.setName(vertex.getProperty(TwitterOrientDatabaseExtension.User.NAME));
        user.setUrl(vertex.getProperty(TwitterOrientDatabaseExtension.User.URL));
        user.setPicture(vertex.getProperty(TwitterOrientDatabaseExtension.User.PICTURE));
        user.setScreenName(vertex.getProperty(TwitterOrientDatabaseExtension.User.SCREEN_NAME));
        user.setNumberOfFollowers(vertex.getProperty(TwitterOrientDatabaseExtension.User.NUMBER_OF_FOLLOWERS));
        user.setNumberOfFollowing(vertex.getProperty(TwitterOrientDatabaseExtension.User.NUMBER_OF_FOLLOWING));
        return user;
    }

    //------------------------ RELATIONSHIPS ------------------------//

    private void follow(OrientGraphWrapper telaGraph, OrientBaseGraph graph, Iterable<User> followers, Iterable<User> followed) {
        for (User follower : followers) {
            OrientVertex followerVertex = createOrUpdateUserVertex(telaGraph, graph, follower);
            for (User f : followed) {
                OrientVertex followedVertex = createOrUpdateUserVertex(telaGraph, graph, f);
                telaGraph.addEdge(graph, TwitterOrientDatabaseExtension.User.FOLLOWS, followerVertex, followedVertex);
            }
        }
    }

    private List<User> findFollowRelationship(OrientGraphWrapper telaGraph, OrientBaseGraph graph, String screenName, Direction direction) {
        OrientVertex userVertex = findUserVertex(telaGraph, graph, screenName);
        List<User> relationship = new ArrayList<>();
        if (userVertex != null) {
            for (Edge edge : userVertex.getEdges(direction, TwitterOrientDatabaseExtension.User.FOLLOWS)) {
                Vertex vertex = edge.getVertex(direction.opposite());
                relationship.add(mapVertex(vertex));
            }
        }
        return relationship;
    }

    private void deleteRelationship(OrientGraphWrapper telaGraph, OrientBaseGraph graph, String screenName, Direction direction) {
        if (direction == Direction.BOTH) {
            deleteRelationship(telaGraph, graph, screenName, Direction.IN);
            deleteRelationship(telaGraph, graph, screenName, Direction.OUT);
            return;
        }
        String command = String.format("DELETE EDGE %s %s (SELECT FROM %s where %s = ?) ",
                TwitterOrientDatabaseExtension.User.FOLLOWS,
                direction == Direction.IN ? "TO" : "FROM",
                TwitterOrientDatabaseExtension.User.CLASS,
                TwitterOrientDatabaseExtension.User.SCREEN_NAME
        );
        telaGraph.execute(graph, command, screenName);
    }

    private void deleteFollowers(OrientGraphWrapper telaGraph, OrientBaseGraph graph, String screenName) {
        deleteRelationship(telaGraph, graph, screenName, Direction.IN);
    }

    private void deleteFollowing(OrientGraphWrapper telaGraph, OrientBaseGraph graph, String screenName) {
        deleteRelationship(telaGraph, graph, screenName, Direction.OUT);
    }

    //------------------------ OVERRIDE ------------------------//

    /** {@inheritDoc} */
    @Override
    public User find(String username) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex vertex = findUserVertex(telaGraph, graph, username);
        User user = (vertex == null) ? null : mapVertex(vertex);
        graph.shutdown();
        return user;
    }

    /** {@inheritDoc} */
    @Override
    public void create(User user) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        try {
            createOrUpdateUserVertex(telaGraph, graph, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        graph.shutdown();
    }

    /** {@inheritDoc} */
    @Override
    public void setFollowers(Iterable<User> followers, User followed) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        deleteFollowers(telaGraph, graph, followed.getScreenName());
        follow(telaGraph, graph, followers, Arrays.asList(followed));
        graph.shutdown();
    }

    /** {@inheritDoc} */
    @Override
    public void setFollowing(User following, Iterable<User> followed) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        deleteFollowing(telaGraph, graph, following.getScreenName());
        follow(telaGraph, graph, Arrays.asList(following), followed);
        graph.shutdown();
    }

    /** {@inheritDoc} */
    @Override
    public List<User> findFollowers(String username) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        List<User> result = findFollowRelationship(telaGraph, graph, username, Direction.IN);
        graph.shutdown();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public List<User> findFollowing(String username) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        List<User> result = findFollowRelationship(telaGraph, graph, username, Direction.OUT);
        graph.shutdown();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public List<User> findFriends(String username) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        String command = String.format("SELECT EXPAND(INTERSECT(IN('%s'), OUT('%s'))) FROM %s WHERE %s = ?",
                TwitterOrientDatabaseExtension.User.FOLLOWS, TwitterOrientDatabaseExtension.User.FOLLOWS,
                TwitterOrientDatabaseExtension.User.CLASS, TwitterOrientDatabaseExtension.User.SCREEN_NAME);
        Iterable<Object> iterable = telaGraph.executeAndFetch(graph, command, username);
        List<User> friends = new ArrayList<>();
        for (Object o : iterable) {
            friends.add(mapVertex((Vertex) o));
        }
        graph.shutdown();
        return friends;
    }

}
