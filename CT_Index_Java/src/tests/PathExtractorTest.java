package tests;

import graph.Graph;
import graph.SimpleNode;
import org.junit.Assert;
import org.junit.Test;
import tools.Path;
import tools.PathExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by iva on 12/4/15.
 */
public class PathExtractorTest {

    private Graph generateGraph(File file, int labelOption) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        String[] splittedline = null;
        int graphID = 0;
        Graph graph = null;
        while ((line = reader.readLine()) != null) {
            splittedline = line.split(" ");
            if(splittedline[0].equals("t")) {
                graphID = Integer.parseInt(splittedline[2]);
                graph = new Graph(graphID);
            }
            if (splittedline[0].equals("v")) {
                int id = Integer.parseInt(splittedline[1]);
                String property = splittedline[2];
                SimpleNode n = new SimpleNode(id, property);
                graph.putNode(n);
            }
            if (splittedline[0].equals("e")) {
                SimpleNode srcNode = graph.getNode(Integer.parseInt(splittedline[1])); // the first node id
                SimpleNode dstNode = graph.getNode(Integer.parseInt(splittedline[2])); // the second node id
                if (srcNode == null || dstNode == null) {
                    throw new Exception("Can't create new edge, source " +
                            "or destination node does not exist");
                }
                srcNode.addEdge(dstNode, splittedline[3]); // the property id
                dstNode.addEdge(srcNode, splittedline[3]); // the property id
            }
        }
        if (labelOption == 1) {
            createIsoLabels(graph);
        }
        return graph;
    }

    /** whenever I am sure that the graph is constructed, create the isomer label
     * for each node */
    private static void createIsoLabels(Graph g){
        for (SimpleNode n : g.getAllNodes()) {
            n.setIsoLabel();
        }
    }

    private boolean contains(String p, ArrayList<String> paths){
        for (String path : paths) {
            System.out.println("*" + path);
            if (path.equals(p) || path.equals(reverse(p))) return true;
        }
        return false;
    }

    /* Important: this works only for not isomer graphs */
    private String reverse(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    /* generate path tests using normal labels */
    @Test
    public void generatePathsC2H6Len3() throws Exception{
        String fstr = "/home/iva/programming/graphIndexing/graphs/C2H6.txt";
        int labelOption = 0;
        int maxPathLen = 3;
        String edgeLabel = "-";

        File f = new File(fstr);
        Graph g = generateGraph(f,labelOption);
        Collection<SimpleNode> allNodes = g.getAllNodes();

        ArrayList<Path> actualPaths;
        ArrayList<String> expectedPathsStr = new ArrayList<>();
        ArrayList<String> actualPathsStr = new ArrayList<>();

        PathExtractor pathExtractor = new PathExtractor(edgeLabel,labelOption);
        for (int i = 1; i <= maxPathLen; i++) {
            pathExtractor.generatePath(allNodes, i);
        }
        actualPaths = pathExtractor.getIndex();
        for (Path p : actualPaths) {
            actualPathsStr.add(p.toString(labelOption));
        }

        expectedPathsStr.add("C");
        expectedPathsStr.add("H");
        expectedPathsStr.add("C-C");
        expectedPathsStr.add("H-C");
        expectedPathsStr.add("H-C-H");
        expectedPathsStr.add("C-C-H");

        /* check that strings in actual paths are contained in expected paths*/
        for (String ep : expectedPathsStr) {
            assertTrue(contains(ep, actualPathsStr));
        }

        /* check that strings in expected paths are contained in actual paths*/
        for (String ep : actualPathsStr) {
            assertTrue(contains(ep, expectedPathsStr));
        }
    }

    @Test
    public void generatePathsC5H11Len3() throws Exception{
        String fstr = "/home/iva/programming/graphIndexing/graphs/C5H11.txt";
        int labelOption = 0;
        int maxPathLen = 3;
        String edgeLabel = "-";

        File f = new File(fstr);
        Graph g = generateGraph(f,labelOption);
        Collection<SimpleNode> allNodes = g.getAllNodes();

        ArrayList<Path> actualPaths;
        ArrayList<String> expectedPathsStr = new ArrayList<>();
        ArrayList<String> actualPathsStr = new ArrayList<>();

        PathExtractor pathExtractor = new PathExtractor(edgeLabel,labelOption);
        for (int i = 1; i <= maxPathLen; i++) {
            pathExtractor.generatePath(allNodes, i);
        }
        actualPaths = pathExtractor.getIndex();
        for (Path p : actualPaths) {
            actualPathsStr.add(p.toString(labelOption));
        }

        expectedPathsStr.add("C");
        expectedPathsStr.add("H");
        expectedPathsStr.add("C-C");
        expectedPathsStr.add("H-C");
        expectedPathsStr.add("H-C-H");
        expectedPathsStr.add("C-C-H");
        expectedPathsStr.add("C-C-C");

        /* check that strings in actual paths are contained in expected paths*/
        for (String ep : expectedPathsStr) {
            assertTrue(contains(ep, actualPathsStr));
        }

        /* check that strings in expected paths are contained in actual paths*/
        for (String ep : actualPathsStr) {
            assertTrue(contains(ep, expectedPathsStr));
        }
    }

    // todo
    @Test
    public void generatePathsAIDS0Len3() throws Exception{
        String fstr = "/home/iva/programming/graphIndexing/graphs/aids#0.txt";
        int labelOption = 0;
        int maxPathLen = 3;
        String edgeLabel = "-";

        File f = new File(fstr);
        Graph g = generateGraph(f,labelOption);
        Collection<SimpleNode> allNodes = g.getAllNodes();

        ArrayList<Path> actualPaths;
        ArrayList<String> expectedPathsStr = new ArrayList<>();
        ArrayList<String> actualPathsStr = new ArrayList<>();

        PathExtractor pathExtractor = new PathExtractor(edgeLabel,labelOption);
        for (int i = 1; i <= maxPathLen; i++) {
            pathExtractor.generatePath(allNodes, i);
        }
        actualPaths = pathExtractor.getIndex();
        for (Path p : actualPaths) {
            //System.out.println("---- " + p.toString(labelOption));
            actualPathsStr.add(p.toString(labelOption));
        }

        expectedPathsStr.add("0");
        expectedPathsStr.add("1");
        expectedPathsStr.add("2");
        expectedPathsStr.add("3");
        expectedPathsStr.add("0-1");
        expectedPathsStr.add("0-0");
        expectedPathsStr.add("0-3");
        expectedPathsStr.add("1-2");
        expectedPathsStr.add("0-0-0");
        expectedPathsStr.add("0-0-3");
        expectedPathsStr.add("0-1-2");
        expectedPathsStr.add("0-0-1");
        expectedPathsStr.add("1-2-1");

        /* check that strings in actual paths are contained in expected paths*/
        for (String ep : expectedPathsStr) {
            assertTrue(contains(ep, actualPathsStr));
        }

        /* check that strings in expected paths are contained in actual paths*/
        for (String ep : actualPathsStr) {
            assertTrue(contains(ep, expectedPathsStr));
        }
    }

    /* generate path labels using isomer labels */
    @Test
    public void generateIsoPathsC2H6Len3() throws Exception{
        String fstr = "/home/iva/programming/graphIndexing/graphs/C2H6.txt";
        int labelOption = 1;
        int maxPathLen = 3;
        String edgeLabel = "-";

        File f = new File(fstr);
        Graph g = generateGraph(f,labelOption);
        Collection<SimpleNode> allNodes = g.getAllNodes();

        ArrayList<Path> actualPaths;
        ArrayList<String> expectedPathsStr = new ArrayList<>();
        ArrayList<String> actualPathsStr = new ArrayList<>();

        PathExtractor pathExtractor = new PathExtractor(edgeLabel,labelOption);
        for (int i = 1; i <= maxPathLen; i++) {
            pathExtractor.generatePath(allNodes, i);
        }
        actualPaths = pathExtractor.getIndex();
        for (Path p : actualPaths) {
            actualPathsStr.add(p.toString(labelOption));
        }

        expectedPathsStr.add("HC");
        expectedPathsStr.add("CCHHH");
        expectedPathsStr.add("HC-CCHHH");
        expectedPathsStr.add("CCHHH-CCHHH");
        expectedPathsStr.add("HC-CCHHH-CCHHH");
        expectedPathsStr.add("HC-CCHHH-HC");

        /* check that strings in actual paths are contained in expected paths*/
        for (String ep : expectedPathsStr) {
            assertTrue(contains(ep, actualPathsStr));
        }

        /* check that strings in expected paths are contained in actual paths*/
        for (String ep : actualPathsStr) {
            assertTrue(contains(ep, expectedPathsStr));
        }
    }
}