/**
 * Represents a server strategy that solves a maze
 * @autor Dvir Simhon & Amit Damri
 * @version 1.0
 */
package Server;

import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;

import java.io.*;
import java.util.Arrays;

public class ServerStrategySolveSearchProblem implements IServerStrategy {

    /**
     * reads a maze from the client and sends back its solution
     * @param inFromClient InputStream to read from the client
     * @param outToClient outputStream to write to the client
     */
    @Override
    public void serverStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            Maze maze;
            Solution solution;
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            toClient.flush();

            //get maze from client
            Object tempMaze = fromClient.readObject();

            //if instance of maze casting to its class;
            if (tempMaze instanceof Maze) {
                maze = (Maze) tempMaze;
            }
            //not instance of maze
            else
                //default maze
                maze = (Configurations.getMazeGenerator()).generate(5, 5);

            //checks if the given maze already solved
            byte[] byteMaze = maze.toByteArray();
            solution = mazeHasSolution(byteMaze);

            if (null == solution) {
                //maze doesnt have solution in the solutions directory - solve it
                SearchableMaze searchableMaze = new SearchableMaze(maze);
                solution = (Configurations.getSearchingAlgorithm()).solve(searchableMaze);
                saveSolutionOfMaze(byteMaze, solution);
            }
            //sends the solution
            toClient.writeObject(solution);

            toClient.close();
            fromClient.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * saves the solution of the maze
     * @param byteMaze
     * @param mazeSolution
     */
    private void saveSolutionOfMaze(byte[] byteMaze, Solution mazeSolution) {
        try {
            String solutionDirectoryPath = String.format("%s/SolutionsAmit&Dvir", System.getProperty("java.io.tmpdir"));
            String tempDirectoryPath = String.format("%s/MazesAmit&Dvir", System.getProperty("java.io.tmpdir"));
            createDirectory(tempDirectoryPath); // creates %temp%/Mazes directory if doesnt exist
            String mazeFileName = String.format("MazeNumber%d", System.currentTimeMillis());
            String mazeFileToWrite = String.format("%s/%s", tempDirectoryPath, mazeFileName);


            //write maze to file in temp directory
            OutputStream outCompressWrite = new MyCompressorOutputStream(new FileOutputStream(mazeFileToWrite));
            outCompressWrite.flush();
            outCompressWrite.write(byteMaze);
            outCompressWrite.close();
            //write solution
            createDirectory(solutionDirectoryPath);
            ObjectOutputStream outWriteSolution = new ObjectOutputStream(new FileOutputStream(String.format("%s/%s", solutionDirectoryPath, mazeFileName)));
            outWriteSolution.flush();
            outWriteSolution.writeObject(mazeSolution);
            outWriteSolution.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDirectory(String directoryName) {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    /**
     * checks if the maze alreadt has a solution in the directory
     * @param byteMazeToCheck
     * @return Solution of the maze
     */
    private Solution mazeHasSolution(byte[] byteMazeToCheck) {
        if (null != byteMazeToCheck) {
            try {
                File[] mazesSolved = (new File(String.format("%s/MazesAmit&Dvir", System.getProperty("java.io.tmpdir"))).listFiles());
                if (null != mazesSolved) {
                    for (File maze : mazesSolved) {
                        byte[] tempByteMaze = new byte[byteMazeToCheck.length];
                        InputStream mazeFile = new MyDecompressorInputStream(new FileInputStream(maze.getPath()));
                        try {
                            mazeFile.read(tempByteMaze);
                            mazeFile.close();
                        } catch (IOException e){}
                        if (Arrays.equals(byteMazeToCheck, tempByteMaze)) {
                            System.out.println("Found Solution");
                            return findTheRequiredSolution(maze.getName());
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * returns solution to the maze
     * @param mazeName the name of the maze
     * @return Solution of the required solution
     */
    private Solution findTheRequiredSolution(String mazeName) {
        try {
            ObjectInputStream solutionFile = new ObjectInputStream(new FileInputStream(
                    String.format("%s/SolutionsAmit&Dvir/%s", System.getProperty("java.io.tmpdir"),mazeName)));

            Object mazeSolution = solutionFile.readObject();
            solutionFile.close();
            if (mazeSolution instanceof Solution)
                return (Solution) mazeSolution;
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}