/**
 * This class represents an empty maze.
 * inherits the methods from the abstract class AMazeGenerator
 * @autor Dvir Simhon & Amit Damri
 * @since 6.4.2019
 * @version 1.0
 */
package algorithms.mazeGenerators;

public class EmptyMazeGenerator extends AMazeGenerator {

    /**
     * Generates an empty maze of size rows*columns without walls
     * @param rows of the maze
     * @param columns of the maze
     * @return empty maze without walls
     */

    public EmptyMazeGenerator(){
        super("EmptyMazeGenerator");
    }
    @Override
    public Maze generate(int rows, int columns) {
        return new Maze(rows, columns, new Position(0, 0), new Position(rows - 1, columns - 1));
    }
}
