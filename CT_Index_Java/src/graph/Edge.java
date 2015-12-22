package graph;

/**
 * Created by iva on 11/25/15.
 */
public class Edge{

    private SimpleNode dstNode;
    private String properties;

    public Edge(SimpleNode node2, String properties){
        this.dstNode = node2;
        this.properties = properties;
    }

    public SimpleNode dstNode(){
        return this.dstNode;
    }

    public String properties(){
        return this.properties;
    }

}
