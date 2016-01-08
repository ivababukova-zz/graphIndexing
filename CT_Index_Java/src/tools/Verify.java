package tools;

import graph.Graph;

import java.io.*;
import java.util.*;

/**
 * Created by iva on 12/11/15.
 */
public class Verify {


    HashMap<Integer,Graph> graphs;

    //public Verify(HashMap<Integer,Graph> graphs) {
       // this.graphs = graphs;
    //}

    public boolean verifyGraphNumber(int expectedNumber){
        return expectedNumber == graphs.size();
    }

    // verify whether graphs with graph id from 0 to endId are in the graphs hasmap param
    public boolean verifyGraphIds(int endId){
        for (int id = 0; id < endId; id++) {
            if (graphs.get(id) == null) {
                return false;
            }
        }
        return true;
    }

    // verify that file1 is in file2
    public boolean isFile1inFile2(File f1, File f2) throws IOException {
        HashMap<Integer, Boolean> f1dict = new HashMap<>();
        String line = null;
        int graphID = 0;
        BufferedReader reader = new BufferedReader(new FileReader(f1));
        while ((line = reader.readLine()) != null) {
            f1dict.put(Integer.parseInt(line),true);
        }
        reader = new BufferedReader(new FileReader(f2));
        System.out.println("---- files verifier ----");
        while ((line = reader.readLine()) != null) {
            if (f1dict.get(Integer.parseInt(line)) == null) {
                System.out.println("this is not in f1:" + line);
                //return false;
            }
        }

        return true;
    }


}
