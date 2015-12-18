package graph;

import java.util.ArrayList;

/**
 * Created by iva on 12/18/15.
 */
public class IsomerNode{

    /**
     * This represents a label of an IsomerNode. The label is constructed
     * based on the labels of the neighbourhood of the Node whose IsomerNode
     * is being created. The label is the lexicographically smallest String of
     * label sequences of the neighbours of the Node. For example, if the input
     * node is labeled as C and is has edges.
     *
     * */
    private String label;
    private int id;
    private ArrayList<Edge> edges;

    /** constructor. Creates the isomer of the @param Node */
    public IsomerNode(Node node){
        this.label = node.property;
        this.id = node.id;
        this.edges = new ArrayList<>();
        //System.out.println("**** " + node.property);
        for (Edge e : node.edges) {
            Node dstNode = e.dstNode();
            addToLabel(dstNode.property);
            //System.out.println("current label: " + this.label);
        }
        System.out.println("Isomer node label is ready: " + node.id + ": " + this.label);
    }

    /** find the place of c in the current label and add it */
    private void addToLabel(String c){
        //System.out.println("to add: " + c);
        if (this.label == null) {
            this.label = c;
            return;
        }

        if (this.label.length() == 1) {
            if (this.label.compareTo(c) < 0) {
                this.label = this.label + c;
                return;
            }
            this.label = c + this.label;
            return;
        }

        for (int i = 0; i < this.label.length() - 1; i++) {
            String s = this.label.substring(i, i + 1);
            //System.out.println("s: " + s);
            if (c.compareTo(s) < 0) {
                this.label = this.label.substring(0,i) + c + this.label.substring(i);
                return;
            }
        }

        this.label = this.label + c;
    }

    public int edgeNumber() {
        return 0;
    }

    public void addEdge(Node node2, String edgeProp) {

    }

    public Edge getEdge(int index) {
        return null;
    }

    public ArrayList<Node> getOppositeNodes() {
        return null;
    }

    public String getEdgeProperty(Node dstNode) {
        return null;
    }

    public String label() {
        return this.label;
    }

    public int id() {
        return this.id;
    }
}
