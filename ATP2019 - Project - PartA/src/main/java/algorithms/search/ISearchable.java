package algorithms.search;

import java.util.ArrayList;

/**
 * ISearchable interface is an abstraction of all problems solving by ISearchingAlgorithm
 */
public interface ISearchable {

     ArrayList<AState> getAllPossibleStates(AState state);
     AState getStartState();
     AState getEndState();

}
