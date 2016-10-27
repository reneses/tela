package io.reneses.tela.modules.instagram.repositories;

import com.orientechnologies.orient.core.storage.ORecordDuplicatedException;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapper;
import io.reneses.tela.modules.instagram.models.Comment;
import io.reneses.tela.modules.instagram.models.User;
import io.reneses.tela.modules.instagram.databases.extensions.InstagramOrientDatabaseExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OrientDB implementation of a CommentRepository
 */
public class OrientCommentRepository extends AbstractOrientRepository implements CommentRepository {


    //------------------------ AUX ------------------------//

    private static Map<String, Object> getCommentProperties(Comment comment) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(InstagramOrientDatabaseExtension.Comment.ID, comment.getId());
        properties.put(InstagramOrientDatabaseExtension.Comment.CREATED_TIME, comment.getCreatedTime());
        properties.put(InstagramOrientDatabaseExtension.Comment.TEXT, comment.getText());
        return properties;
    }


    private static OrientVertex findCommentVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph, long commentId) {
        return telaGraph.getVertex(
                graph,
                InstagramOrientDatabaseExtension.Comment.CLASS,
                InstagramOrientDatabaseExtension.Comment.ID, commentId);
    }

    static Comment mapCommentVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph,Vertex vertex, boolean mapUser) {

        Comment comment = new Comment();

        // Basic properties
        comment.setId(vertex.getProperty(InstagramOrientDatabaseExtension.Comment.ID));
        comment.setText(vertex.getProperty(InstagramOrientDatabaseExtension.Comment.TEXT));
        comment.setCreatedTime(vertex.getProperty(InstagramOrientDatabaseExtension.Comment.CREATED_TIME));

        // User
        if (mapUser) {
            Edge userEdge = vertex.getEdges(Direction.IN, InstagramOrientDatabaseExtension.Comment.COMMENTS).iterator().next();
            Vertex userVertex = userEdge.getVertex(Direction.OUT);
            User user = OrientUserRepository.mapUserVertex(telaGraph, graph, userVertex, false);
            comment.setUser(user);
        }

        return comment;
    }

    static OrientVertex createOrUpdateCommentVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph, Comment comment) {
        Map<String, Object> properties = getCommentProperties(comment);
        OrientVertex commentVertex = findCommentVertex(telaGraph, graph, comment.getId());
        if (commentVertex != null) {
            return commentVertex.setProperties(properties);
        }
        try {
            commentVertex = telaGraph.addVertex(
                    graph,
                    InstagramOrientDatabaseExtension.Comment.CLASS,
                    properties);
            OrientVertex userVertex = OrientUserRepository.createOrUpdateUserVertex(telaGraph, graph, comment.getUser());
            telaGraph.addEdge(graph, InstagramOrientDatabaseExtension.Comment.COMMENTS, userVertex, commentVertex);
            return commentVertex;
        } catch (ORecordDuplicatedException e) {  // If other thread insert it in the meanwhile
            return findCommentVertex(telaGraph, graph, comment.getId());
        }
    }


    //------------------------ OVERRIDE ------------------------//

    /** {@inheritDoc} */
    @Override
    public Comment find(long commentId) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex vertex = findCommentVertex(telaGraph, graph, commentId);
        Comment comment = (vertex == null) ? null : mapCommentVertex(telaGraph, graph, vertex, true);
        graph.shutdown();
        return comment;
    }

    /** {@inheritDoc} */
    @Override
    public List<Comment> findAll(String mediaId) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex mediaVertex = OrientMediaRepository.findMediaVertex(telaGraph, graph, mediaId);
        List<Comment> comments = new ArrayList<>();
        for (Edge edge : mediaVertex.getEdges(Direction.IN, InstagramOrientDatabaseExtension.Comment.COMMENTS)) {
            Vertex commentVertex = edge.getVertex(Direction.OUT);
            Comment comment = mapCommentVertex(telaGraph, graph, commentVertex, true);
            comments.add(comment);
        }
        graph.shutdown();
        return comments;
    }

    /** {@inheritDoc} */
    @Override
    public void createOrUpdate(Comment comment) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        createOrUpdateCommentVertex(telaGraph, graph, comment);
        graph.shutdown();
    }

    /** {@inheritDoc} */
    @Override
    public void setComments(String mediaId, Iterable<Comment> comments) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex mediaVertex = OrientMediaRepository.findMediaVertex(telaGraph, graph, mediaId);
        if (mediaVertex != null) {
            for (Comment comment : comments) {
                OrientVertex commentVertex = createOrUpdateCommentVertex(telaGraph, graph, comment);
                telaGraph.addEdge(graph, InstagramOrientDatabaseExtension.Comment.COMMENTS, commentVertex, mediaVertex);
            }
        }
        graph.shutdown();
    }

}
