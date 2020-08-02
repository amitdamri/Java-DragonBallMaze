package Model;

import Client.*;
import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.*;
import algorithms.search.*;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class MyModel extends Observable implements IModel {

    public MyModel() {
    }

    //<editor-fold desc="Servers">
    Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
    Server solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());

    public void startServers() {
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();
    }

    public void stopServers() {
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
    }
    //</editor-fold>

    //<editor-fold desc="Character">
    private int characterPositionRow = 0;
    private int characterPositionColumn = 0;
    //</editor-fold>

    //<editor-fold desc="Maze">
    private Maze maze;
    private Solution mazeSolution;
    //</editor-fold>

    //<editor-fold desc="Getters">
    @Override
    public Maze getMaze() {
        return maze;
    }

    @Override
    public List<int[]> getSolution() {
        List<AState> solutionPath = mazeSolution.getSolutionPath();
        List<int[]> solutionList = new ArrayList<>();
        for (AState temp : solutionPath) {
            String tempRowColumn = temp.toString().replace("{", "");
            tempRowColumn = tempRowColumn.replace("}", "");
            String[] pathArray = tempRowColumn.split(",");
            int[] rowColumn = new int[]{Integer.parseInt(pathArray[0]), Integer.parseInt(pathArray[1])};
            solutionList.add(rowColumn);
        }
        return solutionList;
    }

    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    @Override
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    @Override
    public void close() {
        stopServers();
    }
    //</editor-fold>

    //<editor-fold desc="Model Functionality">


    @Override
    public void generateMaze(int width, int height) {
        //Generate maze
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{height, width};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[1000000]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        characterPositionRow = maze.getStartPosition().getRowIndex();
        characterPositionColumn = maze.getStartPosition().getColumnIndex();
        setChanged(); //Raise a flag that I have changed
        notifyObservers(maze); //Wave the flag so the observers will notice
    }


    @Override
    public void solveMaze() {
        //update position
        maze.setStartPosition(new Position(characterPositionRow, characterPositionColumn));
        //solve maze
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze);
                        toServer.flush();
                        mazeSolution = (Solution) fromServer.readObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        setChanged(); //Raise a flag that I have changed
        notifyObservers(mazeSolution); //Wave the flag so the observers will notice
    }


    @Override
    public void moveCharacter(KeyCode movement) {
        int[][] tempMaze = maze.getMaze();
        switch (movement) {
            case NUMPAD8:
                if (characterPositionRow - 1 >= 0 && tempMaze[characterPositionRow - 1][characterPositionColumn] != 1)
                    characterPositionRow--;
                break;
            case UP:
                if (characterPositionRow - 1 >= 0 && tempMaze[characterPositionRow - 1][characterPositionColumn] != 1)
                    characterPositionRow--;
                break;
            case NUMPAD2:
                if (characterPositionRow + 1 < tempMaze.length && tempMaze[characterPositionRow + 1][characterPositionColumn] != 1)
                    characterPositionRow++;
                break;
            case DOWN:
                if (characterPositionRow + 1 < tempMaze.length && tempMaze[characterPositionRow + 1][characterPositionColumn] != 1)
                    characterPositionRow++;
                break;
            case NUMPAD6:
                if (characterPositionColumn + 1 < tempMaze[0].length && tempMaze[characterPositionRow][characterPositionColumn + 1] != 1)
                    characterPositionColumn++;
                break;
            case RIGHT:
                if (characterPositionColumn + 1 < tempMaze[0].length && tempMaze[characterPositionRow][characterPositionColumn + 1] != 1)
                    characterPositionColumn++;
                break;
            case NUMPAD4:
                if (characterPositionColumn - 1 >= 0 && tempMaze[characterPositionRow][characterPositionColumn - 1] != 1)
                    characterPositionColumn--;
                break;
            case LEFT:
                if (characterPositionColumn - 1 >= 0 && tempMaze[characterPositionRow][characterPositionColumn - 1] != 1)
                    characterPositionColumn--;
                break;
            case NUMPAD9:
                if (characterPositionRow - 1 >= 0 && characterPositionColumn + 1 < tempMaze[0].length
                        && tempMaze[characterPositionRow - 1][characterPositionColumn + 1] != 1) {
                    characterPositionColumn++;
                    characterPositionRow--;
                }
                break;
            case NUMPAD7:
                if (characterPositionRow - 1 >= 0 && characterPositionColumn - 1 >= 0
                        && tempMaze[characterPositionRow - 1][characterPositionColumn - 1] != 1) {
                    characterPositionColumn--;
                    characterPositionRow--;

                }
                break;
            case NUMPAD3:
                if (characterPositionRow + 1 < tempMaze.length && characterPositionColumn + 1 < tempMaze[0].length
                        && tempMaze[characterPositionRow + 1][characterPositionColumn + 1] != 1) {
                    characterPositionColumn++;
                    characterPositionRow++;
                }
                break;
            case NUMPAD1:
                if (characterPositionRow + 1 < tempMaze.length && characterPositionColumn - 1 >= 0
                        && tempMaze[characterPositionRow + 1][characterPositionColumn - 1] != 1) {
                    characterPositionColumn--;
                    characterPositionRow++;
                }
                break;
            case HOME:
                characterPositionRow = maze.getStartPosition().getRowIndex();
                characterPositionColumn = maze.getStartPosition().getColumnIndex();
        }
        setChanged();
        notifyObservers(new Position());
    }

    @Override
    public void saveMaze(Stage gameStage) {
        fileChooserSaver(gameStage);
    }

    private void fileChooserSaver(Stage gameStage) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(gameStage);
        if (file != null) {
            saveFile(file);
        }
    }

    private void saveFile(File file) {
        OutputStream out = null;
        try {
            out = new MyCompressorOutputStream(new FileOutputStream(file.getPath()));
            out.write(maze.toByteArray());
            out.flush();
            out.close();
            Files.write(Paths.get(file.getPath()), ("ENDOFMAZE" + characterPositionRow + "," + characterPositionColumn).getBytes(), StandardOpenOption.APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadMaze(Stage gameStage) {
       File file =  fileChooserLoader(gameStage);
        if (file != null) {
            Maze maze = loadFile(file);
            setChanged(); //Raise a flag that I have changed
            notifyObservers(maze); //Wave the flag so the observers will notice
        }
    }

    @Override
    public void loadMazeFromMainMenu(Stage gameStage){
        File file = fileChooserLoader(gameStage);
        if (file != null) {
            Maze maze = loadFile(file);
            Object[] tempArray = new Maze[]{maze};
            setChanged(); //Raise a flag that I have changed
            notifyObservers(tempArray); //Wave the flag so the observers will notice
        }
    }

    private File fileChooserLoader(Stage gameStage) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showOpenDialog(gameStage);
        return file;
    }

    private Maze loadFile(File file) {

        byte[] savedMazeBytes = new byte[100000];
        try {
            //write to file only maze compression
            //File inputFile = new File(file.getPath());
            File tempFile = new File(file.getPath() + ".temp");

            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            //copies only the maze compression to new file
            String currentLine;
            String rowColumns = "";
            while ((currentLine = reader.readLine()) != null) {
                // trim newline when comparing with lineToRemove
                String trimmedLine = currentLine.trim();
                if (!trimmedLine.contains("ENDOFMAZE"))
                    writer.write(currentLine + System.getProperty("line.separator"));
                else {
                    writer.write(currentLine.split("ENDOFMAZE")[0]);
                    rowColumns = currentLine.split("ENDOFMAZE")[1];
                }
            }
            writer.close();
            reader.close();
            //copies the temp file to the original file
            tempFile.renameTo(file);
            tempFile.delete();

            //write character position
            String[] characterPosition = rowColumns.split(",");
            characterPositionRow = Integer.parseInt(characterPosition[0]);
            characterPositionColumn = Integer.parseInt(characterPosition[1]);

            //read maze from file and decompress it
            InputStream in = new MyDecompressorInputStream(new FileInputStream(file.getPath()));
            in.read(savedMazeBytes);
            in.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        this.maze = new Maze(savedMazeBytes);
        return maze;
    }

    @Override
    public void chooseDifficultLevel(String value) {

        Configurations config = new Configurations();
        IMazeGenerator generator;
        if (value.equals("Difficult Maze")) {
            generator = new MyMazeGenerator();
        } else if (value.equals("Simple Maze")) {
            generator = new SimpleMazeGenerator();
        } else {
            generator = new EmptyMazeGenerator();
        }
        config.setMazeGenerator(generator);
    }

    @Override
    public void chooseSolutionWay(String value) {

        Configurations config = new Configurations();
        ISearchingAlgorithm searcher;
        if (value.equals("Best First Search")) {
            searcher = new BestFirstSearch();
        } else if (value.equals("Breadth First Search")) {
            searcher = new BreadthFirstSearch();
        } else {
            searcher = new DepthFirstSearch();
        }
        config.setSearchingAlgorithm(searcher);
    }

    //</editor-fold>

}
