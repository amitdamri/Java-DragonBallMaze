/**
 * This class represents a Maze
 * inherits the methods from the abstract class AMazeGenerator
 *
 * @autor Dvir Simhon & Amit Damri
 * @version 1.0
 * @param int[][] an array that represents the maze
 * @param startPosition the start of the maze
 * @param goalPosition the end of the maze(the goal)
 * @since 6.4.2019
 */
package algorithms.mazeGenerators;

import java.io.Serializable;
import java.util.Arrays;


public class Maze implements Serializable {

    //int[][] an array that represents the maze
    //startPosition the start of the maze
    //goalPosition the end of the maze(the goal)
    protected int[][] maze;
    private Position startPosition;
    private Position goalPosition;

    /**
     * Maze Constructor
     *
     * @param rows    of the new maze
     * @param columns of the new maze
     * @param start   the start position in the maze
     * @param goal    the goal position - the end of the maze
     */
    public Maze(int rows, int columns, Position start, Position goal) {
        this.maze = new int[rows][columns];
        this.startPosition = start;
        this.goalPosition = goal;
    }

    //Copy Constructor
    public Maze(Maze maze) {
        this(maze.maze.length, maze.maze[0].length, maze.getStartPosition(), maze.getGoalPosition());
        for (int i = 0; i < this.maze.length; i++) {
            for (int j = 0; j < this.maze[0].length; j++) {
                this.maze[i][j] = maze.maze[i][j];
            }
        }
    }

    /**
     * Creates maze from the byte array
     * @param byteMaze
     */
    public Maze(byte[] byteMaze) {
        if (null == byteMaze || byteMaze[1] == 1 || byteMaze[3] == 1) {
            this.maze = new int[3][3];
            this.startPosition = new Position(0, 0);
            this.goalPosition = new Position(2, 2);
        } else {
            //Copy of Array
            byte[] tempMaze = Arrays.copyOf(byteMaze,byteMaze.length);
            //build maze
            int indexToSearch = decompressOriginalMaze(tempMaze, 0, 0);
            //start position
            indexToSearch = decompressOriginalMaze(tempMaze, indexToSearch, 1);
            //end position
            indexToSearch = decompressOriginalMaze(tempMaze, indexToSearch, 2);
            //maze walls and paths
            decompressOriginalMazePaths(tempMaze, indexToSearch);
        }
    }

    /**
     * set the starting position
     * @param startingPosition
     */
    public void setStartPosition(Position startingPosition){
        this.startPosition = startingPosition;
    }
    /**
     * returns the start position of the maze
     *
     * @return the start position
     */
    public Position getStartPosition() {
        return startPosition;
    }

    /**
     * returns the goal position of the maze
     *
     * @return the goal position
     */
    public Position getGoalPosition() {
        return goalPosition;
    }

