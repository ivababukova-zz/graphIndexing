package tools;

import graph.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by iva on 12/4/15.
 */
public class PathExtractor {

    private ArrayList<String> index = new ArrayList<>();
    private HashMap<Node, Boolean> inPath = new HashMap<>();

    public ArrayList<String> getIndex(){
        return this.index;
    }

    private void putToIndex(String path) {
        String reversed = new StringBuilder(path).reverse().toString();
        if (reversed.compareTo(path) < 0) {
            path = reversed;
        }
        boolean found = false;
        for (String str : index) {
            if (str.equals(path)) {
                found = true;
                break;
            }
        }
        if (!found) {
            index.add(path);
        }
    }

    public void generatePath(Collection<Node> nodes, int len) {
        for (Node n : nodes) {
            inPath.put(n, false);
        }
        for(Node n : nodes) {
            //System.out.println("- current node: " + n.property + " degree: " + n.edgeNumber());
            resetInPath();
            inPath.put(n,true);
            generatePathInner(n, len, n.property, 1);
        }
    }

    private void generatePathInner(Node node,
                                          int maxLen,
                                          String partialPath,
                                          int currentLen){
        //System.out.println("-- curr len: " + currentLen + " maxlen: " + maxLen + " curr node: " + node.id + " " + node.property);
        if (currentLen == maxLen) {
            putToIndex(partialPath);
            //System.out.println("--* " + partialPath);
            return;
        }
        ArrayList<Node> children = node.getOppositeNode();
        for (Node child : children) {
            //System.out.println("--+ " + child.id + " " + child.property);
            if (inPath.get(child) == false) {
                inPath.put(child,true);
                String partialPath2 = partialPath + node.getEdgeProperty(child) + child.property;
                generatePathInner(child,maxLen,partialPath2,currentLen + 1);
            }
        }
    }

    private void resetInPath(){
        inPath.keySet().forEach( node1 -> {
            inPath.put(node1,false);
        });
    }


}
