package main;

import graph.Graph;
import graph.Node;
import tools.CandidatesExtractor;
import tools.PathExtractor;
import tools.Verify;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by iva on 12/4/15.
 */
public class BuildIndex {

    public static HashMap<Integer,Graph> graphs;
    public static Graph pattern;
    public static HashMap<Integer,ArrayList<String>> graphIndices;
    public static ArrayList<String> patternIndices;
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
                if (isPattern) pattern = new Graph(Integer.parseInt(splittedline[2]));
                else {
                    Graph graph = new Graph(Integer.parseInt(splittedline[2]));
                    graphs.put(Integer.parseInt(splittedline[2]), graph);
                    graphID = Integer.parseInt(splittedline[2]);
                }
            }
            if (splittedline[0].equals("v")) {
                int id = Integer.parseInt(splittedline[1]);
                String property = splittedline[2];
                Node n = new Node(id, property);
                if (isPattern) pattern.put(n);
                else {
                    graphs.get(graphID).put(n);
                }
            }
            if (splittedline[0].equals("e")) {
                Node srcNode;
                Node dstNode;
                if (isPattern) {
                    srcNode = pattern.getNode(Integer.parseInt(splittedline[1]));
                    dstNode = pattern.getNode(Integer.parseInt(splittedline[2]));
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

    public static void createIndex(int pathLen){
        Collection<Graph> allGraphs = graphs.values();
        PathExtractor extractor = new PathExtractor();
        for (Graph graph : allGraphs) {
            Collection<Node> allNodes = graph.getAllNodes();
            // for size from 1 to pathLen, generate paths of the graph
            for (int i = 1; i <= pathLen; i++) {
                //System.out.println("new pathLen: " + i);
                extractor.generatePath(allNodes, i);
                graphIndices.put(graph.getId(),extractor.getIndex());
            }
        }
    }

    public static void main(String[] args) throws Exception {

        if (args.length < 4) {
            System.out.println("Usage: <filename> <path length> <index file name> <pattern file name>");
            System.exit(-1);
        }
        String filestr = args[0];
        int pathLen = new Integer(args[1]);
        File file = new File(filestr);
        indexFileName = args[2];
        patternFileName = args[3];
        graphs = new HashMap<>();
        graphIndices = new HashMap<>();

        parse(file,false);
        //System.out.println("Database size: " + graphs.size());

        Verify v = new Verify(graphs);
        System.out.println("the size of graphs is correct: " + v.verifyGraphNumber(40000));
        System.out.println("all graphs are parsed: " + v.verifyGraphIds(39999));

        /* verify whether the file with candidates contains the isomorphic graphs */
/*
        File f1 = new File("/home/iva/programming/graphIndexing/candidateIDs-AIDS-pattern.txt");
        File f2 = new File("/home/iva/University/4thYear/GraphXProject/CT-Index/subisosIDs.txt");
        System.out.println("file 2 is in file 1: " + v.isFile1inFile2(f1, f2));
*/
        createIndex(pathLen);
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
        parse(file,true); // parse the pattern
        PathExtractor patternExtractor = new PathExtractor();
        Collection<Node> allNodes = pattern.getAllNodes();
        patternIndices = new ArrayList<>();
        for (int i = 1; i <= pathLen; i++) {
            patternExtractor.generatePath(allNodes,i);
            patternIndices = patternExtractor.getIndex();
        }
        //System.out.println("*** pattern index ***");
        for (String path : patternIndices) {
           // System.out.println(path);
        }
        //System.out.println("***");
        CandidatesExtractor candidatesExtractor = new CandidatesExtractor();

        long l1 = System.currentTimeMillis();
        graphIndices.keySet().forEach(
                graphId -> {
                    if (candidatesExtractor.isCandidate(graphIndices.get(graphId), patternIndices)){
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

        // put the candidate graph ids in a file:
        File outfile = new File("candidateIDs-AIDS-pattern.txt");
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
