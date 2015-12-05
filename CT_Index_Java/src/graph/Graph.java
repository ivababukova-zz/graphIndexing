package graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by iva on 12/4/15.
 */
public class Graph {
    private HashMap<String, Node> nodesMap;
    private String id;

    public Graph(String id){
        this.nodesMap = new HashMap<>();
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public Collection<Node> getAllNodes(){
        return this.nodesMap.values();
    }

    public Iterator<String> getAllNodesIds(){
        return this.nodesMap.keySet().iterator();
    }

    public void put (Node n) {
        this.nodesMap.put(n.id,n);
    }

    public Node getNode(String id) {
        return this.nodesMap.get(id);
    }

}
