package deprecated;

import graph.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by iva on 11/25/15.
 */
public class OldGraph {
    public static HashMap<Integer, Node> nodesMap;
    public static ArrayList<String> index;
    public static HashMap<Node, Boolean> inPath = new HashMap<>();

    private static void putToIndex(String path){
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

    public static void generatePath(Collection<Node> nodes,
                                    int len) {
        for (Node n : nodes) {
            inPath.put(n, false);
        }
        for(Node n : nodes) {
            System.out.println("- current node: " + n.property + " degree: " + n.edgeNumber());
            resetInPath();
            inPath.put(n,true);
            generatePathInner(n, len, n.property, 1);
        }
    }

    private static void generatePathInner(Node node,
                                          int maxLen,
                                          String partialPath,
                                          int currentLen){
        System.out.println("-- curr len: " + currentLen + " maxlen: " + maxLen + " curr node: " + node.id + " " + node.property);
        if (currentLen == maxLen) {
            putToIndex(partialPath);
            System.out.println("--* " + partialPath);
            return;
        }
        ArrayList<Node> children = node.getOppositeNode();
        for (Node child : children) {
            System.out.println("--+ " + child.id + " " + child.property);
            if (inPath.get(child) == false) {
                inPath.put(child,true);
                String partialPath2 = partialPath + node.getEdgeProperty(child) + child.property;
                generatePathInner(child,maxLen,partialPath2,currentLen + 1);
            }
        }
    }

    private static void resetInPath(){
        inPath.keySet().forEach( node1 -> {
            inPath.put(node1,false);
        });
    }

    private static void parse(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        String[] splittedline = null;
        while ((line = reader.readLine()) != null) {
            splittedline = line.split(" ");
            if (splittedline[0].equals("v")) {
                int id = Integer.parseInt(splittedline[1]);
                String property = splittedline[2];
                Node n = new Node(id, property);
                nodesMap.put(id,n);
            }
            if (splittedline[0].equals("e")) {
                Node srcNode = nodesMap.get(splittedline[1]); // the first node id
                Node dstNode = nodesMap.get(splittedline[2]); // the second node id
                if (srcNode == null || dstNode == null) {
                    throw new Exception ("Can't create new edge, source " +
                                         "or destination node does not exist");
                }
                srcNode.addEdge(dstNode,splittedline[3]); // the property id
                dstNode.addEdge(srcNode,splittedline[3]); // the property id
            }
        }
    }

    public static void main(String[] args) throws Exception {
        index = new ArrayList<>();
        nodesMap = new HashMap<>();
        if (args.length < 2) {
            System.out.println("Usage: <filename> <path lenght>");
            System.exit(-1);
        }
        String filestr = args[0];
        int pathLen = new Integer(args[1]);
        File file = new File(filestr);
        parse(file);
        Collection<Node> allNodes = nodesMap.values();
        for (Node n : allNodes) {
            System.out.println("start node: " + n.id + " " + n.property + " degree: " + n.edgeNumber());
        }

        // for size from 1 to pathLen, generate paths of the graph
        for (int i = 1; i <= pathLen; i++) {
            System.out.println("* in tools *");
            generatePath(allNodes, i);
        }
        System.out.println("****** index ******");
        for (String path : index) {
            System.out.println(path);
        }
    }
}