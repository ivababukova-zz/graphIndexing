package graph;

/**
 * Created by iva on 11/25/15.
 */
public class Edge{

    private Vertex dstNode;
    private String properties;

    public Edge(Vertex node2, String properties){
        this.dstNode = node2;
        this.properties = properties;
    }

    public Vertex dstNode(){
        return this.dstNode;
    }

    public String properties(){
        return this.properties;
    }

}
