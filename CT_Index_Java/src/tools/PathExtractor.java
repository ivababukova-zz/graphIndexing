package tools;

import graph.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

/**
 * Created by iva on 12/4/15.
 */
public class PathExtractor {

    private ArrayList<String> index = new ArrayList<>();
    private Stack<Integer> stack = new Stack<>();

    public ArrayList<String> getIndex(){
        return this.index;
    }

    /* putNode to the index the lexicographically smallest variant of the path,
     * unless the path is already in the index */
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
/*
    public void generatePath(Collection<Node> nodes, int len) {
        //System.out.println("in generatePath ...");

        for(Node n : nodes) {
           // System.out.println("- start node: " + n.property);
            stack = new Stack<>();
            stack.push(n.id);
            generatePathInner(n, len, n.property, 1, 0);
        }
    }

    private void generatePathInner(Node node,
                                          int maxLen,
                                          String partialPath,
                                          int currentLen, int recursion){
        String rec = "";
        for (int i = 0; i < recursion; i++){
            rec = rec + "-";
        }
       // System.out.println(rec + " curr len: " + currentLen + " max len: " + maxLen + " curr node: " + node.id);
        if (currentLen == maxLen) {
            putToIndex(partialPath);
          //  System.out.println(rec + " partial path: " + partialPath);
            stack.pop();
            return;
        }
        ArrayList<Node> children = node.getOppositeNodes();
        for (Node child : children) { // 1, 2, 3
            //System.out.print(rec + " child: " + child.id + " " + child.property);
            if (stack.search(child.id) == -1) {
              //  System.out.println(" NOT visited");
                stack.push(child.id);
                String partialPath2 = partialPath + node.getEdgeProperty(child) + child.property;
                int recursion2 = recursion + 2;
                generatePathInner(child, maxLen, partialPath2, currentLen + 1, recursion2);
            }
            else{
               // System.out.println(" visited");
            }
            if (stack.size() > 1) {
                stack.pop();
            }
        }
    }
    */
}
