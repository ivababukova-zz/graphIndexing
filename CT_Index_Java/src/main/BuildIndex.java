package main;

import graph.Graph;
import graph.IsomerNode;
import graph.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by iva on 12/4/15.
 */
public class BuildIndex {

    public static HashMap<Integer,Graph> graphs;
    public static HashMap<Integer,Graph> patterns;
    public static HashMap<Integer,ArrayList<String>> graphIndices;
    public static ArrayList<String> patternsIndices;
    public static String indexFileName;
    public static String patternFileName;

    private static void parse(File file, boolean isPattern) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        String[] splittedline = null;
        int graphID = 0;
        while ((line = reader.readLine()) != null) {
            splittedline = line.split(" ");
            if(splittedline[0].equals("t")) {
                graphID = Integer.parseInt(splittedline[2]);
                if (!isPattern && graphID !=0) {
                    createGraphIsomer(graphs.get(graphID - 1));
                }
                Graph graph = new Graph(graphID);
                if (isPattern) {
                    patterns.put(graphID, graph);
                }
                else {
                    graphs.put(graphID, graph);
                }
            }
            if (splittedline[0].equals("v")) {
                int id = Integer.parseInt(splittedline[1]);
                String property = splittedline[2];
                Node n = new Node(id, property);
                if (isPattern) {
                    patterns.get(graphID).putNode(n);
                }
                else {
                    graphs.get(graphID).putNode(n);
                }
            }
            if (splittedline[0].equals("e")) {
                Node srcNode;
                Node dstNode;
                if (isPattern) {
                    srcNode = patterns.get(graphID).getNode(Integer.parseInt(splittedline[1]));
                    dstNode = patterns.get(graphID).getNode(Integer.parseInt(splittedline[2]));
                }
                else {
                    srcNode = graphs.get(graphID).getNode(Integer.parseInt(splittedline[1])); // the first node id
                    dstNode = graphs.get(graphID).getNode(Integer.parseInt(splittedline[2])); // the second node id
                }
                if (srcNode == null || dstNode == null) {
                    throw new Exception("Can't create new edge, source " +
                                "or destination node does not exist");
                }
                srcNode.addEdge(dstNode, splittedline[3]); // the property id
                dstNode.addEdge(srcNode, splittedline[3]); // the property id
            }
        }
        createGraphIsomer(graphs.get(graphID));
    }

    private static void createGraphIsomer(Graph g){
        for (Node n : g.getAllNodes()) {
            IsomerNode in = new IsomerNode(n);
            g.putINode(in);
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
/*
    public static void createIndexTarget(int pathLen){
        Collection<Graph> allGraphs = graphs.values();
        PathExtractor extractor = new PathExtractor();
        for (Graph graph : allGraphs) {
            Collection<Node> allNodes = graph.getAllNodes();
            // for size from 1 to pathLen, generate paths of the graph
            for (int i = 1; i <= pathLen; i++) {
                //System.out.println("new pathLen: " + i);
                extractor.generatePath(allNodes, i);
                graphIndices.putNode(graph.getId(),extractor.getIndex());
            }
        }
    }

    public static void createIndexPatterns(int pathLen){
        Collection<Graph> allGraphs = patterns.values();
        PathExtractor extractor = new PathExtractor();
        // System.out.println("*** index of patterns ***");
        for (Graph graph : allGraphs) {
            Collection<Node> allNodes = graph.getAllNodes();
            // for size from 1 to pathLen, generate paths of the graph
            for (int i = 1; i <= pathLen; i++) {
                extractor.generatePath(allNodes, i);
                for (String path : extractor.getIndex()) {
                    putToPatternIndex(path);
                }
            }
        }
    }
*/
    public static void putToPatternIndex(String pattern) {
        if (patternsIndices.contains(pattern)) return;
        else{
            System.out.println(pattern);
            patternsIndices.add(pattern);
        }
    }

    public static void main(String[] args) throws Exception {

        if (args.length < 4) {
            System.out.println("Usage: <filename> <path length> <index file name> <patterns file name>");
            System.exit(-1);
        }
        String filestr = args[0];
        int pathLen = new Integer(args[1]);
        File file = new File(filestr);
        indexFileName = args[2];
        patternFileName = args[3];
        graphs = new HashMap<>();
        graphIndices = new HashMap<>();
        patterns = new HashMap<>();
        patternsIndices = new ArrayList<>();

        parse(file,false);
        System.out.println("Number of targets: " + graphs.size());
/*
        createIndexTarget(pathLen);
        //printIndices();

        // write the extracted paths to the specified index filename
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(indexFileName), "utf-8"))) {
            writeToFile(writer);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }

        // create a patternGraph
        file = new File(patternFileName);
        parse(file,true); // parse the patterns
        createIndexPatterns(pathLen);

        CandidatesExtractor candidatesExtractor = new CandidatesExtractor();

        long l1 = System.currentTimeMillis();
        graphIndices.keySet().forEach(
                graphId -> {
                    if (candidatesExtractor.isCandidate(graphIndices.get(graphId), patternsIndices)){
                        //System.out.println("IT IS A CANDIDATE");
                        candidatesExtractor.addCandidate(graphId);
                    }
                }
        );
        long l2 = System.currentTimeMillis() - l1;
        System.out.println("done");
        System.out.println();
        System.out.println("Index Build Time [s]: \t" + (l2 / 1000.0));
        System.out.println("Printing candidates ...");
        long l3 = System.currentTimeMillis();

        // putNode the candidate graph ids in a file:
        File outfile = new File("candidateIDs-AIDS-patterns.txt");
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outfile), "utf-8"))) {
            for (Integer in : candidatesExtractor.getCandidates()) {
                writer.write(in + "\n");
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }

        long l4 = System.currentTimeMillis() - l3;
        System.out.println("done");
        System.out.println();
        System.out.println("Computing CandidatesTime [s]: \t" + (l4/1000.0));
        System.out.println("Number of candidates: " + candidatesExtractor.getCandidates().size());


        */
    }
}
