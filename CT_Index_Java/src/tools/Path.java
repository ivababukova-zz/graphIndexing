package tools;

import graph.SimpleNode;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by iva on 12/27/15.
 */
public class Path {

    //private SimpleNode[] nodes;
    private String edgeLabel;
    private ArrayList<SimpleNode> nodes;


    public Path(ArrayList<SimpleNode> nodes) {
        /*
        this.nodes = new SimpleNode[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            this.nodes[i] = nodes.get(i);
        }
        */
        this.nodes = nodes;
        this.edgeLabel = "-"; // default edge label
    }

    public Path(ArrayList<SimpleNode> nodes, String edgeLabel) {
        /*
        this.nodes = new SimpleNode[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            this.nodes[i] = nodes.get(i);
        }
        */
        this.nodes = nodes;
        this.edgeLabel = edgeLabel;
    }

    public int length(){
        return this.nodes.size();
    }

    public String toString(int labelOption){
        String path = "";
        for (int i = 0; i < nodes.size(); i++) {
            switch (labelOption) {
                case 0: {path = path + nodes.get(i).label(); break;}
                case 1: {path = path + nodes.get(i).isoLabel(); break;}
                case 2: {path = path + nodes.get(i).id(); break;}
            }
            if (i + 1 != nodes.size()){
                path = path + edgeLabel;
            }
        }
        return path;
    }

    public SimpleNode getNode(int i) {
        return this.nodes.get(i);
    }

    public String getNodeLabel(int i, int option) {
        switch (option) {
            case 0: {return this.nodes.get(i).label();}
            case 1: {return this.nodes.get(i).isoLabel();}
        }
        return "";
    }

    public void reverse(){
        Collections.reverse(nodes);
    }

    public void setNodes(ArrayList<SimpleNode> newNodes) {
        this.nodes = newNodes;
    }

    public ArrayList<SimpleNode> getNodes() {
        return this.nodes;
    }
}
