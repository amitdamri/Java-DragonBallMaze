package algorithms.search;

import algorithms.mazeGenerators.Position;


/**
 * MazeState defines AState of Maze
 */

public class MazeState extends AState {

    private Position position;

    /**
     * Constructor
     * @param position
     * @param previous
     * @param cost
     */
    public MazeState(Position position, AState previous, int cost) {

        super(previous, cost);
        this.position = new Position(position);
    }

    /**
     * @return position of given MazeState
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @param o
     * @return "true" if MazeStates are equals or "false" otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof MazeState)) return false;

        return this.position.equals(((MazeState) o).position);
    }

    /**
     * @return string of MazeState - i.e. string of position
     */
    @Override
    public String toString() {
        return position.toString();
    }

    /**
     * @return integer - function outcome
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int rowAndColumn = position.getRowIndex()+position.getColumnIndex();
        int result = 1;
        result = prime*result+rowAndColumn;
        return result;

    }

    @Override
    public void setCost(AState goalState) {

        if (goalState != null && goalState instanceof MazeState) {
            this.cost = (Math.abs(this.getPosition().getRowIndex()-((MazeState)goalState).getPosition().getRowIndex())) +
                    (Math.abs(this.getPosition().getColumnIndex()-((MazeState)goalState).getPosition().getColumnIndex()));
        }
        else
            this.cost = 0;
    }
}
