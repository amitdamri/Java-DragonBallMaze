/**
 * Interface of the classes that makes the maze
 * @autor Dvir Simhon & Amit Damri
 * @since 6.4.2019
 * @version 1.0
 */
package algorithms.mazeGenerators;


public interface IMazeGenerator {

    /**
     * generates a maze according to the required size
     * @param rows number of rows of the maze
     * @param columns number of columns of the maze
     * @return the required maze
     */
    Maze generate(int rows, int columns);

    /**
     * gets the size of the maze, executes the generate method and counts the time it took to make the maze
     * @param rows maze rows
     * @param columns maze columns
     * @return how long it took to create the maze
     */
    long measureAlgorithmTimeMillis(int rows, int columns);

    /**
     * @return generator name
     */
    String getGeneratorName();

}
