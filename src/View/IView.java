package View;

import algorithms.mazeGenerators.Maze;

import java.util.List;

public interface IView {
    void displayMaze(Maze maze);
    void displayCharacter();
    void displaySolution(List<int[]> solution);
}

