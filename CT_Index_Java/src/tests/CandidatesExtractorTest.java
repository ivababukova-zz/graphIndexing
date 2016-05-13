package tests;

import graph.Vertex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tools.CandidatesExtractor;
import tools.Path;

import java.util.ArrayList;

/**
 * Created by iva on 12/28/15.
 */
public class CandidatesExtractorTest {

    private CandidatesExtractor candidatesExtractor;

    private Path generatePath(ArrayList<String> labels, String edgeLabel) {
        ArrayList<Vertex> nodes = new ArrayList<>();
        Vertex n;
        int pathLen = labels.size();
        for (int i = 0; i < pathLen; i++) {
            n = new Vertex(i, labels.get(i).substring(0,1));
            //n.setNlabel(labels.get(i).substring(1));
            nodes.add(i,n);
        }
        Path path = new Path(nodes,edgeLabel);
        return path;
    }

    @Before
    public void setUp() throws Exception {
        candidatesExtractor = new CandidatesExtractor();
    }

    @Test
    public void isCandidate1PathTest1(){
        ArrayList<String> tLabels = new ArrayList<>();
        tLabels.add("AB");
        tLabels.add("AABBBBCDE");
        tLabels.add("EFGGG");
        Path tarP = generatePath(tLabels, "-");

        ArrayList<String> pLabels = new ArrayList<>();
        pLabels.add("AB");
        pLabels.add("AABCDE");
        pLabels.add("EFG");
        Path pattP = generatePath(pLabels, "-");

        ArrayList<Path> tarr = new ArrayList<>();
        tarr.add(tarP);
        ArrayList<Path> parr = new ArrayList<>();
        parr.add(pattP);
        Assert.assertTrue(candidatesExtractor.isCandidate(tarr, parr, 1));
    }

    @Test
    public void isCandidate1PathTest2(){
        ArrayList<String> tLabels = new ArrayList<>();
        tLabels.add("AB");
        tLabels.add("ACDE");
        tLabels.add("EFGGG");
        Path tarP = generatePath(tLabels, "-");

        ArrayList<String> pLabels = new ArrayList<>();
        pLabels.add("AB");
        pLabels.add("AABCDE");
        pLabels.add("EFG");
        Path pattP = generatePath(pLabels, "-");

        ArrayList<Path> tarr = new ArrayList<>();
        tarr.add(tarP);
        ArrayList<Path> parr = new ArrayList<>();
        parr.add(pattP);
        Assert.assertFalse(candidatesExtractor.isCandidate(tarr, parr, 1));
    }

    @Test
    public void isCandidate1PathTest3(){
        ArrayList<String> tLabels = new ArrayList<>();
        tLabels.add("AB");
        tLabels.add("ACDE");
        tLabels.add("EFGGG");
        Path tarP = generatePath(tLabels, "-");

        ArrayList<String> pLabels = new ArrayList<>();
        pLabels.add("AB");
        pLabels.add("BACDE");
        pLabels.add("EFG");
        Path pattP = generatePath(pLabels, "-");

        ArrayList<Path> tarr = new ArrayList<>();
        tarr.add(tarP);
        ArrayList<Path> parr = new ArrayList<>();
        parr.add(pattP);
        Assert.assertFalse(candidatesExtractor.isCandidate(tarr, parr, 1));
    }

    @Test
    public void isCandidateMultipleTPathsTest(){
        ArrayList<String> tLabels = new ArrayList<>();
        tLabels.add("AB");
        tLabels.add("ACDE");
        tLabels.add("CEFGGG");
        Path tarP = generatePath(tLabels, "-");

        tLabels.clear();
        tLabels.add("ABC");
        tLabels.add("AAAA");
        tLabels.add("CCDDDE");
        Path tarP2 = generatePath(tLabels,"-");

        ArrayList<String> pLabels = new ArrayList<>();
        pLabels.add("AB");
        pLabels.add("A");
        pLabels.add("C");
        Path pattP = generatePath(pLabels, "-");

        ArrayList<Path> tarr = new ArrayList<>();
        tarr.add(tarP);
        tarr.add(tarP2);
        ArrayList<Path> parr = new ArrayList<>();
        parr.add(pattP);
        Assert.assertTrue(candidatesExtractor.isCandidate(tarr, parr, 1));
    }

