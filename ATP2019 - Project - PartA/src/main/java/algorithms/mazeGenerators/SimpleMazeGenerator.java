/**
 * This class represents a simple maze.
 * inherits the methods from the abstract class AMazeGenerator
 * @autor Dvir Simhon & Amit Damri
 * @since 6.4.2019
 * @version 1.0
 */
package algorithms.mazeGenerators;

public class SimpleMazeGenerator extends AMazeGenerator {

    public SimpleMazeGenerator(){
        super("SimpleMazeGenerator");
    }

    /**
     * Generates a simple maze with random walls and solution after it chooses the goal and start position randomly
     * @param rows of the maze
     * @param columns of the maze
     * @return a simple maze with walls
     */
    @Override
    public Maze generate(int rows, int columns) {
        //checks if the given parameters are less than three - 3*3 is the minimum size of the maze
        if (rows<3)
            rows=3;
        if (columns<3)
            columns =3;

        //represents the maze while 1 is a wall and 0 is a passage
        int[][] tempMaze = new int[rows][columns];

        //choose random positions to be the goal and the start position(inside the maze bounds)
        Position start = new Position((int) (Math.random() * rows), (int) (Math.random() * columns));
        Position goal = new Position((int) (Math.random() * rows), (int) (Math.random() * columns));

        //checks if the start position equals to the goal position - if yes choose other goal position
        while (goal.equals(start)) {
            goal = new Position((int) (Math.random() * rows), (int) (Math.random() * columns));
        }
        //creates a maze with the required size and the required start and goal position
        Maze maze = new Maze(rows, columns, start, goal);

        //makes the solution of the maze - marks the path with 2
        tempMaze = solvedSampleMaze(tempMaze, start, goal);

        //checks if the current position is not in the path and chooses randomly if this position is a wall or not
        for (int i = 0; i < maze.maze.length; i++) {
            for (int j = 0; j < maze.maze[0].length; j++) {
                if (tempMaze[i][j] != 2)
                    maze.maze[i][j] = (int) (Math.random() * 2);
            }
        }
        //returns the simple maze
        return maze;
    }

    /**
     * this function makes sure that the maze has a solution and marks the solution path with 2
     * @param tempMaze an empty array that represents the maze
     * @param start position
     * @param goal position
     * @return an array that represents the maze with a solution path that signposted with 2
     */
    private int[][] solvedSampleMaze(int[][] tempMaze, Position start, Position goal) {
       //checks if the maze is solved
        boolean solved = false;

        //the maze start position
        int row = start.getRowIndex(), column = start.getColumnIndex();

        /*
        * Goes from the start position in the direction of the goal position until it makes it and gets to the goal position.
        * every position in the path to the solution it marks with 2
        */
        while (!solved) {
            if (row < goal.getRowIndex()) {
                tempMaze[row][column] = 2;
                ++row;
            } else if (row > goal.getRowIndex()) {
                tempMaze[row][column] = 2;
                --row;
            }
            if (column < goal.getColumnIndex()) {
                tempMaze[row][column] = 2;
                ++column;
            } else if (column > goal.getColumnIndex()) {
                tempMaze[row][column] = 2;
                --column;
            }
            //gets a solution - finds the goal position
            if (row == goal.getRowIndex() && column == goal.getColumnIndex()) {
                tempMaze[row][column] = 2;
                solved = true;
            }
        }
        return tempMaze;
    }
}
