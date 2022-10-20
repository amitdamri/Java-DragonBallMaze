/**
 * This class represents a maze that was generated according to Prim's algorithm
 * inherits the methods from the abstract class AMazeGenerator
 *
 * @autor Dvir Simhon & Amit Damri
 * @version 1.0
 * @since 6.4.2019
 */
package algorithms.mazeGenerators;

import java.util.ArrayList;

public class MyMazeGenerator extends AMazeGenerator {

    //walls a list of all the walls inside the maze
    ArrayList<Position> walls;

    public MyMazeGenerator() {
        super("MyMazeGenerator");
    }

    @Override
    public Maze generate(int rows, int columns) {

        walls = new ArrayList<>();

        //checks if the given parameters are less than three - 3*3 is the minimum size of the maze
        if (rows < 3)
            rows = 3;
        if (columns < 3)
            columns = 3;

        //represents the maze while 1 is a wall and 0 is a passage
        int[][] tempMaze = new int[rows][columns];

        //marks all the maze with 1 (the maze has only walls)
        for (int i = 0; i < rows; i++) {
            java.util.Arrays.fill(tempMaze[i], 1);
        }

        //chooses randomly a start position
        Position start = new Position((int) (Math.random() * rows), (int) (Math.random() * columns));

        //Checks that if the size of the maze is 3*3 it doesnt start in the middle of it
        while (rows == 3 && columns == 3 && start.getRowIndex() == 1 && start.getColumnIndex() == 1) {
            start = new Position((int) (Math.random() * rows), (int) (Math.random() * columns));
        }

        //marks the start position with 0 - breaks the wall in there
        tempMaze[start.getRowIndex()][start.getColumnIndex()] = 0;

        //adds the walls around the start position in distance 2 to the wall's list
        addWalls(start, tempMaze);

        /*
         * while the list of the walls is not empty get one of the walls from the list, checks if one of his neighbors(in distance 2)
         * is a passage if yes break the current wall and the wall between them, adds the neighbors walls of this wall,
         * remove this wall from the list and move forward
         */
        Position wall = new Position(0, 0);

        while (!walls.isEmpty()) {
            wall = walls.get((int) (Math.random() * walls.size()));
            checkPassNeighbors(wall, tempMaze);
            walls.remove(wall);
        }

        //creates a new maze with start position and the goal position is the last wall the we broke
        Maze mazeToReturn = new Maze(rows, columns, start, wall);
        //sets the maze to be the new maze
        mazeToReturn.maze = tempMaze;
        //marks the goal position with 0 - a passage
        mazeToReturn.maze[wall.getRowIndex()][wall.getColumnIndex()] = 0;
        return mazeToReturn;
    }

    /**
     * checks if one of the position is distance 2 from this wall is a passage if yes it breaks the wall between them and makes
     * this position a passage
     * @param wall checks the neighbor's wall of the previous position
     * @param tempMaze an array of the maze
     */
    private void checkPassNeighbors(Position wall, int[][] tempMaze) {

        //represents the passage neighbors of the current all
        ArrayList<Position> passNeighbors = new ArrayList<>();
        //checks every neighbor of the wall in distance 2 if it is inside the maze and if it is
        // a passage. if yes adds this neighbor to the pass neighbors list
        if (wall.getRowIndex() < tempMaze.length - 2 && tempMaze[wall.getRowIndex() + 2][wall.getColumnIndex()] == 0)
            passNeighbors.add(new Position(wall.getRowIndex() + 2, wall.getColumnIndex()));
        if (wall.getRowIndex() > 1 && tempMaze[wall.getRowIndex() - 2][wall.getColumnIndex()] == 0)
            passNeighbors.add(new Position(wall.getRowIndex() - 2, wall.getColumnIndex()));
        if (wall.getColumnIndex() < tempMaze[0].length - 2 && tempMaze[wall.getRowIndex()][wall.getColumnIndex() + 2] == 0)
            passNeighbors.add(new Position(wall.getRowIndex(), wall.getColumnIndex() + 2));
        if (wall.getColumnIndex() > 1 && tempMaze[wall.getRowIndex()][wall.getColumnIndex() - 2] == 0)
            passNeighbors.add(new Position(wall.getRowIndex(), wall.getColumnIndex() - 2));

        //chooses one the pass neighbors randomly
        Position chosenNeighbor = passNeighbors.get((int) (Math.random() * passNeighbors.size()));
        //breaks the walls between them and the current wall
        breakWall(chosenNeighbor, wall, tempMaze);
    }