    @Test
    public void isCandidateMultiplePPathsTest(){
        ArrayList<String> tLabels = new ArrayList<>();
        tLabels.add("ABBCCCD");
        tLabels.add("ACDE");
        tLabels.add("CEFGGG");
        Path tarP = generatePath(tLabels, "-");

        ArrayList<String> pLabels = new ArrayList<>();
        pLabels.add("AB");
        pLabels.add("A");
        pLabels.add("C");
        Path pattP = generatePath(pLabels, "-");

        pLabels.clear();
        pLabels.add("ABC");
        pLabels.add("AE");
        pLabels.add("CE");
        Path pattP2 = generatePath(pLabels,"-");

        pLabels.clear();
        pLabels.add("A");
        pLabels.add("AC");
        pLabels.add("CGG");
        Path pattP3 = generatePath(pLabels,"-");

        ArrayList<Path> tarr = new ArrayList<>();
        tarr.add(tarP);
        ArrayList<Path> parr = new ArrayList<>();
        parr.add(pattP);
        parr.add(pattP2);
        parr.add(pattP3);
        Assert.assertFalse(candidatesExtractor.isCandidate(tarr, parr, 1));
    }

    @Test
    public void isCandidateMultiplePathsTest1(){
        ArrayList<String> tLabels = new ArrayList<>();
        tLabels.add("AB");
        tLabels.add("ACDE");
        tLabels.add("CEFGGG");
        Path tarP = generatePath(tLabels, "-");

        tLabels.clear();
        tLabels.add("ABC");
        tLabels.add("AAAA");
        tLabels.add("E");
        Path tarP2 = generatePath(tLabels,"-");

        tLabels.clear();
        tLabels.add("HH");
        tLabels.add("HC");
        tLabels.add("HCHHH");
        Path tarP3 = generatePath(tLabels,"-");

        ArrayList<String> pLabels = new ArrayList<>();
        pLabels.add("AB");
        pLabels.add("A");
        pLabels.add("C");
        Path pattP = generatePath(pLabels, "-");

        pLabels.clear();
        pLabels.add("A");
        pLabels.add("A");
        pLabels.add("E");
        Path pattP2 = generatePath(pLabels,"-");

        ArrayList<Path> tarr = new ArrayList<>();
        tarr.add(tarP);
        tarr.add(tarP2);
        tarr.add(tarP3);

        ArrayList<Path> parr = new ArrayList<>();
        parr.add(pattP);
        parr.add(pattP2);
        Assert.assertTrue(candidatesExtractor.isCandidate(tarr, parr, 1));
    }

    @Test
    public void isCandidateMultiplePathsTest2(){
        ArrayList<String> tLabels = new ArrayList<>();
        tLabels.add("AB");
        tLabels.add("ACDE");
        tLabels.add("CEFGGG");
        Path tarP = generatePath(tLabels, "-");

        tLabels.clear();
        tLabels.add("ABC");
        tLabels.add("AAAA");
        tLabels.add("E");
        Path tarP2 = generatePath(tLabels,"-");

        tLabels.clear();
        tLabels.add("HH");
        tLabels.add("HC");
        tLabels.add("HCHHH");
        Path tarP3 = generatePath(tLabels,"-");

        ArrayList<String> pLabels = new ArrayList<>();
        pLabels.add("AB");
        pLabels.add("A");
        pLabels.add("C");
        Path pattP = generatePath(pLabels, "-");

        pLabels.clear();
        pLabels.add("A");
        pLabels.add("A");
        pLabels.add("G");
        Path pattP2 = generatePath(pLabels,"-");

        ArrayList<Path> tarr = new ArrayList<>();
        tarr.add(tarP);
        tarr.add(tarP2);
        tarr.add(tarP3);

        ArrayList<Path> parr = new ArrayList<>();
        parr.add(pattP);
        parr.add(pattP2);
        Assert.assertFalse(candidatesExtractor.isCandidate(tarr, parr, 1));
    }


}
