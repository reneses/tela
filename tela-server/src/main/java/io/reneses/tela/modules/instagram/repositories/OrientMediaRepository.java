package io.reneses.tela.modules.instagram.repositories;

import com.orientechnologies.orient.core.storage.ORecordDuplicatedException;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapper;
import io.reneses.tela.modules.instagram.models.*;
import io.reneses.tela.modules.instagram.databases.extensions.InstagramOrientDatabaseExtension;

import java.util.*;

/**
 * OrientDB implementation of a MediaRepository
 */
public class OrientMediaRepository extends AbstractOrientRepository implements MediaRepository {

    private CommentRepository commentRepository = new OrientCommentRepository();

    //------------------------ AUX ------------------------//

    private static Map<String, Object> getMediaProperties(Media media) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(InstagramOrientDatabaseExtension.Media.ID, media.getId());
        properties.put(InstagramOrientDatabaseExtension.Media.LINK, media.getLink());
        properties.put(InstagramOrientDatabaseExtension.Media.FILTER, media.getFilter());
        properties.put(InstagramOrientDatabaseExtension.Media.TYPE, media.getType());
        properties.put(InstagramOrientDatabaseExtension.Media.CREATED_TIME, media.getCreatedTime());
        properties.put(InstagramOrientDatabaseExtension.Media.HASHTAGS, media.getTags());
        properties.put(InstagramOrientDatabaseExtension.Media.N_LIKES, media.getNumberOfLikes());
        properties.put(InstagramOrientDatabaseExtension.Media.N_COMMENTS, media.getNumberOfComments());
        if (media.getUserHasLiked() != null)
            properties.put(InstagramOrientDatabaseExtension.Media.USER_HAS_LIKED, media.getUserHasLiked());
        return properties;
    }


    static OrientVertex findMediaVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph, String mediaId) {
        return telaGraph.getVertex(graph,
                InstagramOrientDatabaseExtension.Media.CLASS,
                InstagramOrientDatabaseExtension.Media.ID, mediaId);
    }

    private static List<User> mapLikesVertices(OrientGraphWrapper telaGraph, OrientBaseGraph graph, Vertex mediaVertex) {
        List<User> likes = new ArrayList<>();
        for (Vertex userLikeVertex : mediaVertex.getVertices(Direction.IN, InstagramOrientDatabaseExtension.Media.LIKES)) {
            User userLike = OrientUserRepository.mapUserVertex(telaGraph, graph, userLikeVertex, false);
            likes.add(userLike);
        }
        return likes;
    }

    private static Map<String, MediaResource> mapResourcesVertices(OrientGraphWrapper telaGraph, OrientBaseGraph graph, Vertex mediaVertex, String resourceEdge) {
        Map<String, MediaResource> resources = new HashMap<>();
        for (Vertex resourceVertex : mediaVertex.getVertices(Direction.OUT, resourceEdge)) {
            MediaResource resource = new MediaResource();
            resource.setWidth(resourceVertex.getProperty(InstagramOrientDatabaseExtension.Media.Resource.WIDTH));
            resource.setHeight(resourceVertex.getProperty(InstagramOrientDatabaseExtension.Media.Resource.HEIGHT));
            resource.setUrl(resourceVertex.getProperty(InstagramOrientDatabaseExtension.Media.Resource.URL));
            String code = resourceVertex.getProperty(InstagramOrientDatabaseExtension.Media.Resource.CODE);
            resources.put(code, resource);
        }
        return resources;
    }


    private static Location mapLocationVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph, Vertex mediaVertex) {
        Iterator<Vertex> iterator = mediaVertex.getVertices(Direction.OUT, InstagramOrientDatabaseExtension.Media.Location.LOCATED_AT).iterator();
        if (!iterator.hasNext())
            return null;
        Vertex vertex = iterator.next();
        Location location = new Location();
        location.setId(vertex.getProperty(InstagramOrientDatabaseExtension.Media.Location.ID));
        location.setLongitude(vertex.getProperty(InstagramOrientDatabaseExtension.Media.Location.LONGITUDE));
        location.setLatitude(vertex.getProperty(InstagramOrientDatabaseExtension.Media.Location.LATITUDE));
        location.setName(vertex.getProperty(InstagramOrientDatabaseExtension.Media.Location.NAME));
        location.setStreetAddress(vertex.getProperty(InstagramOrientDatabaseExtension.Media.Location.ADDRESS));
        return location;
    }

    private static List<TaggedUser> mapTagsVertices(OrientGraphWrapper telaGraph, OrientBaseGraph graph, Vertex mediaVertex) {
        List<TaggedUser> tags = new ArrayList<>();
        for (Edge edge : mediaVertex.getEdges(Direction.OUT, InstagramOrientDatabaseExtension.Media.Tags.TAGS)) {
            Vertex userVertex = edge.getVertex(Direction.IN);
            User user = OrientUserRepository.mapUserVertex(telaGraph, graph, userVertex, false);
            double x = edge.getProperty(InstagramOrientDatabaseExtension.Media.Tags.TAGS_X);
            double y = edge.getProperty(InstagramOrientDatabaseExtension.Media.Tags.TAGS_Y);
            TaggedUser tag = new TaggedUser();
            tag.setUser(user);
            tag.setPosition(new Position(x, y));
            tags.add(tag);
        }
        return tags;
    }

    private Media mapMediaVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph, Vertex vertex) {

        Media media = new Media();

        // Basic properties
        media.setId(vertex.getProperty(InstagramOrientDatabaseExtension.Media.ID));
        media.setLink(vertex.getProperty(InstagramOrientDatabaseExtension.Media.LINK));
        media.setFilter(vertex.getProperty(InstagramOrientDatabaseExtension.Media.FILTER));
        media.setType(vertex.getProperty(InstagramOrientDatabaseExtension.Media.TYPE));
        media.setCreatedTime(vertex.getProperty(InstagramOrientDatabaseExtension.Media.CREATED_TIME));
        media.setNumberOfLikes(vertex.getProperty(InstagramOrientDatabaseExtension.Media.N_LIKES));
        media.setNumberOfComments(vertex.getProperty(InstagramOrientDatabaseExtension.Media.N_COMMENTS));
        if (vertex.getPropertyKeys().contains(InstagramOrientDatabaseExtension.Media.USER_HAS_LIKED))
            media.setUserHasLiked(vertex.getProperty(InstagramOrientDatabaseExtension.Media.USER_HAS_LIKED));

        // Hashtags
        media.setTags(vertex.getProperty(InstagramOrientDatabaseExtension.Media.HASHTAGS));

        // User
        Edge postsEdge = vertex.getEdges(Direction.IN, InstagramOrientDatabaseExtension.Media.POSTS).iterator().next();
        Vertex userVertex = postsEdge.getVertex(Direction.OUT);
        User user = OrientUserRepository.mapUserVertex(telaGraph, graph, userVertex, false);
        media.setUser(user);

        // Likes
        media.setLikes(mapLikesVertices(telaGraph, graph, vertex));

        // Tags
        media.setTaggedUsers(mapTagsVertices(telaGraph, graph, vertex));

        // Location
        media.setLocation(mapLocationVertex(telaGraph, graph, vertex));

        // Resources
        media.setImages(mapResourcesVertices(telaGraph, graph, vertex, InstagramOrientDatabaseExtension.Media.Resource.IMAGE));
        media.setVideos(mapResourcesVertices(telaGraph, graph, vertex, InstagramOrientDatabaseExtension.Media.Resource.VIDEO));

        // Comments
        media.setComments(commentRepository.findAll(media.getId()));

        // Caption
        Iterator<Edge> captionIterable = vertex.getEdges(Direction.IN, InstagramOrientDatabaseExtension.Media.CAPTION).iterator();
        if (captionIterable.hasNext()) {
            Edge captionEdge = captionIterable.next();
            Vertex captionVertex = captionEdge.getVertex(Direction.OUT);
            Comment caption = OrientCommentRepository.mapCommentVertex(telaGraph, graph, captionVertex, false);
            caption.setUser(user);
            media.setCaption(caption);
        }

        return media;

    }

    private static void createLikesVertices(OrientGraphWrapper telaGraph, OrientBaseGraph graph, OrientVertex mediaVertex, List<User> likes) {
        if (likes == null)
            return;
        for (User userLike : likes) {
            OrientVertex userLikeVertex = OrientUserRepository.createOrUpdateUserVertex(telaGraph, graph, userLike);
            telaGraph.addEdge(graph, InstagramOrientDatabaseExtension.Media.LIKES, userLikeVertex, mediaVertex);
        }
    }

    private static void createLocationVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph, OrientVertex mediaVertex, Location location) {
        if (location == null)
            return;

        OrientVertex locationVertex = telaGraph.getVertex(graph,
                InstagramOrientDatabaseExtension.Media.Location.CLASS,
                InstagramOrientDatabaseExtension.Media.Location.ID, location.getId());
        if (locationVertex == null) {
            Map<String, Object> properties = new HashMap<>();
            properties.put(InstagramOrientDatabaseExtension.Media.Location.LATITUDE, location.getLatitude());
            properties.put(InstagramOrientDatabaseExtension.Media.Location.LONGITUDE, location.getLongitude());
            properties.put(InstagramOrientDatabaseExtension.Media.Location.ID, location.getId());
            if (location.getName() != null)
                properties.put(InstagramOrientDatabaseExtension.Media.Location.NAME, location.getName());
            if (location.getStreetAddress() != null)
                properties.put(InstagramOrientDatabaseExtension.Media.Location.ADDRESS, location.getStreetAddress());
            locationVertex = telaGraph.addVertex(
                    graph,
                    InstagramOrientDatabaseExtension.Media.Location.CLASS,
                    properties);
        }

        telaGraph.addEdge(graph, InstagramOrientDatabaseExtension.Media.Location.LOCATED_AT, mediaVertex, locationVertex);
    }

    private static void createTagsVertices(OrientGraphWrapper telaGraph, OrientBaseGraph graph, OrientVertex mediaVertex, List<TaggedUser> tags) {
        if (tags == null)
            return;
        for (TaggedUser tag : tags) {
            OrientVertex userVertex = OrientUserRepository.createOrUpdateUserVertex(telaGraph, graph, tag.getUser());
            Map<String, Object> properties = new HashMap<>();
            properties.put(InstagramOrientDatabaseExtension.Media.Tags.TAGS_X, tag.getPosition().getX());
            properties.put(InstagramOrientDatabaseExtension.Media.Tags.TAGS_Y, tag.getPosition().getY());
            telaGraph.addEdge(graph,
                    InstagramOrientDatabaseExtension.Media.Tags.TAGS,
                    mediaVertex, userVertex,
                    properties);
        }
    }

    private static void createResourcesVertices(OrientGraphWrapper telaGraph, OrientBaseGraph graph, OrientVertex mediaVertex, Iterable<MediaResource> resources, String resourceEdge) {
        for (MediaResource resource : resources) {
            Map<String, Object> properties = new HashMap<>();
            properties.put(InstagramOrientDatabaseExtension.Media.Resource.WIDTH, resource.getWidth());
            properties.put(InstagramOrientDatabaseExtension.Media.Resource.HEIGHT, resource.getHeight());
            properties.put(InstagramOrientDatabaseExtension.Media.Resource.CODE, resource.getCode());
            properties.put(InstagramOrientDatabaseExtension.Media.Resource.URL, resource.getUrl());
            OrientVertex resourceVertex = telaGraph.addVertex(
                    graph,
                    InstagramOrientDatabaseExtension.Media.Resource.CLASS,
                    properties);
            telaGraph.addEdge(graph,
                    resourceEdge,
                    mediaVertex, resourceVertex,
                    properties);
        }
    }

    private static void createResourcesVertices(OrientGraphWrapper telaGraph, OrientBaseGraph graph, OrientVertex mediaVertex, Iterable<MediaResource> images, Iterable<MediaResource> videos) {
        if (images != null) {
            createResourcesVertices(telaGraph, graph, mediaVertex, images, InstagramOrientDatabaseExtension.Media.Resource.IMAGE);
        }
        if (videos != null) {
            createResourcesVertices(telaGraph, graph, mediaVertex, videos, InstagramOrientDatabaseExtension.Media.Resource.VIDEO);
        }
    }


    private OrientVertex createMediaVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph, Media media) {

        // Basic media vertex
        OrientVertex mediaVertex = OrientGraphWrapperFactory.get().addVertex(
                graph,
                InstagramOrientDatabaseExtension.Media.CLASS,
                getMediaProperties(media));

        // User
        OrientVertex userVertex = OrientUserRepository.createOrUpdateUserVertex(telaGraph, graph, media.getUser());
        telaGraph.addEdge(graph, InstagramOrientDatabaseExtension.Media.POSTS, userVertex, mediaVertex);

        // Likes
        createLikesVertices(telaGraph, graph, mediaVertex, media.getLikes());

        // Tags
        createTagsVertices(telaGraph, graph, mediaVertex, media.getTaggedUsers());

        // Location
        createLocationVertex(telaGraph, graph, mediaVertex, media.getLocation());

        // Resources
        createResourcesVertices(telaGraph, graph, mediaVertex, media.getImages().values(), media.getVideos().values());

        // Comment
        commentRepository.setComments(media.getId(), media.getComments());

        // Caption
        if (media.getCaption() != null) {
            OrientVertex captionVertex = OrientCommentRepository.createOrUpdateCommentVertex(telaGraph, graph, media.getCaption());
            telaGraph.addEdge(graph, InstagramOrientDatabaseExtension.Media.CAPTION, captionVertex, mediaVertex);
        }

        return mediaVertex;
    }

    private OrientVertex createOrUpdateMediaVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph, Media media) {
        OrientVertex mediaVertex = findMediaVertex(telaGraph, graph, media.getId());
        if (mediaVertex != null) {
            telaGraph.removeVertices(graph,
                    InstagramOrientDatabaseExtension.Media.CLASS,
                    InstagramOrientDatabaseExtension.Media.ID, media.getId());
        }
        try {
            return createMediaVertex(telaGraph, graph, media);
        } catch (ORecordDuplicatedException e) {  // If other thread insert it in the meanwhile
            return findMediaVertex(telaGraph, graph, media.getId());
        }
    }


    //------------------------ OVERRIDE ------------------------//

    /**
     * {@inheritDoc}
     */
    @Override
    public void createOrUpdate(Media media) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        createOrUpdateMediaVertex(telaGraph, graph, media);
        graph.shutdown();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLikes(String mediaId, List<User> likes) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex vertex = findMediaVertex(telaGraph, graph, mediaId);
        if (vertex != null)
            createLikesVertices(telaGraph, graph, vertex, likes);
        graph.shutdown();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> findLikes(String mediaId) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex vertex = findMediaVertex(telaGraph, graph, mediaId);
        if (vertex == null)
            return new ArrayList<>();
        List<User> likes = mapLikesVertices(telaGraph, graph, vertex);
        graph.shutdown();
        return likes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Media find(String mediaId) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex vertex = findMediaVertex(telaGraph, graph, mediaId);
        io.reneses.tela.modules.instagram.models.Media media = (vertex == null) ? null : mapMediaVertex(telaGraph, graph, vertex);
        graph.shutdown();
        return media;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Media> findAll(long userId) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex userVertex = OrientUserRepository.findUserVertex(telaGraph, graph, userId);
        List<Media> output = new ArrayList<>();
        for (Vertex mediaVertex : userVertex.getVertices(Direction.OUT, InstagramOrientDatabaseExtension.Media.POSTS)) {
            output.add(mapMediaVertex(telaGraph, graph, mediaVertex));
        }
        graph.shutdown();
        return output;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Media> findLatest(long userId, int count) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        String command = String.format("SELECT EXPAND(OUT('%s')) FROM %s WHERE %s = ? ORDER BY %s DESC LIMIT %d",
                InstagramOrientDatabaseExtension.Media.POSTS,
                InstagramOrientDatabaseExtension.User.CLASS,
                InstagramOrientDatabaseExtension.User.ID,
                InstagramOrientDatabaseExtension.Media.CREATED_TIME,
                count);
        List<Media> output = new ArrayList<>();
        for (Object mediaVertex : telaGraph.executeAndFetch(graph, command, userId)) {
            output.add(mapMediaVertex(telaGraph, graph, (Vertex) mediaVertex));
        }
        graph.shutdown();
        return output;
    }

}
