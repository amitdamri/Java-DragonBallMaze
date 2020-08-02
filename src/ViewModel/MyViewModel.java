package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class MyViewModel extends Observable implements Observer {

    private IModel model;

    public MyViewModel(IModel model){
        this.model = model;
    }

    //<editor-fold desc="Take care Observable">
    @Override
    public void update(Observable o, Object arg) {
        if (o==model){
            //Notify my observer (View) that I have changed
            setChanged();
            notifyObservers(arg);
        }
    }
    //</editor-fold>

    //<editor-fold desc="ViewModel Functionality">
    public void generateMaze(int width, int height){
        model.generateMaze(width, height);
    }

    public void solveMaze(){
        model.solveMaze();
    }

    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement);
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public Maze getMaze() {
        return model.getMaze();
    }

    public List<int[]> getSolution(){
        return model.getSolution();
    }

    public int getCharacterPositionRow() {
        //return characterPositionRowIndex;
        return model.getCharacterPositionRow();
    }

    public int getCharacterPositionColumn() {
        //return characterPositionColumnIndex;
        return model.getCharacterPositionColumn();
    }

    public void saveMaze(Stage gameStage){
        model.saveMaze(gameStage);
    }

    public void loadMaze(Stage gameStage){
        model.loadMaze(gameStage);
    }

    public void chooseDifficultLevel(String value) { model.chooseDifficultLevel(value); }

    public void closeApp(){
        model.close();
    }

    public void chooseSolutionWay(String value) { model.chooseSolutionWay(value);}

    public void loadMazeFromMainMenu(Stage gameStage){
        model.loadMazeFromMainMenu(gameStage);
    }
    //</editor-fold>
}
