package View;

import algorithms.mazeGenerators.Maze;
import javafx.scene.canvas.Canvas;

public class ADisplayer extends Canvas {

    protected static  Maze maze;
    protected int characterPositionRow = 0;
    protected int characterPositionColumn = 0;
    protected double cellHeight;
    protected double cellWidth;


    public void setCellHeight(){
        cellHeight = getHeight() / maze.getMaze().length;
    }

    public void setCellWidth(){
        cellWidth = getWidth() / maze.getMaze()[0].length;
    }

    public void setMazeToAll(Maze maze){
        this.maze = maze;
    }

    public void setCanvasWidthAndHeight(double width,double height){
        setHeight(height);
        setWidth(width);
    }

    public double getCellHeight(){
        return cellHeight;
    }

    public double getCellWidth(){
        return cellWidth;
    }
}
