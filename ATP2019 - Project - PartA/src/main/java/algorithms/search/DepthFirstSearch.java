package algorithms.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

/**
 * DepthFirstSearch algorithm for solving ISearchable problems
 */
public class DepthFirstSearch extends ASearchingAlgorithm {

    Stack<AState> statesToVisit;

    /**
     * Constructor
     */
    public DepthFirstSearch() {

        super("DepthFirstSearch");
        statesToVisit = new Stack<>();
    }

    /**
     * Solve problem by DFS algorithm
     * @param domain - ISearchable
     * @return outcome of algorithm running (instance Solution)
     */
    public Solution solve(ISearchable domain) {

        if (domain == null)
            return new Solution();

        HashSet<AState> visitedStates = new HashSet<>();
        statesToVisit.push(domain.getStartState());

        while (!statesToVisit.isEmpty()) {

            AState curr = statesToVisit.pop();

            ArrayList<AState> possibleStates = domain.getAllPossibleStates(curr);

            if (visitedStates.contains(curr) || (!curr.equals(domain.getEndState()) && possibleStates.size() == 0)) {
                statesToVisit.push(curr.getPrevious());
                continue;
            }

            else if (curr.equals(domain.getEndState())) {
                return new Solution(curr);
            }

            visitedStates.add(curr);

            for (AState state : possibleStates) {
                if (!statesToVisit.contains(state) && !visitedStates.contains(state))
                    statesToVisit.push(state);
            }

            nodesEvaluated++;

        }
        return new Solution();
    }
}
