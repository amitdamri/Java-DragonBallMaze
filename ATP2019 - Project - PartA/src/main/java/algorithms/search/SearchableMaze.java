package algorithms.search;

import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.util.ArrayList;

public class SearchableMaze implements ISearchable {

    private Maze maze;
    private MazeState startState;
    private MazeState goalState;

    public SearchableMaze(Maze maze) {
        if (maze != null) {
            this.maze = maze;
        }
        else{
            EmptyMazeGenerator emptyMaze = new EmptyMazeGenerator();
            this.maze = emptyMaze.generate(3,3);
        }
        this.startState = new MazeState(this.maze.getStartPosition(), null, 0);
        this.goalState = new MazeState(this.maze.getGoalPosition(), null, 0);
    }

    @Override
    public MazeState getStartState() {
        return startState;
    }

    @Override
    public MazeState getEndState() {
        return goalState;
    }

    @Override
    public ArrayList<AState> getAllPossibleStates(AState state) {

        ArrayList<AState> possibleStates = new ArrayList<>();
        if (state instanceof MazeState) {
            checkOptions(possibleStates, (MazeState) state);
        }
        return possibleStates;
    }

    private void checkOptions(ArrayList<AState> possibleStates, MazeState state) {
        if(null == state)
            return;
        int j = 1, k = 0;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 1: j = -1; k = 0; break;
                case 2: k = 1; j = 0; break;
                case 3: k = -1; j = 0; break;
            }
            if (!maze.checkIfWall(state.getPosition().getRowIndex() + j, state.getPosition().getColumnIndex() + k)) {

                Position newPosition = new Position(state.getPosition().getRowIndex() + j, state.getPosition().getColumnIndex() + k);
                possibleStates.add(new MazeState(newPosition, state, 10));

                int counter = 0;
                while (counter > 1) {
                    if (j == 0)
                        j = counter > 0 ? -1 : 1;
                    else
                        k = counter > 0 ? -1 : 1;
                    if (!maze.checkIfWall(state.getPosition().getRowIndex() + j, state.getPosition().getColumnIndex() + k)) {
                        newPosition = new Position(state.getPosition().getRowIndex() + j, state.getPosition().getColumnIndex() + k);
                        possibleStates.add(new MazeState(newPosition, state, 15));
                    }
                    ++counter;
                }
            }
        }
    }
}
