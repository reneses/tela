package io.reneses.tela.modules.instagram.databases.extensions;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import io.reneses.tela.core.databases.extensions.OrientDatabaseExtension;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapper;

import java.util.*;

/**
 * OrientDB extension for the Instagram module
 */
public class InstagramOrientDatabaseExtension extends OrientDatabaseExtension {

    public static class User {
        public static final String
                CLASS = "InstagramUser",
                FOLLOWS = "InstagramFollows", // Edge
                FULL_NAME = "full_name", WEBSITE = "website", USERNAME = "username", ID = "user_id",
                PICTURE = "profile_picture", BIO = "bio";

        public static class Count {
            public static final String
                    CLASS = "InstagramUserCounts",
                    COUNTS = "InstagramHasCounts", // Edge
                    MEDIA = "media", FOLLOWERS = "followers", FOLLOWING = "following",
                    CREATED_AT = "created_at";
        }
    }

    public static class Media {
        public static final String
                CLASS = "InstagramMedia",
                LIKES = "InstagramLikes", POSTS = "InstagramPosts", CAPTION = "InstagramCaption", // Edge
                ID = "media_id", LINK = "link", FILTER = "filter", TYPE = "type", HASHTAGS = "tags",
                N_LIKES = "n_likes", N_COMMENTS = "n_comments",
                CREATED_TIME = "created_at",
                USER_HAS_LIKED = "user_has_liked";

        public static class Tags {
            public static final String TAGS = "InstagramTags", TAGS_X = "x", TAGS_Y = "y";
        }

        public static class Location {
            public static final String
                    CLASS = "InstagramLocation", LOCATED_AT = "InstagramLocatedAt",
                    ID = "location_id", LONGITUDE = "longitude", LATITUDE = "latitude",
                    NAME = "name", ADDRESS = "address";
        }

        public static class Resource {
            public static final String
                    CLASS = "InstagramResource", VIDEO = "InstagramVideo", IMAGE = "InstagramImage",
                    URL = "url", WIDTH = "width", HEIGHT = "height", CODE = "code";
        }
    }

    public static class Comment {
        public static final String
                CLASS = "InstagramComment",
                COMMENTS = "InstagramComments", // Edge
                TEXT = "text", ID = "comment_id", CREATED_TIME = "created_time";
    }

    public static class Relationship {
        public static final String
                CLASS = "InstagramRelationship",
                RELATE = "InstagramRelates", RELATE_TARGET_ID = "target_id", RELATED_CREATED_AT = "created_at", // Edge
                INCOMING = "incoming", OUTGOING = "outgoing", TARGET_USER_IS_PRIVATE = "is_target_private";
    }

    private void createUserVertexClass(OrientGraphWrapper telaGraph, OrientBaseGraph graph) {

        // Create user vertex
        Map<String, OType> properties = new HashMap<>();
        properties.put(User.USERNAME, OType.STRING);
        properties.put(User.ID, OType.LONG);
        properties.put(User.PICTURE, OType.STRING);
        properties.put(User.FULL_NAME, OType.STRING);
        properties.put(User.WEBSITE, OType.STRING);
        telaGraph.createVertexClass(graph, User.CLASS, properties);

        // Create user indexes
        Map<String, OClass.INDEX_TYPE> indexes = new HashMap<>();
        indexes.put(User.USERNAME, OClass.INDEX_TYPE.UNIQUE_HASH_INDEX);
        indexes.put(User.ID, OClass.INDEX_TYPE.UNIQUE_HASH_INDEX);
        telaGraph.createIndexes(graph, User.CLASS, indexes);

    }

    private void createUserCountClassAndEdge(OrientGraphWrapper telaGraph, OrientBaseGraph graph) {

        // Create vertex
        Map<String, OType> vertexProperties = new HashMap<>();
        vertexProperties.put(User.Count.MEDIA, OType.INTEGER);
        vertexProperties.put(User.Count.FOLLOWERS, OType.INTEGER);
        vertexProperties.put(User.Count.FOLLOWING, OType.INTEGER);
        vertexProperties.put(User.Count.CREATED_AT, OType.DATETIME);
        telaGraph.createVertexClass(graph, User.Count.CLASS, vertexProperties);

        // Create  indexes
        Map<String, OClass.INDEX_TYPE> indexes = new HashMap<>();
        indexes.put(User.Count.CREATED_AT, OClass.INDEX_TYPE.NOTUNIQUE);
        telaGraph.createIndexes(graph, User.Count.CLASS, indexes);

        // Create edge
        telaGraph.createEdgeClass(graph, User.Count.COUNTS);

    }

    private void createFollowsEdgeClass(OrientGraphWrapper telaGraph, OrientBaseGraph graph) {
        Map<String, OType> properties = new HashMap<>();
        telaGraph.createEdgeClass(graph, User.FOLLOWS, properties);
    }

