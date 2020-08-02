package View;

import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class MazeDisplayer extends ADisplayer  {

    public void setMaze(Maze maze) {
        super.setMazeToAll(maze);
        redraw();
    }


    public void redraw() {

        int[][] drawMaze = maze.getMaze();
        setCellHeight();
        setCellWidth();

        try {
            Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
            Image pathImage = new Image(new FileInputStream(ImageFileNamePath.get()));
            Image goalImage = new Image(new FileInputStream(ImageFileNameGoal.get()));
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());

            //Draw Maze
            for (int i = 0; i < drawMaze.length; i++) {
                for (int j = 0; j < drawMaze[i].length; j++) {
                    if (i == maze.getGoalPosition().getRowIndex() && j == maze.getGoalPosition().getColumnIndex())
                        gc.drawImage(goalImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                    else if (drawMaze[i][j] == 0)
                        gc.drawImage(pathImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                    else
                        gc.drawImage(wallImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    //region Properties
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNamePath = new SimpleStringProperty();
    private StringProperty ImageFileNameGoal = new SimpleStringProperty();

    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePath() {
        return ImageFileNamePath.get();
    }

    public void setImageFileNamePath(String imageFileNamePath) {
        this.ImageFileNamePath.set(imageFileNamePath);
    }

    public String getImageFileNameGoal() {
        return ImageFileNameGoal.get();
    }

    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.ImageFileNameGoal.set(imageFileNameGoal);
    }

    //canvas size
    @Override
    public boolean isResizable() {
        return true;
    }

    //endregion

}
