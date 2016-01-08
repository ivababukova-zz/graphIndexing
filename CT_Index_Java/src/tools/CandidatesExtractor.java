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
            //System.out.println("*");
            if (!contains(tarF, pp, option)){
                return false;
            }
        }

        return true;
    }

    private boolean contains(ArrayList<Path> tpaths, Path pp, int option) {

        for(Path tp : tpaths) {
            if (containsPath(tp,pp,option)) {
                //System.out.println("move to next one");
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
        //System.out.print(tp.toString(option) + " " + pp.toString(option));

        // if the number of nodes in pp is more than in tp, they are not equal:
        if(tp.length() < pp.length()) {/*System.out.println(" false 1");*/return false;}

        // if the first label of tp is not the same of the first label in pp, they are not equal;
        if (!tp.toString(0).equals(pp.toString(0))) {
            //System.out.println(" false 2 " + tp.toString(0) + " " + pp.toString(0));
            return false;}


        for (int i = 0; i < tp.length(); i++) {
            //System.out.print("\n| " + tp.getNodeLabel(i, option) + " + " + pp.getNodeLabel(i, option));
            if (!containsLabel(tp.getNodeLabel(i,option), pp.getNodeLabel(i, option))) {
                //System.out.println(" false 3");
                return false;
            }
        }
        //System.out.println(" true");
        return true;
    }

    /**
     * Checks whether the targetLabel contains the patternLabel
     * true: it contains
     * false: it doesn't contain
     * */
    private boolean containsLabel(String tarl, String patl) {
        //System.out.println("\nin containsLabel");
        if (tarl.length() < patl.length()) {return false;}
        if (!tarl.substring(0,1).equals(patl.substring(0,1))){return false;} // first symbols must match
        int j = 1;
        for (int i = 1; i < patl.length(); i++){
            String p = patl.substring(i,i+1);
            //System.out.println("p: " + p);

            if (j == tarl.length()) {return false;} // tarl is exhausted, but patl not

            while (j < tarl.length()){
                String t = tarl.substring(j,j+1);
                //System.out.println("-- t: " + t);
                if(p.equals(t)) {
                    j++;
                    //System.out.println("t and p equal");
                    break;
                }
                else if(t.compareTo(p) > 0) {
                    //System.out.println("t and p DON'T equal");
                    return false;
                }
                j++;
            }
        }
        return true;
    }



}