    /**
     * adds to the wall's list the neighbors with walls of the given position
     * @param positionAddToMaze a position that we need to add its neighbors walls
     * @param maze an array of the maze
     */
    private void addWalls(Position positionAddToMaze, int[][] maze) {

        //checks every neighbor in distance 2 of the positionAddToMaze. at first checks that this position is not on the bounds of the maze
        //in order to check the neighbor in distance 2, then it checks that the checkPosition(the new one) is a wall,
        //and checks that this neighbor's wall is not in wall's list. if everything is okay adds this wall to the list of walls
        Position checkPosition = new Position(positionAddToMaze.getRowIndex() - 2, positionAddToMaze.getColumnIndex());

        if (positionAddToMaze.getRowIndex() > 1 && !walls.contains(checkPosition) &&
                maze[positionAddToMaze.getRowIndex() - 2][positionAddToMaze.getColumnIndex()] != 0)
            walls.add(checkPosition);

        checkPosition = new Position(positionAddToMaze.getRowIndex() + 2, positionAddToMaze.getColumnIndex());
        if (positionAddToMaze.getRowIndex() < maze.length - 2 && !walls.contains(checkPosition) &&
                maze[positionAddToMaze.getRowIndex() + 2][positionAddToMaze.getColumnIndex()] != 0)
            walls.add(checkPosition);

        checkPosition = new Position(positionAddToMaze.getRowIndex(), positionAddToMaze.getColumnIndex() - 2);
        if (positionAddToMaze.getColumnIndex() > 1 && !walls.contains(checkPosition) &&
                maze[positionAddToMaze.getRowIndex()][positionAddToMaze.getColumnIndex() - 2] != 0)
            walls.add(checkPosition);

        checkPosition = new Position(positionAddToMaze.getRowIndex(), positionAddToMaze.getColumnIndex() + 2);
        if (positionAddToMaze.getColumnIndex() < maze[0].length - 2 && !walls.contains(checkPosition) &&
                maze[positionAddToMaze.getRowIndex()][positionAddToMaze.getColumnIndex() + 2] != 0)
            walls.add(checkPosition);
    }

    /**
     * Breaks the required walls and makes it a passage
     * @param chosenNeighbor the passage position of the wall that we are going to break
     * @param wall the neighbor wall that need the break his wall
     * @param tempMaze an array of the maze
     */
    private void breakWall(Position chosenNeighbor, Position wall, int[][] tempMaze) {

        //breaks the wall neighbors wall
        tempMaze[wall.getRowIndex()][wall.getColumnIndex()] = 0;

        //checks where is the neighbor that because of him we need to break the wall - this neighbor is a passage(the chosenNeighbor)
        //and after we found it we break the wall between both of them
        if (chosenNeighbor.getRowIndex() > wall.getRowIndex())
            tempMaze[wall.getRowIndex() + 1][wall.getColumnIndex()] = 0;
        else if (chosenNeighbor.getRowIndex() < wall.getRowIndex())
            tempMaze[wall.getRowIndex() - 1][wall.getColumnIndex()] = 0;
        else if (chosenNeighbor.getColumnIndex() > wall.getColumnIndex())
            tempMaze[wall.getRowIndex()][wall.getColumnIndex() + 1] = 0;
        else if (chosenNeighbor.getColumnIndex() < wall.getColumnIndex())
            tempMaze[wall.getRowIndex()][wall.getColumnIndex() - 1] = 0;

        //adds the neighbor's walls of the wall that we just broke
        addWalls(wall, tempMaze);
    }


}