package algorithms.search;

/**
 * BestFirstSearch solve problem by evaluating nodes same as Breadth First Search, but evaluates fewer nodes, by using
 * priority to each State
 */

public class BestFirstSearch extends BreadthFirstSearch {

    /**
     * Constructor
     */
    public BestFirstSearch() {
        super("BestFirstSearch");
    }

    @Override
    protected void setPriority(AState state, AState end) {
        state.setCost(end);
    }
}
