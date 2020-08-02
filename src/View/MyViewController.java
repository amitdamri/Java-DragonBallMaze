package View;

import algorithms.mazeGenerators.Maze;


import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MyViewController implements Observer, IView {

    @FXML
    private static MyViewModel viewModel;
    public MazeDisplayer mazeDisplayer;
    public CharacterDisplayer characterDisplayer;
    public SolutionDisplayer solutionDisplayer;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    @FXML
    private ChoiceBox<String> choiceBoxDifficultLevel;
    @FXML
    private ChoiceBox<String> choiceBoxSolutionWay;
    @FXML
    private Pane MazePane;
    @FXML
    private Button btn_solveMaze;
    @FXML
    private Button btn_saveMaze;
    @FXML
    private MenuItem btn_file_save;


    //Canvas sizes
    private static double currentCanvasWidth = 600.0;
    private static double currentCanvasHeight = 600.0;

    //Stages
    private static Stage mainStage;
    private static Stage subStage;
    private static Scene mainScene;

    //Properties - For Binding
    public StringProperty characterPositionRow = new SimpleStringProperty("1");
    public StringProperty characterPositionColumn = new SimpleStringProperty("1");

    //media audio
    private static Media audioFile = new Media(ClassLoader.getSystemResource("song.mp3").toString());
    private static MediaPlayer mediaPlayer = new MediaPlayer(audioFile);

    //down keys
    private static boolean isCtrlDown = false;

    public void initialize(MyViewModel viewModel, Stage mainStage, Scene mainScene) {
        this.viewModel = viewModel;
        this.mainScene = mainScene;
        this.mainStage = mainStage;
        setResizeEvent();
        //starts audio
        if (!mediaPlayer.isAutoPlay()) {
            audioFile = new Media(ClassLoader.getSystemResource("song.mp3").toString());
            mediaPlayer = new MediaPlayer(audioFile);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setVolume(0.3);
            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        }
    }

    private void bindProperties() {
        lbl_rowsNum.textProperty().bind(this.characterPositionRow);
        lbl_columnsNum.textProperty().bind(this.characterPositionColumn);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            if (arg instanceof Maze) {
                displayMaze(viewModel.getMaze());
                displayCharacter();
            } else if (arg instanceof Maze[]) {
                try {
                    PlayGame();
                    displayMaze(viewModel.getMaze());
                    displayCharacter();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (arg instanceof Position) {
                displayCharacter();
                if (solutionDisplayer.isDrawn()) {
                    displaySolution(viewModel.getSolution());
                }
                if (characterDisplayer.playerAtGoalPosition())
                    showWinningScreen();
            } else {
                displaySolution(viewModel.getSolution());
            }
        }
    }

    @FXML
    public void PlayGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        BorderPane borderPane = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        Scene scene = new Scene(borderPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("View.css").toExternalForm());
        MyViewController newView = fxmlLoader.getController();
        this.mazeDisplayer = newView.mazeDisplayer;
        this.characterDisplayer = newView.characterDisplayer;
        this.solutionDisplayer = newView.solutionDisplayer;
        reloadController(mainStage, scene, newView);
        newView.setResizeEvent();
        newView.bindProperties();
    }


    @FXML
    public void ReturnToMenu() throws IOException {
        if (subStage != null)
            subStage.close();
        FXMLLoader fxmlLoader = new FXMLLoader();
        BorderPane borderPane = fxmlLoader.load(getClass().getResource("ViewStartGame.fxml").openStream());
        Scene scene = new Scene(borderPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("ViewStartGame.css").toExternalForm());
        MyViewController newView = fxmlLoader.getController();
        reloadController(mainStage, scene, newView);
    }


    @Override
    public void displayMaze(Maze maze) {
        mazeDisplayer.setCanvasWidthAndHeight(currentCanvasWidth, currentCanvasHeight);
        mazeDisplayer.setMaze(maze);
    }

    @Override
    public void displayCharacter() {
        characterDisplayer.setCanvasWidthAndHeight(currentCanvasWidth, currentCanvasHeight);

        int characterPositionRow = viewModel.getCharacterPositionRow();
        int characterPositionColumn = viewModel.getCharacterPositionColumn();
        characterDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
        this.characterPositionRow.set(characterPositionRow + "");
        this.characterPositionColumn.set(characterPositionColumn + "");
    }

    public void generateMaze() throws IOException {
        if (!txtfld_columnsNum.getText().matches("\\d*") || !txtfld_rowsNum.getText().matches("\\d*")
                || Integer.valueOf(txtfld_rowsNum.getText()) > 1000 || Integer.valueOf(txtfld_columnsNum.getText()) > 1000) {
            showError("Please insert only numbers (less than 1000). Please try again");
        } else {
            int heigth = Integer.valueOf(txtfld_rowsNum.getText());
            int width = Integer.valueOf(txtfld_columnsNum.getText());
            subStage.close();
            PlayGame();
            viewModel.generateMaze(width, heigth);
        }
    }

    public void solveMaze() {
        //showAlert("Solving maze..");
        viewModel.solveMaze();
    }

    @Override
    public void displaySolution(List<int[]> solution) {
        solutionDisplayer.setCanvasWidthAndHeight(currentCanvasWidth, currentCanvasHeight);

        solutionDisplayer.setSolution(solution);
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }
    private void showError(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(alertMessage);
        alert.show();
    }

    /**
     *
     * @param keyEvent
     */
    public void KeyPressed(KeyEvent keyEvent) {
        if (mazeDisplayer != null) {
            if (keyEvent.getCode().equals(KeyCode.CONTROL))
                isCtrlDown = true;
            else
                viewModel.moveCharacter(keyEvent.getCode());
        }
        keyEvent.consume();
    }

    /**
     * when key released changes the isCtrlDown value to false
     * @param keyEvent
     */
    //if ctrl doesnt press anymore
    public void handleKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.CONTROL))
            isCtrlDown = false;
        keyEvent.consume();
    }

    /**
     * resizes the maze,character and solution according to the windows size
     */
    public void setResizeEvent() {
        this.mainStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            currentCanvasWidth = newValue.doubleValue() - 200;
            if (mazeDisplayer != null && characterDisplayer != null && solutionDisplayer != null) {
                mazeDisplayer.setWidth(newValue.doubleValue() - 200);
                characterDisplayer.setWidth(newValue.doubleValue() - 200);
                solutionDisplayer.setWidth(newValue.doubleValue() - 200);
                mazeDisplayer.redraw();
                characterDisplayer.redraw();
                if (solutionDisplayer.isDrawn())
                    solutionDisplayer.redraw();
            }
        });
        this.mainStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            currentCanvasHeight = newValue.doubleValue() - 100;
            if (mazeDisplayer != null && characterDisplayer != null && solutionDisplayer != null) {
                mazeDisplayer.setHeight(newValue.doubleValue() - 100);
                characterDisplayer.setHeight(newValue.doubleValue() - 100);
                solutionDisplayer.setHeight(newValue.doubleValue() - 100);
                mazeDisplayer.redraw();
                characterDisplayer.redraw();
                if (solutionDisplayer.isDrawn())
                    solutionDisplayer.redraw();
            }
        });
    }

    /**
     * opens the about screen
     * @param actionEvent
     */
    public void About(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("AboutController");
            stage.initStyle(StageStyle.UNDECORATED);
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(getClass().getResource("About.css").toExternalForm());
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            showingSubStage(stage, scene, fxmlLoader);
        } catch (Exception e) {

        }
    }

    /**
     * opens the help screen
     * @param actionEvent
     */
    public void Help(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("GameHelpController");
            stage.initStyle(StageStyle.UNDECORATED);
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Help.fxml").openStream());
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(getClass().getResource("About.css").toExternalForm());
            showingSubStage(stage, scene, fxmlLoader);
        } catch (Exception e) {

        }
    }

    /**
     * requests focus to the maze screen
     * @param mouseEvent
     */
    public void mouseClicked(MouseEvent mouseEvent) {
        this.mazeDisplayer.requestFocus();
    }

    /**
     * reloads the controller in order to use it in the next scene
     * @param stageToShow
     * @param scene
     * @param newView
     */
    private void reloadController(Stage stageToShow, Scene scene, MyViewController newView) {
        stageToShow.setScene(scene);
        newView.initialize(viewModel, mainStage, scene);
        viewModel.deleteObservers();
        viewModel.addObserver(newView);
    }

    /**
     * saves the maze to the local disk
     */
    public void saveMaze() {
        viewModel.saveMaze(mainStage);
    }

    /**
     * loads maze from the gaming screen
     */
    public void loadMaze() {
        //if solution is drawn clear it
        viewModel.loadMaze(mainStage);
        if (solutionDisplayer != null && solutionDisplayer.isDrawn())
            solutionDisplayer.clearSolution();
    }

    /**
     * loads maze from the main menu
     */
    public void loadMazeFromMainMenu() {
        viewModel.loadMazeFromMainMenu(mainStage);
    }


    /**
     * close the entire application
     */
    public void closeApp() {
        viewModel.closeApp();
        mainStage.close();
    }

    /**
     * changes the music of the game
     */
    public static void changeMusic() {
        mediaPlayer.stop();
        audioFile = new Media(ClassLoader.getSystemResource("solving.mp3").toString());
        mediaPlayer = new MediaPlayer(audioFile);
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        mediaPlayer.setVolume(0.3);
        mediaPlayer.play();
    }

    /**
     * opens the maze size window
     * @throws IOException
     */
    @FXML
    public void MazeSize() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Maze Size");
        FXMLLoader fxmlLoader = new FXMLLoader();
        AnchorPane anchorPane = fxmlLoader.load(getClass().getResource("MazeSize.fxml").openStream());
        Scene scene = new Scene(anchorPane, 330, 216);
        stage.initStyle(StageStyle.UNDECORATED);
        //scene.getStylesheets().add(getClass().getResource("View.css").toExternalForm());
        stage.setResizable(false);
        showingSubStage(stage, scene, fxmlLoader);
    }

    /**
     * open the properties window of the game
     */
    public void openProperties() {
        try {
            Stage stage = new Stage();
            stage.setTitle("Maze Properties");
            FXMLLoader fxmlLoader = new FXMLLoader();
            TabPane tabPane = fxmlLoader.load(getClass().getResource("Properties.fxml").openStream());
            Scene scene = new Scene(tabPane, 600, 646);
            //choiceBoxDifficultLevel.setValue(choiceBoxDifficult);
            //cant maximize
            stage.resizableProperty().setValue(Boolean.FALSE);
            //scene.getStylesheets().add(getClass().getResource("View.css").toExternalForm());
            showingSubStage(stage, scene, fxmlLoader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * shous the new sub stage
     * @param newStage
     * @param newScene
     * @param fxmlLoader
     */
    private void showingSubStage(Stage newStage, Scene newScene, FXMLLoader fxmlLoader) {
        newStage.setScene(newScene);
        newStage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
        subStage = newStage;
        subStage.show();
    }

    /**
     * closes the window
     */
    public void closeWindow() {
        if (subStage != null)
            subStage.close();
    }

    /**
     * changes the configuration according to the user choice
     */
    public void choiceBoxChoose() {
        try {
            String difficultValue = choiceBoxDifficultLevel.getSelectionModel().getSelectedItem();
            choiceBoxDifficultLevel.setValue(difficultValue);
            viewModel.chooseDifficultLevel(difficultValue);
            String algorithmValue = choiceBoxSolutionWay.getSelectionModel().getSelectedItem();
            choiceBoxSolutionWay.setValue(algorithmValue);
            viewModel.chooseSolutionWay(algorithmValue);
            closeWindow();
            MazeSize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * starts a new match
     * @throws IOException
     */
    public void Rematch() throws IOException {
        subStage.close();
        mediaPlayer.stop();
        Stage stage = new Stage();
        stage.setTitle("Maze Size");
        FXMLLoader fxmlLoader = new FXMLLoader();
        stage.initStyle(StageStyle.UNDECORATED);
        AnchorPane anchorPane = fxmlLoader.load(getClass().getResource("MazeSizeAfterWinning.fxml").openStream());
        Scene scene = new Scene(anchorPane, 330, 216);
        //scene.getStylesheets().add(getClass().getResource("View.css").toExternalForm());
        stage.setResizable(false);
        showingSubStage(stage, scene, fxmlLoader);

    }

    /**
     * Returns to main menu after winning the game
     * @throws IOException
     */
    public void ReturnToMainMenuFromWinningScreen() throws IOException {
        mediaPlayer.stop();
        subStage.close();
        initialize(viewModel, mainStage, mainScene);
        ReturnToMenu();
    }

    /**
     * shows the winning screen after the user solves the game
     */
    private void showWinningScreen() {
        MyViewController.changeMusic();
        try {
            Stage winningStage = new Stage();
            winningStage.setTitle("Winning Screen");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("WinningScreen.fxml").openStream());
            Scene scene = new Scene(root, 500, 400);
            //hide the top bar
            winningStage.initStyle(StageStyle.UNDECORATED);
            scene.getStylesheets().add(getClass().getResource("WinningScreen.css").toExternalForm());
            btn_solveMaze.setDisable(true);
            btn_saveMaze.setDisable(true);
            btn_file_save.setDisable(true);
            showingSubStage(winningStage, scene, fxmlLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Zoom in and out the displayer
     */
    public void zoom() {

        MazePane.setOnScroll(
                event -> {
                    double zoomFactor = 1.05;
                    double deltaY = event.getDeltaY();

                    if (deltaY < 0) {
                        zoomFactor = 0.95;
                    }
                    if (isCtrlDown) {
                        if (MazePane.getScaleX() * zoomFactor > 0.95 && MazePane.getScaleX() * zoomFactor < 4
                                && MazePane.getScaleY() * zoomFactor > 0.95 && MazePane.getScaleY() * zoomFactor < 4) {
                            MazePane.setScaleX(MazePane.getScaleX() * zoomFactor);
                            MazePane.setScaleY(MazePane.getScaleY() * zoomFactor);
                        }
                        event.consume();
                    }
                });
    }

    /*****************************************************************
     *******************************Bonus*****************************
     *****************************************************************/
    //Fields for the drag
    private double xDragStartPosition, yDragStartPosition;
    private boolean isDragDetected = false;

    // Add mouse event handlers for the drag character

    /**
     * drag the character on the screen according to the mouse position
     * @param event CliclmouseEvent
     */
    public void dragCharacter(MouseEvent event) {


        MazePane.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                MazePane.setMouseTransparent(true);
                //System.out.println("Event on Source: mouse pressed");
                event.setDragDetect(true);
                xDragStartPosition = event.getX();
                yDragStartPosition = event.getY();
            }
        });

        MazePane.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                MazePane.setMouseTransparent(false);
               // System.out.println("Event on Source: mouse released");
                double angle = getAngle(event.getX(), event.getY());
               // System.out.println(angle);
                if (isDragDetected) {
                    isDragDetected=false;
                    //NUMPAD8
                    if (angle >= 330 || angle <= 30)
                        viewModel.moveCharacter(KeyCode.NUMPAD8);
                        //NUMPAD9
                    else if (angle < 60)
                        viewModel.moveCharacter(KeyCode.NUMPAD9);
                        //NUMPAD6
                    else if (angle <= 120)
                        viewModel.moveCharacter(KeyCode.NUMPAD6);
                        //NUMPAD3
                    else if (angle < 150)
                        viewModel.moveCharacter(KeyCode.NUMPAD3);
                        //NUMPAD2
                    else if (angle <= 210)
                        viewModel.moveCharacter(KeyCode.NUMPAD2);
                        //NUMPAD1
                    else if (angle < 240)
                        viewModel.moveCharacter(KeyCode.NUMPAD1);
                        //NUMPAD4
                    else if (angle <= 300)
                        viewModel.moveCharacter(KeyCode.NUMPAD4);
                        //NUMPAD7
                    else if (angle < 330)
                        viewModel.moveCharacter(KeyCode.NUMPAD7);

                }
            }
        });

        MazePane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
               // System.out.println("Event on Source: mouse dragged");
                isDragDetected = true;
            }
        });

        MazePane.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                MazePane.startFullDrag();
                //System.out.println("Event on Source: drag detected");
            }
        });


    }

    /**
     * caclulat the angle for the character drag
     * @param xTargetPt x position of the mouse
     * @param yTargetPt y position of the mouse
     * @return angle
     */
    private double getAngle(double xTargetPt, double yTargetPt) {
        // calculate the angle theta from the deltaY and deltaX values
        // (atan2 returns radians values from [-PI,PI])
        // 0 currently points EAST.
        // NOTE: By preserving Y and X param order to atan2,  we are expecting
        // a CLOCKWISE angle direction.
        double theta = Math.atan2(yTargetPt - yDragStartPosition, xTargetPt - xDragStartPosition);

        // rotate the theta angle clockwise by 90 degrees
        // (this makes 0 point NORTH)
        // NOTE: adding to an angle rotates it clockwise.
        // subtracting would rotate it counter-clockwise
        theta += Math.PI / 2.0;

        // convert from radians to degrees
        // this will give you an angle from [0->270],[-180,0]
        double angle = Math.toDegrees(theta);

        // convert to positive range [0-360)
        // since we want to prevent negative angles, adjust them now.
        // we can assume that atan2 will not return a negative value
        // greater than one partial rotation
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    /**
     * Related to sound on and off button
     * Stop background music and play again from where it has stopped
     */
    public void sound() {
        boolean playing = mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING);
        if (playing)
            mediaPlayer.pause();
        else
            mediaPlayer.play();
    }

    //endregion

}


