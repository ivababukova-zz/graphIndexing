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
    /* compare 1 target with the pattern and return true if the target is
     a candidate for subgraph isomorphism
    */
    public boolean isCandidate(ArrayList<String> targetFeatures, ArrayList<String> patternFeatures){
        boolean candidate = true;
        boolean contains = false;
        System.out.println("in isCandidate ...");
        for (String pfeature : patternFeatures) {
           // System.out.println("* current pattern feature: " + pfeature + "!!!!!!!!!!!");
            for (String tfeature : targetFeatures) {
              //  System.out.println("** current target feature: " + tfeature);
                if (tfeature.equals(pfeature)) {
              //      System.out.println("YES");
                    contains = true;
                    break;
                }
                else {
                //    System.out.println("NO");
                }
            }
            // even if 1 feature from the pattern is not contained in the target,
            // the target is a candidate and we stop checking
            if (!contains) {
                candidate = false;
                break;
            }
            contains = false;
        }

        return candidate;
    }

}
