/**
 * This abstract class represents the maze generators that we have created.
 * all the generators have the same methods
 *
 * @autor Dvir Simhon & Amit Damri
 * @version 1.0
 * @since 6.4.2019
 */
package algorithms.mazeGenerators;

public abstract class AMazeGenerator implements IMazeGenerator {

    //abstract method every inherited class will implement this method
    @Override
    public abstract Maze generate(int rows, int columns);

    String generatorName;

    /**
     * gets the size of the maze, executes the generate method and counts the time it took to make the maze
     * @param rows maze rows
     * @param columns maze columns
     * @return how long it took to create the maze
     */

    public AMazeGenerator(String name) {
        generatorName = name;
    }

    public String getGeneratorName() {
        return generatorName;
    }

    @Override
    public long measureAlgorithmTimeMillis(int rows, int columns) {
        long startTime = System.currentTimeMillis();
        generate(rows, columns);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
