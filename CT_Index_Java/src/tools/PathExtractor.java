package tools;

import graph.SimpleNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;


/**
 * Created by iva on 12/4/15.
 */
public class PathExtractor {

    private String edgeLabel;

    private int option;

    private ArrayList<Path> index = new ArrayList<>();

    /** this stack is used both for the path search and for checking visited nodes */
    private Stack<SimpleNode> stackPath = new Stack<>();

    /** this keeps a record of previously explored paths so that we don't explore
     * the same path over and over again*/
    private Stack<SimpleNode> globalStack = new Stack<>();

    public ArrayList<Path> getIndex(){
        return this.index;
    }

    public PathExtractor(String edgeLabel, int option) {
        this.option = option;
        this.edgeLabel = edgeLabel;
    }

    public PathExtractor(int option) {
        this.option = option;
        this.edgeLabel = "-";
    }

    /** putNode to the index the lexicographically smallest variant of the path,
     * unless the path is already in the index
     * make the path to String before adding it to the index */
    private void putToIndex(ArrayList<SimpleNode> stackArr) {

        ArrayList<SimpleNode> arr = stackArr;
        ArrayList<SimpleNode> reversedArr = stackArr;
        reverse(reversedArr);
        String reversedStr = toString(reversedArr);
        String pathStr = toString(arr);


        //System.out.println("*** partial path: " + pathStr);
        if (reversedStr.compareTo(pathStr) < 0) {
            pathStr = reversedStr;
            arr = reversedArr;
        }
        boolean found = false;
        for (Path p : index) {
            String str = p.toString(option);
            if (str.equals(pathStr)) {
                found = true;
                break;
            }
        }
        if (!found) {
            Path path = new Path(arr);
            index.add(path);
        }
    }

    public void generatePath(Collection<SimpleNode> nodes, int len) {
      //  System.out.println("in generatePath ...");
        globalStack = new Stack<>();
        for(SimpleNode n : nodes) {
          //  System.out.println("- start node: " + n.id());
            stackPath = new Stack<>();
            stackPath.push(n);
            globalStack.push(n);
            generatePathInner(n, len, 0);
            for (SimpleNode n1 : nodes) n1.unvisit();
        }
    }


    private void generatePathInner(SimpleNode node,
                                          int maxLen,
                                          int recursion){
        node.visit();
        /** rec is only for testing purposes */
        String rec = "";
        for (int i = 0; i < recursion; i++){
            rec = rec + "-";
        }
       // System.out.println(rec + " curr L: " + stackPath.size() + " max L: " + maxLen + " curr node: " + node.id());
        if (stackPath.size() == maxLen) {
            ArrayList<SimpleNode> arr = new ArrayList(stackPath);
            putToIndex(arr);
            stackPath.pop();
            return;
        }
        ArrayList<SimpleNode> children = node.getOppositeNodes();
        for (SimpleNode child : children) { // 1, 2, 3
          // System.out.print(rec + " child: " + child.id() + " " + child.label());
            if (stackPath.search(child) == -1 && globalStack.search(child) == -1) {
               // System.out.println(" NOT visited");
                stackPath.push(child);
                int recursion2 = recursion + 2;
                generatePathInner(child, maxLen, recursion2);
            }
            else{
             //   System.out.println(" visited");
                child.visit();
            }
            // remove lastOnStack from the stackPath only after we are sure that
            // all children of lastOnStack are explored
            if (stackPath.size() > 1) {
                boolean removeFromStack = true;
                SimpleNode lastOnStack = stackPath.peek();
                //System.out.println(rec + "* lastOnStack: " + lastOnStack.id());
                for (SimpleNode lsChild : lastOnStack.getOppositeNodes()) {
                    //System.out.println(rec + "** lsChild: " + lsChild.id() + " isVisited? " + lsChild.isVisited());
                    if (!lsChild.isVisited() && (lsChild.id() != child.id())){
                        removeFromStack = false;
                    }
                }
                if (removeFromStack) {
                    //System.out.println(rec + " ALL visited, popping out from the stackPath");
                    stackPath.pop();
                    for (SimpleNode lsChild : lastOnStack.getOppositeNodes()) {
                        lsChild.unvisit();
                    }
                }
            }
        }
    }

    /** methods needed for the stack-path */

    /*
     * if option == 0: make string using the labels of the nodes
     * if option == 1: make string using the isolabels of the nodes
     * if option == 2: make string using the ids of the nodes --- testing purposes
     * */
    public String toString(ArrayList<SimpleNode> nodes){
        String path = "";
        for (SimpleNode n : nodes) {
            switch (this.option) {
                case 0: {path = path + n.label(); break;}
                case 1: {path = path + n.isoLabel(); break;}
                case 2: {path = path + n.id(); break;}
            }
            if (nodes.indexOf(n) != (nodes.size()-1)) {
                path = path + edgeLabel;
            }
        }
        return path;
    }

    /* reverse a given arraylist specified as parameter */
    public void reverse(ArrayList<SimpleNode> nodes){
        Collections.reverse(nodes);
    }
}
