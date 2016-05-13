package tools;

import graph.Vertex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;


/**
 * Created by iva on 12/4/15.
 */
public class PathExtractor {

    private String          edgeLabel;
    private int             option;
    private ArrayList<Path> index = new ArrayList<>();
    private Stack<Vertex>   stackPath = new Stack<>(); // used both for the path search and for checking visited nodes


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

    public void clearIndex(){
        this.index = new ArrayList<>();
    }

    /** putNode to the index the lexicographically smallest variant of the path,
     * unless the path is already in the index
     * make the path to String before adding it to the index */
    private void putToIndex(ArrayList<Vertex> stackArr) {

        Path path = new Path(new ArrayList<>(stackArr), edgeLabel);
        Path reversedPath = new Path(new ArrayList<>(stackArr),edgeLabel);
        reversedPath.reverse();

        String reversedStr = reversedPath.toString(option);
        String pathStr = path.toString(option);

        //System.out.println("*!* ");
        //System.out.println( " " + pathStr.compareTo(reversedStr));

        if (pathStr.compareTo(reversedStr) > 0) {
            path.setNodes(reversedPath.getNodes());
            pathStr = path.toString(option);
        }
        boolean found = false;
        for (Path p : index) {
            if (p.toString(option).equals(pathStr)) {
                found = true;
                break;
            }
        }
        if (!found) {
            //System.out.print(path.toString(option) + "\n");
            index.add(path);
        }
    }

    public void generatePath(Collection<Vertex> vertex, int len) {
        for(Vertex n : vertex) { // n is the current start node
            stackPath = new Stack<>();
            stackPath.push(n);
            generatePathInner(n, len, 0);
            for (Vertex v1 : vertex) v1.unvisit();
        }
    }

    private void generatePathInner(Vertex node, int maxL, int recursion) {
        node.visit();
        //System.out.println("func call");
        if (stackPath.size() <= maxL) {
            putToIndex(new ArrayList<>(stackPath));
            if (stackPath.size() == maxL) {
                stackPath.pop();
                return;
            }
        }
        ArrayList<Vertex> children = node.getNeighbors();
        boolean allChildrenVisited = true;
        for (Vertex child : children) {
            if (stackPath.search(child) == -1) {
                stackPath.push(child);
                allChildrenVisited = true;
                generatePathInner(child,maxL,0);
            }
        }
        if (allChildrenVisited && stackPath.size() > 1) {
            stackPath.pop();
        }
        if (stackPath.size() == 0) return;
    }

/*
    private void generatePathInner(Vertex node,
                                          int maxLen,
                                          int recursion){
        node.visit();
        // rec is only for testing purposes
        String rec = "";
        for (int i = 0; i < recursion; i++){
            rec = rec + "-";
        }
        //System.out.println(rec + " curr L: " + stackPath.size() + " max L: " + maxLen + " curr node: " + node.id());
        if (stackPath.size() == maxLen) {
            putToIndex(new ArrayList(stackPath));
            stackPath.pop();
            return;
        }
        ArrayList<Vertex> children = node.getNeighbors();
        for (Vertex child : children) { // 1, 2, 3
            //System.out.print(rec + " child: " + child.id() + " " + child.label());
            if (stackPath.search(child) == -1 ) {
                //System.out.println(" NOT visited");
                stackPath.push(child);
                int recursion2 = recursion + 2;
                generatePathInner(child, maxLen, recursion2);
            }

            else{
               // System.out.println(" visited");
                child.visit();
            }

            // remove lastOnStack from the stackPath only after we are sure that
            // all children of lastOnStack are explored
            if (stackPath.size() > 1) {
                boolean removeFromStack = true;
                Vertex lastOnStack = stackPath.peek();
               // System.out.println(rec + "* lastOnStack: " + lastOnStack.id());
                for (Vertex lsChild : lastOnStack.getNeighbors()) {
                    //System.out.println(rec + "** lsChild: " + lsChild.id() + " isVisited? " + lsChild.isVisited());
                    if (!lsChild.isVisited() && (lsChild.id() != child.id())){
                        removeFromStack = false;
                    }
                }

                if (removeFromStack) {
                   // System.out.println(rec + " ALL visited, popping out from the stack: ");
                    stackPath.pop();
                    for (Vertex lsChild : lastOnStack.getNeighbors()) {
                        if (stackPath.search(lsChild) == -1) {
                            lsChild.unvisit();
                        }
                    }
                    return;
                }
            }
        }
    }
*/
    /** methods needed for the stack-path */

    /*
     * if option == 0: make string using the labels of the nodes
     * if option == 1: make string using the isolabels of the nodes
     * if option == 2: make string using the ids of the nodes --- testing purposes
     * */
    /*
    public String toString(ArrayList<Vertex> nodes){
        String path = "";
        for (Vertex n : nodes) {
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
*/
    /* reverse a given arraylist specified as parameter */
    /*public void reverse(ArrayList<Vertex> nodes){
        Collections.reverse(nodes);
    }*/
}
