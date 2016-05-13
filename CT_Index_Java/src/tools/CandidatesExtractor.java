package tools;

import graph.Vertex;

import java.util.ArrayList;

/**
 * Created by iva on 12/6/15.
 */
public class CandidatesExtractor {

    private ArrayList<String> candidates; // arraylist of the ids of the graphs that are candiates

    public CandidatesExtractor(){
        this.candidates = new ArrayList<>();
    }
    public void addCandidate(String graphId){
        this.candidates.add(graphId);
    }
    public ArrayList<String> getCandidates(){
        return this.candidates;
    }

    /**
     * Takes an arraylist of target paths @tarF: all paths in a graph and arraylist
     * of pattern paths @patF: all pattern paths and returns true if each path
     * in @patF is contained in @tarF
     * **/
    public boolean isCandidate(ArrayList<Path> tarF, ArrayList<Path> patF, int option){
        for (Path pp : patF) {
            if (!contains(tarF, pp, option)){
                return false;
            }
        }
        return true;
    }

    private boolean contains(ArrayList<Path> tpaths, Path pp, int option) {
        for(Path tp : tpaths) {
            if (containsPath(tp,pp,option)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Does target path contain pattern path?
     * true: it contains
     * false: it doesn't contain
     * */
    private boolean containsPath(Path tp, Path pp, int option){
        // if the number of nodes in pp is more than in tp, they are not equal:
        if(tp.length() < pp.length()) {return false;}

        // if the first label of tp is not the same of the first label in pp, they are not equal;
        if (!tp.toString(0).equals(pp.toString(0))) {return false;}

        for (int i = 0; i < tp.length(); i++) {
            if (!containsLabel(tp.label(i, option), pp.label(i, option))) {
                ArrayList<Vertex> reversedtpArr = new ArrayList<>(tp.getNodes());
                Path reversedtp                 = new Path(reversedtpArr);
                reversedtp.reverse();
                if (!containsLabel(reversedtp.label(i, option), pp.label(i, option))) {return false;}
            }
        }
        return true;
    }

    /**
     * Checks whether the targetLabel contains the patternLabel
     * true: it contains
     * false: it doesn't contain
     * */
    private boolean containsLabel(String tarl, String patl) {
        if (tarl.length() < patl.length()) {return false;}
        if (!tarl.substring(0,1).equals(patl.substring(0,1))){return false;} // first symbols must match
        int j = 1;
        for (int i = 1; i < patl.length(); i++){
            String p = patl.substring(i,i+1);

            if (j == tarl.length()) {return false;} // tarl is exhausted, but patl not

            while (j < tarl.length()){
                String t = tarl.substring(j,j+1);
                if(p.equals(t)) {
                    j++;
                    break;
                }
                else if(t.compareTo(p) > 0) {
                    return false;
                }
                j++;
            }
        }
        return true;
    }



}
