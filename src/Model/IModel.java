package Model;

import algorithms.mazeGenerators.Maze;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by Aviadjo on 6/14/2017.
 */
public interface IModel {
    //Maze
    void generateMaze(int width, int height);
    Maze getMaze();
    void solveMaze();
    List<int[]> getSolution();
    void saveMaze(Stage gameStage);
    void loadMaze(Stage gameStage);
    void loadMazeFromMainMenu(Stage gameStage);

    //Properties
    void chooseDifficultLevel(String value);
    void chooseSolutionWay(String value);

    //Character
    void moveCharacter(KeyCode movement);
    int getCharacterPositionRow();
    int getCharacterPositionColumn();

    //close Application
    void close();
}