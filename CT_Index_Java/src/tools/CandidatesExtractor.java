package tools;

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
    /* compare 1 target with the patterns and return true if the target is
     a candidate for subgraph isomorphism
    */
    public boolean isCandidate(ArrayList<String> targetFeatures, ArrayList<String> patternFeatures){
        boolean candidate = true;
        boolean contains = false;
        for (String pfeature : patternFeatures) {
            for (String tfeature : targetFeatures) {
                if (tfeature.equals(pfeature)) {
                    contains = true;
                    break;
                }
            }
            // even if 1 feature from the patterns is not contained in the target,
            // the target is a candidate and we stop checking
            if (!contains) {
                candidate = false;
                break;
            }
            contains = false;
        }

        return candidate;
    }

    /**
     *
     *
     * */
    private boolean contains(String targetPath, String patternPath) {
        return false;
    }



}
