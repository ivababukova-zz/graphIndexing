package graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by iva on 12/4/15.
 */
public class Graph {
    private HashMap<Integer, Node> nodesMap;
    private int id;

    public Graph(int id){
        this.nodesMap = new HashMap<>();
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

    public void put (Node n) {
        this.nodesMap.put(n.id,n);
    }

    public Node getNode(int id) {
        return this.nodesMap.get(id);
    }

}
