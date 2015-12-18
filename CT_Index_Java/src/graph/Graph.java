package graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by iva on 12/4/15.
 */
public class Graph {
    private HashMap<Integer, Node> nodesMap;
    private HashMap<Integer, IsomerNode> isomerNodesMap;
    private int id;

    public Graph(int id){
        this.nodesMap = new HashMap<>();
        this.isomerNodesMap = new HashMap<>();
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public Collection<Node> getAllNodes(){
        return this.nodesMap.values();
    }

    public Iterator<Integer> getAllNodesIds(){
        return this.nodesMap.keySet().iterator();
    }

    public void putNode(Node n) {
        this.nodesMap.put(n.id,n);
    }

    public Node getNode(int id) {
        return this.nodesMap.get(id);
    }

    public void putINode(IsomerNode in) {
        this.isomerNodesMap.put(in.id(),in);
    }

    public IsomerNode getIsomerNode(int id) {
        return this.isomerNodesMap.get(id);
    }

    public Collection<IsomerNode> getAllINodes() {return this.isomerNodesMap.values();}

}
