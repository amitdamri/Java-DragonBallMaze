package algorithms.search;

import sun.misc.Queue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * BreadthFirstSearch algorithm for solving ISearchable problems
 */
public class BreadthFirstSearch extends ASearchingAlgorithm {

    PriorityQueue<AState> statesToVisit;

    /**
     * Constructor
     */
    public BreadthFirstSearch() {
        super("BreadthFirstSearch");
        statesToVisit = new PriorityQueue<>();
    }

    /**
     * Constructor with parameters
     * @param name
     */
    public BreadthFirstSearch(String name) {
        super(name);
        statesToVisit = new PriorityQueue<>();
    }

    /**
     * Solve problem by BFS algorithm
     * @param domain - ISearchable
     * @return outcome of algorithm running (instance Solution)
     */
    public Solution solve(ISearchable domain) {

        if (domain == null)
            return new Solution();

        HashSet<AState> visitedStates = new HashSet<>();
        statesToVisit.add(domain.getStartState());
        visitedStates.add(domain.getStartState());

        while (!statesToVisit.isEmpty()) {

            AState curr = statesToVisit.poll();

            for (AState possibleState : domain.getAllPossibleStates(curr)) {
                if (!visitedStates.contains(possibleState)) {
                    setPriority(possibleState, domain.getEndState());
                    possibleState.setPrevious(curr);
                    statesToVisit.add(possibleState);
                    visitedStates.add(possibleState);
                    if (possibleState.equals(domain.getEndState())) {
                        return new Solution(possibleState);
                    }
                }
            }
            nodesEvaluated++;
        }

        return new Solution();
    }


    protected void setPriority(AState state, AState end) {
        state.setCost(null);
    }


}
