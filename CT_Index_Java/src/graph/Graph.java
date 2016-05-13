package graph;

import java.io.IOException;
import java.util.*;

/**
 * Created by iva on 12/4/15.
 */
public class Graph {

    private ArrayList<Vertex>   vertices;   // hashmap containing each vertex and its id
    private String              id;         // the graph id
    private int                 order;      // number of vertices
    private int                 size;       // number of edges
    private int[]               degree;     // degree of vertices


    public Graph(Scanner sc) throws IOException{
        this.vertices   = new ArrayList<>();
        this.id         = sc.next();
        this.order      = sc.nextInt();
        this.degree     = new int[order];

        for(int i = 0; i < order; i++) {
            Vertex v = new Vertex(i,sc.next());
            vertices.add(v);
        }

        this.size = sc.nextInt();
        for (int i = 0; i < size; i++){
            Vertex u = vertices.get(sc.nextInt());
            Vertex v = vertices.get(sc.nextInt());
            u.addEdge(v,""); // to support edge labels, change "" with the corresponding edge label
            v.addEdge(u,"");
        }
    }

    public String getId(){
        return this.id;
    }
    public ArrayList<Vertex> getAllVertices(){
        return this.vertices;
    }


}
