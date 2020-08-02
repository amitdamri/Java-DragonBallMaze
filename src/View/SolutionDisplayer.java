package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class SolutionDisplayer extends ADisplayer {

    private List<int[]> mazeSolution;
    private StringProperty ImageFileNameSolution = new SimpleStringProperty();
    GraphicsContext gc = getGraphicsContext2D();
    private boolean isDrawn = false;

    public void setSolution(List<int[]> solution) {
        this.mazeSolution = solution;
        redraw();
    }


    public void redraw() {
        isDrawn = true;
        try {
            setCellHeight();
            setCellWidth();
            Image solutionImage = new Image(new FileInputStream(ImageFileNameSolution.get()));

            gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());

            for (int[] rowColumn : mazeSolution) {
                if (!(rowColumn[0] == maze.getGoalPosition().getRowIndex() && rowColumn[1] == maze.getGoalPosition().getColumnIndex())
                        && !(rowColumn[0] == characterPositionRow && rowColumn[1] == characterPositionColumn))
                    gc.drawImage(solutionImage, rowColumn[1] * cellWidth, rowColumn[0] * cellHeight, cellWidth, cellHeight);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public String getImageFileNameSolution() {
        return ImageFileNameSolution.get();
    }


    public void setImageFileNameSolution(String imageFileNameSolution) {
        this.ImageFileNameSolution.set(imageFileNameSolution);
    }

    //canvas size
    @Override
    public boolean isResizable() {
        return true;
    }

    public boolean isDrawn() {
        return isDrawn;
    }

    public void clearSolution() {
        this.gc.clearRect(0, 0, getWidth(), getHeight());
        isDrawn = false;
    }

}
