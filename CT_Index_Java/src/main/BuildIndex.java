package main;

import graph.Graph;
import graph.Vertex;
import tools.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by iva on 12/4/15.
 */
public class BuildIndex {

    public static ArrayList<Graph> targets;
    public static ArrayList<Graph> patterns;

    public static HashMap<String,Graph>             graphs;
    public static HashMap<String,Graph>             patternMaps;
    public static HashMap<String,ArrayList<Path>>   graphIndices;
    public static ArrayList<Path>                   patternsIndices;
    public static String                            indexFileName;
    public static int                               labelOption;

    private static ArrayList<Graph> parse(Scanner sc) throws Exception {
        ArrayList<Graph> graphs = new ArrayList<>();
        while(sc.hasNext()){
            graphs.add(new Graph(sc));
        }
        sc.close();
        return graphs;
    }

    /** whenever I am sure that the graph is constructed, create the isomer label
     * for each node */
    private static void createIsoLabels(Graph g){
        for (Vertex n : g.getAllVertices()) {
            n.setNlabel();
        }
    }

    /* prints the index of each graph in graphs */
    public static void printIndices(){
        Collection<Graph> graphsCollect = graphs.values();
        for (Graph g : graphsCollect) {
            for (Path path : graphIndices.get(g.getId())){
                System.out.println(path.toString(labelOption));
            }
        }
    }

    public static void writeToFile(Writer writer) throws IOException {
        Collection<Graph> graphsCollect = graphs.values();
        for (Graph g : graphsCollect) {
                writer.write("#" + g.getId() + "\n");
            for (Path path : graphIndices.get(g.getId())){
                writer.write(path.toString(labelOption) + "\n");
            }
        }
    }

    public static void createIndexTarget(int pathLen){
        Collection<Graph> allGraphs = graphs.values();
        PathExtractor extractor = new PathExtractor(labelOption);
        for (Graph graph : allGraphs) {
            Collection<Vertex> allNodes = graph.getAllVertices();
            extractor.generatePath(allNodes, pathLen);
            graphIndices.put(graph.getId(), extractor.getIndex());
            extractor.clearIndex();
        }
    }

    public static void createIndexPatterns(int pathLen){
        System.out.println("****** pattern index:");
        Collection<Graph> allGraphs = patternMaps.values();
        PathExtractor extractor = new PathExtractor(labelOption);
        for (Graph graph : allGraphs) {
            Collection<Vertex> allNodes = graph.getAllVertices();
            // for size from 1 to pathLen, generate paths of the graph
            for (int i = 1; i <= pathLen; i++) {
                extractor.generatePath(allNodes, i);
                for (Path path : extractor.getIndex()) {
                    putToPatternIndex(path);
                }
            }
        }
    }

    public static void putToPatternIndex(Path pattern) {
        if (patternsIndices.contains(pattern)) {
            return;
        }
        else{
            patternsIndices.add(pattern);
        }
    }

    public static void main(String[] args) throws Exception {
        long l1 = System.nanoTime();
        if (args.length < 5) {
            System.out.println("Usage: <targets file name>" +
                                     " <path length>" +
                                     " <label option> " +
                                     " <index file name>" +
                                     " <patterns file name>" +
                    "where label option is: 0 for only labels, 1 for isoLabels, 2 for id instead of labels");
            System.exit(-1);
        }

        Scanner targetScanner   = new Scanner(new File(args[0]));
        targets                 = parse(targetScanner);
        int pathLen             = new Integer(args[1]);
        indexFileName           = args[3];
        Scanner patternScanner  = new Scanner(new File(args[4]));
        patterns                = parse(patternScanner);
        graphs                  = new HashMap<>();
        graphIndices            = new HashMap<>();
        patternMaps             = new HashMap<>();
        patternsIndices         = new ArrayList<>();
        labelOption             = new Integer(args[2]);

        System.out.println("number of targets: " + targets.size());
        System.out.println("number of patterns: " + patterns.size());

        for (int i = 0; i < targets.size(); i++){
            Graph g = targets.get(i);
            graphs.put(g.getId(),g);
            if (labelOption == 1) {for(Vertex n: g.getAllVertices()) n.setNlabel();}
        }

        for (int i = 0; i < patterns.size(); i++){
            Graph g = patterns.get(i);
            patternMaps.put(g.getId(),g);
            if (labelOption == 1) {for(Vertex n: g.getAllVertices()) n.setNlabel();}
        }

        createIndexTarget(pathLen);
        //printIndices();

        // write the extracted paths to the specified index filename
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(indexFileName), "utf-8"))) {
            writeToFile(writer);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }

        createIndexPatterns(pathLen);
        CandidatesExtractor candidatesExtractor = new CandidatesExtractor();
        graphIndices.keySet().forEach(
                graphId -> {
                    if (candidatesExtractor.isCandidate(graphIndices.get(graphId), patternsIndices, labelOption)){
                        System.out.println("IT IS A CANDIDATE: " + graphId);
                        candidatesExtractor.addCandidate(graphId);
                    }
                }
        );
        System.out.println(candidatesExtractor.getCandidates().size());
        long l2 = System.nanoTime() - l1;
        System.out.println("done");
        System.out.println();
        System.out.println("Printing candidates ...");
        long l3 = System.nanoTime();

        // putNode the candidate graph ids in a file:
        File outfile = new File("candidates.txt");
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outfile), "utf-8"))) {
            for (String in : candidatesExtractor.getCandidates()) {
                writer.write(in + "\n");
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }

/*
        Verify verifier = new Verify();
        File f1 = new File("/home/iva/programming/graphIndexing/CT_Index_Java/candidates.txt");
        File f2 = new File("/home/iva/University/4thYear/GraphXProject/CT-Index/subisosIDs.txt");
        System.out.println("is file1 in file2: " + verifier.isFile1inFile2(f1, f2));
*/

        long l4 = System.nanoTime() - l3;
        long ltotal = System.nanoTime() - l1;
        System.out.println("done");
        System.out.println();
        System.out.println("Index Build Time [millisec]: " + (l2/1000000));
        System.out.println("Computing CandidatesTime [millisec]: " + (l4/1000000));
        System.out.println("Number of candidates: " + candidatesExtractor.getCandidates().size());
        System.out.println("Total running Time [millisec]: " + (ltotal/1000000));


    }
}
