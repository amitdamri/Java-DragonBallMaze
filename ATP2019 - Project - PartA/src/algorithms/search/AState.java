package algorithms.search;

import java.io.Serializable;

/**
 * AState defines "State" in ISearchable
 */

public abstract class AState implements Comparable<AState>, Serializable {

    private transient AState previous;
    protected transient int cost;

    public AState(AState prev, int cost) {
        previous = prev;
        this.cost = cost;
    }

    public AState getPrevious() {
        return previous;
    }

    protected void setPrevious(AState previous) {
        this.previous = previous;
    }

    public abstract boolean equals(Object o);

    public abstract int hashCode();

    public int getCost() {
        return cost;
    }

    public abstract void setCost(AState goalState);

    @Override
    public int compareTo(AState state) {
        return this.cost - state.cost;
    }


}
