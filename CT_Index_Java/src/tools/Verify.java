package tools;

import graph.Graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by iva on 12/11/15.
 */
public class Verify {


    HashMap<Integer,Graph> graphs;

    public Verify(HashMap<Integer,Graph> graphs) {
        this.graphs = graphs;
    }

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

}
