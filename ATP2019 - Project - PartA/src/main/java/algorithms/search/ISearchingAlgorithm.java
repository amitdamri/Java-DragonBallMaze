package algorithms.search;

/**
 * ISearchingAlgorithm interface sets the required methods for searchers
 */
public interface ISearchingAlgorithm {

    Solution solve(ISearchable domain);
    int getNumberOfNodesEvaluated();
    String getName();
}
