package main;

import graph.Graph;
import graph.Node;
import tools.PathExtractor;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by iva on 12/4/15.
 */
public class BuildIndex {

    public static HashMap<String,Graph> graphs;
    public static HashMap<String,ArrayList<String>> graphIndices;
    public static String indexFileName;

    private static void parse(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        String[] splittedline = null;
        String graphID = "";
        while ((line = reader.readLine()) != null) {
            splittedline = line.split(" ");
            if(splittedline[0].equals("t")) {
                Graph graph = new Graph(splittedline[2]);
                graphs.put(splittedline[2],graph);
                graphID = splittedline[2];
            }
            if (splittedline[0].equals("v")) {
                String id = splittedline[1];
                String property = splittedline[2];
                Node n = new Node(id, property);
                graphs.get(graphID).put(n);
            }
            if (splittedline[0].equals("e")) {
                Node srcNode = graphs.get(graphID).getNode(splittedline[1]); // the first node id
                Node dstNode = graphs.get(graphID).getNode(splittedline[2]); // the second node id
                if (srcNode == null || dstNode == null) {
                    throw new Exception ("Can't create new edge, source " +
                            "or destination node does not exist");
                }
                srcNode.addEdge(dstNode,splittedline[3]); // the property id
                dstNode.addEdge(srcNode,splittedline[3]); // the property id
            }
        }
    }

    public static void printIndices(){
        Collection<Graph> graphsCollect = graphs.values();
        for (Graph g : graphsCollect) {
            System.out.println("****** index ****** " + g.getId());
            for (String path : graphIndices.get(g.getId())){
                System.out.println(path);
            }
        }
    }

    public static void writeToFile(Writer writer) throws IOException {
        Collection<Graph> graphsCollect = graphs.values();
        for (Graph g : graphsCollect) {
                writer.write("****** index ****** " + g.getId() + "\n");
            for (String path : graphIndices.get(g.getId())){
                writer.write(path + "\n");
            }
        }
    }

    public static void main(String[] args) throws Exception {

        if (args.length < 3) {
            System.out.println("Usage: <filename> <path lenght> <index file name>");
            System.exit(-1);
        }
        String filestr = args[0];
        int pathLen = new Integer(args[1]);
        File file = new File(filestr);
        indexFileName = args[2];
        graphs = new HashMap<>();
        graphIndices = new HashMap<>();
        parse(file);
        Collection<Graph> allGraphs = graphs.values();
        for (Graph graph : allGraphs) {
            Collection<Node> allNodes = graph.getAllNodes();
            PathExtractor extractor = new PathExtractor();

            // for size from 1 to pathLen, generate paths of the graph
            for (int i = 1; i <= pathLen; i++) {
                extractor.generatePath(allNodes, i);
                graphIndices.put(graph.getId(),extractor.getIndex());
            }
        }
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(indexFileName), "utf-8"))) {
            writeToFile(writer);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        //printIndices();
    }
}