    private void createMediaVertexClass(OrientGraphWrapper telaGraph, OrientBaseGraph graph) {

        // Create media vertex
        Map<String, OType> properties = new HashMap<>();
        properties.put(Media.ID, OType.STRING);
        properties.put(Media.LINK, OType.STRING);
        properties.put(Media.FILTER, OType.STRING);
        properties.put(Media.TYPE, OType.STRING);
        properties.put(Media.HASHTAGS, OType.EMBEDDEDSET);
        properties.put(Media.USER_HAS_LIKED, OType.BOOLEAN);
        properties.put(Media.CREATED_TIME, OType.DATETIME);
        properties.put(Media.N_COMMENTS, OType.INTEGER);
        properties.put(Media.N_LIKES, OType.INTEGER);
        telaGraph.createVertexClass(graph, Media.CLASS, properties);

        // Create indexes
        Map<String, OClass.INDEX_TYPE> indexes = new HashMap<>();
        indexes.put(Media.ID, OClass.INDEX_TYPE.UNIQUE_HASH_INDEX);
        indexes.put(Media.CREATED_TIME, OClass.INDEX_TYPE.NOTUNIQUE);
        telaGraph.createIndexes(graph, Media.CLASS, indexes);

    }

    private void createMediaEdgeClasses(OrientGraphWrapper telaGraph, OrientBaseGraph graph) {

        telaGraph.createEdgeClass(graph, Media.LIKES);
        telaGraph.createEdgeClass(graph, Media.POSTS);
        telaGraph.createEdgeClass(graph, Media.CAPTION);

        Map<String, OType> tagsProperties = new HashMap<>();
        tagsProperties.put(Media.Tags.TAGS_X, OType.DOUBLE);
        tagsProperties.put(Media.Tags.TAGS_Y, OType.DOUBLE);
        telaGraph.createEdgeClass(graph, Media.Tags.TAGS, tagsProperties);

    }

    private void createMediaLocationClasses(OrientGraphWrapper telaGraph, OrientBaseGraph graph) {

        telaGraph.createEdgeClass(graph, Media.Location.LOCATED_AT);

        Map<String, OType> properties = new HashMap<>();
        properties.put(Media.Location.ADDRESS, OType.STRING);
        properties.put(Media.Location.NAME, OType.STRING);
        properties.put(Media.Location.ID, OType.LONG);
        properties.put(Media.Location.LONGITUDE, OType.DOUBLE);
        properties.put(Media.Location.LATITUDE, OType.DOUBLE);
        telaGraph.createVertexClass(graph, Media.Location.CLASS, properties);

    }

    private void createMediaResourceClasses(OrientGraphWrapper telaGraph, OrientBaseGraph graph) {

        telaGraph.createEdgeClass(graph, Media.Resource.VIDEO);
        telaGraph.createEdgeClass(graph, Media.Resource.IMAGE);

        Map<String, OType> properties = new HashMap<>();
        properties.put(Media.Resource.URL, OType.STRING);
        properties.put(Media.Resource.WIDTH, OType.INTEGER);
        properties.put(Media.Resource.HEIGHT, OType.INTEGER);
        properties.put(Media.Resource.CODE, OType.STRING);
        telaGraph.createVertexClass(graph, Media.Resource.CLASS, properties);

    }

    private void createCommentVertexAndEdgeClasses(OrientGraphWrapper telaGraph, OrientBaseGraph graph) {

        // Create vertex
        Map<String, OType> vertexProperties = new HashMap<>();
        vertexProperties.put(Comment.ID, OType.LONG);
        vertexProperties.put(Comment.TEXT, OType.STRING);
        vertexProperties.put(Comment.CREATED_TIME, OType.DATETIME);
        telaGraph.createVertexClass(graph, Comment.CLASS, vertexProperties);

        // Create edge
        telaGraph.createEdgeClass(graph, Comment.COMMENTS);

    }

    private void createRelationshipVertexAndEdgeClasses(OrientGraphWrapper telaGraph, OrientBaseGraph graph) {

        // Create vertex
        Map<String, OType> vertexProperties = new HashMap<>();
        vertexProperties.put(Relationship.INCOMING, OType.STRING);
        vertexProperties.put(Relationship.OUTGOING, OType.STRING);
        vertexProperties.put(Relationship.TARGET_USER_IS_PRIVATE, OType.BOOLEAN);
        telaGraph.createVertexClass(graph, Relationship.CLASS, vertexProperties);

        // Create edge
        Map<String, OType> edgeProperties = new HashMap<>();
        edgeProperties.put(Relationship.RELATE_TARGET_ID, OType.LONG);
        edgeProperties.put(Relationship.RELATED_CREATED_AT, OType.DATETIME);
        telaGraph.createEdgeClass(graph, Relationship.RELATE, edgeProperties);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInitiated(OrientGraphWrapper telaGraph) {
        OrientBaseGraph graph = telaGraph.getNoTxGraph();
        boolean isInitiated = telaGraph.existsClass(graph, User.CLASS)
                && telaGraph.existsClass(graph, User.Count.CLASS)
                && telaGraph.existsClass(graph, Media.CLASS)
                && telaGraph.existsClass(graph, Media.Location.CLASS)
                && telaGraph.existsClass(graph, Comment.CLASS)
                && telaGraph.existsClass(graph, Relationship.CLASS);
        graph.shutdown();
        return isInitiated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(OrientGraphWrapper telaGraph) {
        OrientBaseGraph graph = telaGraph.getNoTxGraph();
        createUserVertexClass(telaGraph, graph);
        createUserCountClassAndEdge(telaGraph, graph);
        createFollowsEdgeClass(telaGraph, graph);
        createMediaVertexClass(telaGraph, graph);
        createMediaEdgeClasses(telaGraph, graph);
        createMediaLocationClasses(telaGraph, graph);
        createMediaResourceClasses(telaGraph, graph);
        createCommentVertexAndEdgeClasses(telaGraph, graph);
        createRelationshipVertexAndEdgeClasses(telaGraph, graph);
        graph.shutdown();
    }

}
