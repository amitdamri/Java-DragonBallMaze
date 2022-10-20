package algorithms.search;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Solution - an outcome of algorithm, list of AStates
 */
public class Solution implements Serializable {

    private AState finalState;

    public Solution() {}


    /**
     * Constructor
     *
     * @param startState
     * @param finalState
     */
    public Solution(AState finalState) {
        this.finalState = finalState;
    }

    /**
     * @return list of AStates - the solution's path
     */
    public ArrayList<AState> getSolutionPath() {

        ArrayList<AState> solution = new ArrayList<>();
        solution.add(finalState);
        AState last = solution.get(0);

        if (last != null) {
            while (last.getPrevious() != null) {
                last = last.getPrevious();
                solution.add(0, last);

            }
        }

        return solution;

    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        List<AState> tempList = new ArrayList<>();
        AState tempState = finalState;
        while (null != tempState){
            tempList.add(tempState);
            tempState = tempState.getPrevious();
        }
        stream.writeObject(tempList);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        List<AState> tempList = (ArrayList) stream.readObject();
        this.finalState = tempList.get(0);
        AState tempState = finalState;
        for (int i=1;i<tempList.size();i++){
            tempState.setPrevious(tempList.get(i));
            tempState = tempState.getPrevious();
        }
        //the first AState
        tempState.setPrevious(null);
    }

    @Override
    public String toString() {
        return "********Maze Solution********";
    }
}
