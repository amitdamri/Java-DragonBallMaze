package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CharacterDisplayer extends ADisplayer {

    //region Properties
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacterBack = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacterSide1 = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacterSide2 = new SimpleStringProperty();


    private static int characterPositionRowPrev = -1;
    private static int characterPositionColumnPrev = -1;

    private static Image characterImage = null;

    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
        redraw();
    }

    public void redraw() {
        setCellHeight();
        setCellWidth();
        try {
            if (characterPositionRowPrev == characterPositionRow && characterPositionColumnPrev == characterPositionColumn) {
            } else if (characterPositionRowPrev == -1 || characterPositionRowPrev < characterPositionRow)
                characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
            else if (characterPositionRowPrev > characterPositionRow)
                characterImage = new Image(new FileInputStream(ImageFileNameCharacterBack.get()));
            else if (characterPositionColumn < characterPositionColumnPrev && characterPositionRowPrev == characterPositionRow)
                characterImage = new Image(new FileInputStream(ImageFileNameCharacterSide2.get()));
            else
                characterImage = new Image(new FileInputStream(ImageFileNameCharacterSide1.get()));

            characterPositionColumnPrev = characterPositionColumn;
            characterPositionRowPrev = characterPositionRow;

            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());

            //Draw Character
            gc.drawImage(characterImage, characterPositionColumn * cellWidth, characterPositionRow * cellHeight, cellWidth, cellHeight);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }

    public String getImageFileNameCharacterBack() {
        return ImageFileNameCharacterBack.get();
    }

    public void setImageFileNameCharacterBack(String imageFileNameCharacterBack) {
        this.ImageFileNameCharacterBack.set(imageFileNameCharacterBack);
    }

    public String getImageFileNameCharacterSide1() {
        return ImageFileNameCharacterSide2.get();
    }

    public void setImageFileNameCharacterSide1(String imageFileNameCharacterSide1) {
        this.ImageFileNameCharacterSide1.set(imageFileNameCharacterSide1);
    }

    public String getImageFileNameCharacterSide2() {
        return ImageFileNameCharacterSide2.get();
    }

    public void setImageFileNameCharacterSide2(String imageFileNameCharacterSide2) {
        this.ImageFileNameCharacterSide2.set(imageFileNameCharacterSide2);
    }


    @Override
    public boolean isResizable() {
        return true;
    }


    public boolean playerAtGoalPosition() {
        return characterPositionColumn == maze.getGoalPosition().getColumnIndex()
                && characterPositionRow == maze.getGoalPosition().getRowIndex();
    }
}