    /**
     * Prints the maze while S is the start, E is the goal position , 0 is a passage, 1 is a wall
     */
    public void print() {

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (startPosition.getRowIndex() == i && startPosition.getColumnIndex() == j)
                    System.out.print("S");
                else if (goalPosition.getRowIndex() == i && goalPosition.getColumnIndex() == j)
                    System.out.print("E");
                else
                    System.out.print(maze[i][j]);
            }
            System.out.print("\n");
        }
    }

    /**
     * Checks if there is a wall in this position
     *
     * @param row    of the maze
     * @param column of the maze
     * @return true if in this position there is a wall
     */
    public boolean checkIfWall(int row, int column) {

        //if not inside the maze
        if (row < 0 || row > maze.length - 1 || column < 0 || column > maze[0].length - 1)
            return true;
        else
            return (maze[row][column] == 1);

    }

    /**
     * transforms the maze to byte array
     * @return byte array of the maze
     */
    public byte[] toByteArray() {

        byte[] byteMaze = new byte[0];
        byteMaze = convertInfoToByte(byteMaze, maze.length, 1);
        byteMaze = convertInfoToByte(byteMaze, maze[0].length, 1);
        byteMaze = convertInfoToByte(byteMaze, getStartPosition().getRowIndex(), 1);
        byteMaze = convertInfoToByte(byteMaze, getStartPosition().getColumnIndex(), 1);
        byteMaze = convertInfoToByte(byteMaze, getGoalPosition().getRowIndex(), 1);
        byteMaze = convertInfoToByte(byteMaze, getGoalPosition().getColumnIndex(), 1);

        //counts the sequence of every number
        int checkInt = 1, counter = 0;
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j] == checkInt)
                    ++counter;
                else {
                    byteMaze = convertInfoToByte(byteMaze, counter, 0);
                    checkInt = (1 == checkInt ? 0 : 1);
                    counter = 1;
                }
            }
        }
        byteMaze = convertInfoToByte(byteMaze, counter, 0);

        return byteMaze;
    }

    /**
     * writes the maze to byte array while for every 1 and 0 writes each sequence number
     * @param byteMaze maze byte
     * @param info the sum of the sequence of the number
     * @param withSeparating if has 0 as seperator
     * @return byte array
     */
    private byte[] convertInfoToByte(byte[] byteMaze, int info, int withSeparating) {
        if (info == 0) {
            byteMaze = expandArrayByOne(byteMaze);
            byteMaze[byteMaze.length - 1] = 0;
        }
        while (info > 255) {
            byteMaze = expandArrayByOne(byteMaze);
            byteMaze[byteMaze.length - 1] = (byte) 255;
            info -= 255;
            if (withSeparating == 0) {
                byteMaze = expandArrayByOne(byteMaze);
                byteMaze[byteMaze.length - 1] = 0;
            }

        }
        if (info > 0) {
            byteMaze = expandArrayByOne(byteMaze);
            byteMaze[byteMaze.length - 1] = (byte) info;
        }
        if (withSeparating == 1) {
            byteMaze = expandArrayByOne(byteMaze);
            byteMaze[byteMaze.length - 1] = 0;
        }
        return byteMaze;
    }

    /**
     * expands array by one
     * @param mazeByte
     * @return byte array
     */
    private byte[] expandArrayByOne(byte[] mazeByte) {
        byte[] tempByteMaze = new byte[mazeByte.length + 1];
        for (int i = 0; i < mazeByte.length; i++) {
            tempByteMaze[i] = mazeByte[i];
        }
        return tempByteMaze;
    }

    /**
     * creates the start position goal position and maze size
     * @param byteMaze byte maze
     * @param indexToStartSearching where start from
     * @param indexOfWhatSearching row, column, or maze size
     * @return
     */
    private int decompressOriginalMaze(byte[] byteMaze, int indexToStartSearching, int indexOfWhatSearching) {

        int rows = 0, columns = 0, indexRowOrColumn = 0, isSeparator = 0; //says if the zero is a coordinate or separator;

        for (int i = indexToStartSearching; i < byteMaze.length; i++) {
            if (indexRowOrColumn > 1) {
                switch (indexOfWhatSearching) {
                    case 0:
                        this.maze = new int[rows][columns];
                        break;
                    case 1:
                        this.startPosition = new Position(rows, columns);
                        break;
                    case 2:
                        this.goalPosition = new Position(rows, columns);
                        break;
                }
                return i;
            }
            if (indexRowOrColumn == 0 && (byteMaze[i] != 0 || (byteMaze[i] == 0 && isSeparator == 0))) {
                isSeparator = 1;
                rows += (byteMaze[i] & 0xFF);
                continue;
            }
            if (indexRowOrColumn == 1 && (byteMaze[i] != 0 || (byteMaze[i] == 0 && isSeparator == 0))) {
                isSeparator = 1;
                columns += (byteMaze[i] & 0xFF);
                continue;
            }
            if (byteMaze[i] == 0 && isSeparator != 0) {
                ++indexRowOrColumn;
                isSeparator = 0;
            }
        }
        return 0;
    }

    /**
     * builds the maze - walls and paths
     * @param byteMaze maze in bytes
     * @param indexToStartBuildingMaze
     */
    private void decompressOriginalMazePaths(byte[] byteMaze, int indexToStartBuildingMaze) {
        int currentNumber = 1;
        boolean cellHasNumber = false;

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                while (!cellHasNumber) {
                    //writes 1 according to byte array counter
                    if (currentNumber == 1 && (byteMaze[indexToStartBuildingMaze] & 0xFF) > 0) {
                        cellHasNumber = true;
                        maze[i][j] = 1;
                        //decrements the counter
                        byteMaze[indexToStartBuildingMaze] = (byte) ((byteMaze[indexToStartBuildingMaze] & 0xFF) - 1);
                        continue;
                    }
                    //writes 0 according to byte array counter
                    if (currentNumber == 0 && (byteMaze[indexToStartBuildingMaze] & 0xFF) > 0) {
                        cellHasNumber = true;
                        maze[i][j] = 0;
                        //decrements the counter
                        byteMaze[indexToStartBuildingMaze] = (byte) ((byteMaze[indexToStartBuildingMaze] & 0xFF) - 1);
                        continue;
                    }
                    // if the counter is over move to the next index
                    if (byteMaze[indexToStartBuildingMaze] == 0) {
                        ++indexToStartBuildingMaze;
                        //end of the array
                        if (indexToStartBuildingMaze >= byteMaze.length)
                            break;
                        //change number to write
                        currentNumber = (currentNumber == 1 ? 0 : 1);
                    }
                }
                cellHasNumber = false;
            }
        }
    }

    /**
     * @return the maze array
     */
    public int[][] getMaze(){
        return this.maze;
    }

    /**
     * checks if mazes equals
     * @param obj maze
     * @return true or false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Maze) {
            boolean equals = true;
            for (int i = 0; i < this.maze.length; i++) {
                if (!Arrays.equals(maze[i],((Maze)obj).maze[i]))
                    equals = false;
            }
            return equals;
        }
        return false;
    }
}
