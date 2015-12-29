package tools;

import graph.SimpleNode;

import java.util.ArrayList;

/**
 * Created by iva on 12/27/15.
 */
public class Path {

    private SimpleNode[] nodes;
    private String edgeLabel;

    public Path(ArrayList<SimpleNode> nodes) {
        this.nodes = new SimpleNode[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            this.nodes[i] = nodes.get(i);
        }
        this.edgeLabel = "-"; // default edge label
    }

    public Path(ArrayList<SimpleNode> nodes, String edgeLabel) {
        this.nodes = new SimpleNode[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            this.nodes[i] = nodes.get(i);
        }
        this.edgeLabel = edgeLabel;
    }

    public int length(){
        return this.nodes.length;
    }

    public String toString(int labelOption){
        String path = "";
        for (int i = 0; i < nodes.length; i++) {
            switch (labelOption) {
                case 0: {path = path + nodes[i].label(); break;}
                case 1: {path = path + nodes[i].isoLabel(); break;}
                case 2: {path = path + nodes[i].id(); break;}
            }
            if (i + 1 != nodes.length){
                path = path + edgeLabel;
            }
        }
        return path;
    }

    public SimpleNode getNode(int i) {
        return this.nodes[i];
    }

    public String getNodeLabel(int i, int option) {
        switch (option) {
            case 0: {return this.nodes[i].label();}
            case 1: {return this.nodes[i].isoLabel();}
        }
        return "";
    }


}
