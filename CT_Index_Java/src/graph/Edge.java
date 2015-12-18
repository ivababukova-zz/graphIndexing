package graph;

/**
 * Created by iva on 11/25/15.
 */
public class Edge{

    private Node dstNode;
    private String properties;

    public Edge(Node node2, String properties){
        this.dstNode = node2;
        this.properties = properties;
    }

    public Node dstNode(){
        return this.dstNode;
    }

    public String properties(){
        return this.properties;
    }

}
