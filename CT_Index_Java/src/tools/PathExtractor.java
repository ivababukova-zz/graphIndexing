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
        this.option     = option;
        this.edgeLabel  = edgeLabel;
    }

    public PathExtractor(int option) {
        this.option     = option;
        this.edgeLabel  = "-";
    }

    public void clearIndex(){
        this.index = new ArrayList<>();
    }

    /** putNode to the index the lexicographically smallest variant of the path,
     * unless the path is already in the index
     * make the path to String before adding it to the index */
    private void putToIndex(ArrayList<Vertex> stackArr) {

        Path path           = new Path(new ArrayList<>(stackArr), edgeLabel);
        Path reversedPath   = new Path(new ArrayList<>(stackArr),edgeLabel);
        reversedPath.reverse();
        String reversedStr  = reversedPath.toString(option);
        String pathStr      = path.toString(option);

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
        if (!found) index.add(path);
    }

    public void generatePath(Collection<Vertex> vertex, int len) {
        for(Vertex v : vertex) { // v is the current start vertex
            stackPath = new Stack<>();
            stackPath.push(v);
            generatePathInner(v, len);
            for (Vertex v1 : vertex) v1.unvisit();
        }
    }

    private void generatePathInner(Vertex node, int maxL) {
        node.visit();
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
                generatePathInner(child,maxL);
            }
        }
        if (allChildrenVisited && stackPath.size() > 1) {
            stackPath.pop();
        }
        if (stackPath.size() == 0) return;
    }
}
