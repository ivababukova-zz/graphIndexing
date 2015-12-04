import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by iva on 11/25/15.
 */
public class Node {

    public String property;
    public String id;
    public ArrayList<Edge> edges; // edge1Property, node1id, edge2property, node2id ...
    public boolean visited;

    public Node(String id, String property){
        this.id = id;
        this.property = property;
        this.edges = new ArrayList<>();
        this.visited = false;
    }

    public int edgeNumber(){
        return this.edges.size();
    }

    public void addEdge(Node node2, String edgeProp) {
        Edge edge = new Edge(node2, edgeProp);
        this.edges.add(edge);
    }

    public Edge getEdge(int index){
        return this.edges.get(index);
    }

    public ArrayList<Node> getOppositeNode() {
        ArrayList<Node> opposites = new ArrayList<>();
        for (Edge e : this.edges) {
            System.out.println("edge: " + e.dstNode().id + " " + e.dstNode().property);
            Node child = e.dstNode();
            opposites.add(child);
        }
        return opposites;
    }

    public String getEdgeProperty(Node dstNode) {
        for (Edge e : this.edges) {
            if (e.dstNode() == dstNode) {
                return e.properties();
            }
        }
        return null;
    }
}
