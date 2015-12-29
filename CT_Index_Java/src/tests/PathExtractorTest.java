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
            System.out.println(path);
            if (path.equals(p)) return true;
        }
        return false;
    }

    // generate path test
    @Test
    public void generatePathsLen3() throws Exception{
        String fstr = "/home/iva/programming/graphIndexing/graphs/C2H6.txt";
        int labelOption = 0;
        int pathLen = 3;
        String edgeLabel = "-";

        File f = new File(fstr);
        Graph g = generateGraph(f,labelOption);
        Collection<SimpleNode> allNodes = g.getAllNodes();

        ArrayList<Path> actualPaths = new ArrayList<>();
        ArrayList<String> expectedPathsStr = new ArrayList<>();
        ArrayList<String> actualPathsStr = new ArrayList<>();

        PathExtractor pathExtractor = new PathExtractor(edgeLabel,labelOption);
        pathExtractor.generatePath(allNodes, pathLen);
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

        for (String ep : expectedPathsStr) {
            System.out.println("** " + ep);
            assertTrue(contains(ep, actualPathsStr));
            System.out.println("**********");
        }
    }

    //

}