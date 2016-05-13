package graph;

import java.util.ArrayList;

/**
 * Created by iva on 11/25/15.
 */
public class Vertex {

    private String          label;
    private String          nlabel;
    private int             id;
    private ArrayList<Edge> edges; // all edges a Vertex has. Size of edges is the degree of the vertex
    private boolean         visited;

    /** this property is needed at PathExtractor class. We need to know when to
     * remove a node from the stack. We remove a node from the stack when we know
     * that all children it has are already visited. After popping out from the stack,
     * we need to unset these children to unvisited. */

    public Vertex(int id, String property) {
        this.id         = id;               // id
        this.label      = property;         // label
        this.edges      = new ArrayList<>();
        this.nlabel     = new String();     // neighbourhood label
        this.visited    = false;
    }

    public void visit(){
        this.visited = true;
    }

    public void unvisit(){
        this.visited = false;
    }

    /* sets the neighbour label of this vertex */
    public void setNlabel() {
        this.nlabel = "";
        for (Edge e : this.edges) {
            Vertex dstNode = e.dstNode();
            addToLabel(dstNode.label());
        }
        this.nlabel = this.label + this.nlabel;
    }

    /** insert c in the current nlabel so that it is in lexicographically increasing order */
    private void addToLabel(String c){
        if (this.nlabel.length() == 0) {
            this.nlabel += c;
            return;
        }

        for (int i = 0; i < this.nlabel.length(); i++) {
            String s = this.nlabel.substring(i, i + 1);
            if (c.compareTo(s) < 0) {
                this.nlabel = this.nlabel.substring(0,i) + c + this.nlabel.substring(i);
                return;
            }
        }
        this.nlabel = this.nlabel + c;
    }

    public void addEdge(Vertex node2, String elabel) {
        Edge edge = new Edge(node2, elabel);
        this.edges.add(edge);
    }

    /* returns the vertices that are connected via an edge to this vertex */
    public ArrayList<Vertex> getNeighbors() {
        ArrayList<Vertex> opposites = new ArrayList<>();
        for (Edge e : this.edges) {
            Vertex child = e.dstNode();
            opposites.add(child);
        }
        return opposites;
    }

    public int id(){
        return this.id;
    }

    public String label(){
        return this.label;
    }

    public String isoLabel(){
        return this.nlabel;
    }

    public int degree(){ return this.edges.size(); }
}
