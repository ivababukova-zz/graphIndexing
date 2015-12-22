package graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by iva on 12/4/15.
 */
public class Graph {
    private HashMap<Integer, SimpleNode> nodesMap;
    private int id;

    public Graph(int id){
        this.nodesMap = new HashMap<>();
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public Collection<SimpleNode> getAllNodes(){
        return this.nodesMap.values();
    }

    public Iterator<Integer> getAllNodesIds(){
        return this.nodesMap.keySet().iterator();
    }

    public void putNode(SimpleNode n) {
        this.nodesMap.put(n.id(),n);
    }

    public SimpleNode getNode(int id) {
        return this.nodesMap.get(id);
    }

}
