/**
 * This class represents a Position inside the maze
 * inherits the methods from the abstract class AMazeGenerator
 * @autor Dvir Simhon & Amit Damri
 * @since 6.4.2019
 * @version 1.0
 * @param column of the current position inside the maze
 * @param row of the current position inside the maze
 */
package algorithms.mazeGenerators;


import java.io.Serializable;

public class Position implements Serializable {

    //column of the current position inside the maze
    //row of the current position inside the maze
    private int column;
    private int row;

    //Default constructor
    public Position() {
        this.column = 0;
        this.row = 0;
    }

    //Constructor
    public Position(int row, int column) {
        this.column = column;
        this.row = row;
    }

    //Copy Constructor
    public Position(Position position) {
        if (position != null) {
            this.row = position.getRowIndex();
            this.column = position.getColumnIndex();
        }
        else {
            this.row = 0;
            this.column = 0;
        }
    }

    /**
     * return the row index of the required position
     * @return the row index
     */
    public int getRowIndex() {
        return row;
    }

    /**
     * return the column index of the required position
     * @return column index
     */
    public int getColumnIndex() {
        return column;
    }

    /**
     * @return String format of the position
     */
    @Override
    public String toString() {
        String printPosition = String.format("{%d,%d}", row, column);
        return printPosition;
    }

    /**
     *
     * @param obj position
     * @return if both positions are equals according to their row and column indexes
     */
    @Override
    public boolean equals(Object obj) {
        if (null != obj && obj instanceof Position) {
            return this.row == ((Position) obj).getRowIndex() && this.column == ((Position) obj).getColumnIndex();
        }
        return false;
    }

}
