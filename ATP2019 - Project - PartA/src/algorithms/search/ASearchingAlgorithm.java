package algorithms.search;

/**
 *ASearchingAlgorithm abstract class implements mutual methods of all searcher classes
 */
public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {

    protected int nodesEvaluated;
    private String name;

    /**
     * Constructor
     * @param name
     */
    public ASearchingAlgorithm(String name) { this.name = name; }

    /**
     * @return number of nodes evaluated during each searching algorithm
     */
    public int getNumberOfNodesEvaluated() { return nodesEvaluated; }

    /**
     * @return Searching algorithm name
     */
    public String getName() { return name; }
}
