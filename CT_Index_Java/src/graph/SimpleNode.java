package graph;

import java.util.ArrayList;

/**
 * Created by iva on 11/25/15.
 */
public class SimpleNode {

    private String label;
    private String isoLabel;
    private int id;
    private ArrayList<Edge> edges; // edge1Property, node1id, edge2property, node2id ...

    /** this property is needed at PathExtractor class. We need to know when to
     * remove a node from the stack. We remove a node from the stack when we know
     * that all children it has are already visited. After popping out from the stack,
     * we need to unset these children to unvisited. */
    private boolean visited;


    public SimpleNode(int id, String property) {
        this.id = id;
        this.label = property;
        this.edges = new ArrayList<>();
        this.isoLabel = new String();
        this.visited = false;
    }

    public void visit(){
        this.visited = true;
    }

    public void unvisit(){
        this.visited = false;
    }

    public boolean isVisited(){
        return this.visited;
    }

    public void setIsoLabel() {
        for (Edge e : this.edges) {
            SimpleNode dstNode = e.dstNode();
            addToLabel(dstNode.label());
        }
        this.isoLabel = this.label + this.isoLabel;
    }

    /** find the place of c in the current label and add it */
    private void addToLabel(String c){
        System.out.println("to add: " + c);
        if (this.isoLabel.length() == 0) {
            this.isoLabel += c;
            return;
        }

        if (this.isoLabel.length() == 1) {
            if (this.isoLabel.compareTo(c) < 0) {
                this.isoLabel = this.isoLabel + c;
                return;
            }
            this.isoLabel = c + this.isoLabel;
            return;
        }

        for (int i = 0; i < this.isoLabel.length(); i++) {
            String s = this.isoLabel.substring(i, i + 1);
            if (c.compareTo(s) < 0) {
                this.isoLabel = this.isoLabel.substring(0,i) + c + this.isoLabel.substring(i);
                return;
            }
        }
        this.isoLabel = this.isoLabel + c;
    }

    public int edgeNumber(){
        return this.edges.size();
    }

    public void addEdge(SimpleNode node2, String edgeProp) {
        Edge edge = new Edge(node2, edgeProp);
        this.edges.add(edge);
    }

    public Edge getEdge(int index){
        return this.edges.get(index);
    }

    public ArrayList<SimpleNode> getOppositeNodes() {
        ArrayList<SimpleNode> opposites = new ArrayList<>();
        for (Edge e : this.edges) {
            SimpleNode child = e.dstNode();
            opposites.add(child);
        }
        return opposites;
    }

    public String getEdgeProperty(SimpleNode dstNode) {
        for (Edge e : this.edges) {
            if (e.dstNode() == dstNode) {
                return e.properties();
            }
        }
        return null;
    }

    public int id(){
        return this.id;
    }

    public String label(){
        return this.label;
    }

    public String isoLabel(){
        return this.isoLabel;
    }

    public ArrayList<Edge> edges(){
        return this.edges;
    }

}
