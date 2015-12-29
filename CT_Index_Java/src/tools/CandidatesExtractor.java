package tools;

import graph.Graph;

import java.util.ArrayList;

/**
 * Created by iva on 12/6/15.
 */
public class CandidatesExtractor {

   // private ArrayList<String> patternFeatures; // all features of the small query graph
   // private HashMap<String, ArrayList<String>> targetsFeatures; // map of graphid : feature for each graph
    private ArrayList<Integer> candidates; // arraylist of the ids of the graphs that are candiates

    // todo pass features as files and here extract files
    // todo it is not needed to have features as fields
    public CandidatesExtractor(){
        this.candidates = new ArrayList<>();
    }

    public void addCandidate(int graphId){
        this.candidates.add(graphId);
    }

    public ArrayList<Integer> getCandidates(){
        return this.candidates;
    }

    /**
     * Takes an arraylist of target paths @tarF: all paths in a graph and arraylist
     * of pattern paths @patF: all pattern paths and returns true if each path
     * in @patF is contained in @tarF
     * **/
    public boolean isCandidate(ArrayList<Path> tarF, ArrayList<Path> patF, int option){
        if (tarF.size() < patF.size()) return false;

        for (Path pp : patF) {
            if (!contains(tarF, pp, option)){
                return false;
            }
        }

        return true;
    }

    private boolean contains(ArrayList<Path> tpaths, Path pp, int option) {

        for(Path tp : tpaths) {
            if (containsPath(tp,pp,option)) return true;
        }
        return false;
    }

    /**
     * Does target path contain pattern path?
     * true: it contains
     * false: it doesn't contain
     * */
    private boolean containsPath(Path tp, Path pp, int option){
        if(tp.length() < pp.length()) {return false;}
        if (!tp.toString(0).equals(pp.toString(0))) {return false;}

        for (int i = 0; i < tp.length(); i++) {
            if (!containsLabel(tp.getNodeLabel(i,option), pp.getNodeLabel(i, option))) return false;
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
        for (int i = 1; i < tarl.length() - 1; i++){
            String t = tarl.substring(i,i+1);
            while (j < patl.length() - 1){
                String p = patl.substring(j,j+1);
                if(t.equals(p)) {
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
