package graph;

import java.util.ArrayList;

/**
 * Created by iva on 12/18/15.
 *
 * This is an interface that represents a node of a graph. Each node has label,
 * id and a list of edges. The size of the list of edges is equal to the degree
 * of the node.
 */
public interface INode {

    /* returns the number of edges this node has */
    int edgeNumber();

    /* creates a new Edge with dst Node=node2 and property=edgeProp */
    void addEdge(INode node2, String edgeProp);

    /* returns the edge that is stored in position=index in the list of edges */
    Edge getEdge(int index);

    /* returns the opposite nodes of all edges that this node has  */
    ArrayList<INode> getOppositeNodes();

    /* returns the property of the edge between this node and dstNode */
    String getEdgeProperty(INode dstNode);

    String getLabel();

}
