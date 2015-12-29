package main;

import graph.Graph;
import graph.SimpleNode;
import tools.*;

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
    public static HashMap<Integer,ArrayList<Path>> graphIndices;
    public static ArrayList<Path> patternsIndices;
    public static String indexFileName;
    public static String patternFileName;
    public static int labelOption;

    private static void parse(File file, boolean isPattern) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        String[] splittedline = null;
        int graphID = 0;
        while ((line = reader.readLine()) != null) {
            splittedline = line.split(" ");
            if(splittedline[0].equals("t")) {
                graphID = Integer.parseInt(splittedline[2]);
                if (!isPattern && graphID !=0 && labelOption == 1) {
                    createIsoLabels(graphs.get(graphID - 1));
                }
                if (isPattern && graphID !=0 && labelOption == 1) {
                    createIsoLabels(patterns.get(graphID - 1));
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
                SimpleNode n = new SimpleNode(id, property);
                if (isPattern) {
                    patterns.get(graphID).putNode(n);
                }
                else {
                    graphs.get(graphID).putNode(n);
                }
            }
            if (splittedline[0].equals("e")) {
                SimpleNode srcNode;
                SimpleNode dstNode;
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
        if(!isPattern && reader.readLine() == null && labelOption == 1) {
            createIsoLabels(graphs.get(graphID));
        }
        if (isPattern && reader.readLine() == null && labelOption == 1) {
            createIsoLabels(patterns.get(graphID));
        }
    }

    /** whenever I am sure that the graph is constructed, create the isomer label
     * for each node */
    private static void createIsoLabels(Graph g){
        for (SimpleNode n : g.getAllNodes()) {
            n.setIsoLabel();
        }
    }

    public static void printIndices(){
        Collection<Graph> graphsCollect = graphs.values();
        for (Graph g : graphsCollect) {
            System.out.println("****** index ****** " + g.getId());
            for (Path path : graphIndices.get(g.getId())){
                System.out.println(path.toString(labelOption));
            }
        }
    }

    public static void writeToFile(Writer writer) throws IOException {
        Collection<Graph> graphsCollect = graphs.values();
        for (Graph g : graphsCollect) {
                System.out.println(g.getId());
                writer.write("t # " + g.getId() + "\n");
            for (Path path : graphIndices.get(g.getId())){
                writer.write(path.toString(labelOption) + "\n");
            }
        }
    }

    public static void createIndexTarget(int pathLen){
        Collection<Graph> allGraphs = graphs.values();
        PathExtractor extractor = new PathExtractor(labelOption);
        for (Graph graph : allGraphs) {
            Collection<SimpleNode> allNodes = graph.getAllNodes();
            // for size from 1 to pathLen, generate paths of the graph
            for (int i = 1; i <= pathLen; i++) {
                extractor.generatePath(allNodes, i);
                graphIndices.put(graph.getId(), extractor.getIndex());
            }
        }
    }

    public static void createIndexPatterns(int pathLen){
        System.out.println("pattern index:");
        Collection<Graph> allGraphs = patterns.values();
        PathExtractor extractor = new PathExtractor(labelOption);
        for (Graph graph : allGraphs) {
            Collection<SimpleNode> allNodes = graph.getAllNodes();
            // for size from 1 to pathLen, generate paths of the graph
            for (int i = 1; i <= pathLen; i++) {
                extractor.generatePath(allNodes, i);
                for (Path path : extractor.getIndex()) {
                    //System.out.print(path);
                    putToPatternIndex(path);
                }
            }
        }
    }

    public static void putToPatternIndex(Path pattern) {

        if (patternsIndices.contains(pattern)) {
            //System.out.println(" Path is already in index");
            return;
        }
        else{
            //System.out.println(" Path is NOT in index");
            patternsIndices.add(pattern);
        }
    }

    public static void main(String[] args) throws Exception {

        if (args.length < 5) {
            System.out.println("Usage: <filename>" +
                                     " <path length>" +
                                     " <label option> " +
                                     " <index file name>" +
                                     " <patterns file name>" +
                    "where label option is: 0 for only labels, 1 for isoLabels, 2 for id instead of labels");
            System.exit(-1);
        }
        String filestr = args[0];
        int pathLen = new Integer(args[1]);
        File file = new File(filestr);
        indexFileName = args[3];
        patternFileName = args[4];
        graphs = new HashMap<>();
        graphIndices = new HashMap<>();
        patterns = new HashMap<>();
        patternsIndices = new ArrayList<>();
        labelOption = new Integer(args[2]);
        System.out.println("option: " + labelOption);
        parse(file, false);
        System.out.println("Number of targets: " + graphs.size());

        createIndexTarget(pathLen);
        //printIndices();
        System.out.println("--------------------");

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
                    if (candidatesExtractor.isCandidate(graphIndices.get(graphId), patternsIndices, labelOption)){
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
        File outfile = new File("candidates.txt");
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



    }
}
