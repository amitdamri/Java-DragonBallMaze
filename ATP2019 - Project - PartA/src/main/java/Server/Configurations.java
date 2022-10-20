/**
 * This class represents the configuration of the project
 * @autor Dvir Simhon & Amit Damri
 * @since 16.5.2019
 * @version 1.0
 **/

package Server;

import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.BestFirstSearch;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.DepthFirstSearch;
import algorithms.search.ISearchingAlgorithm;

import java.io.*;
import java.util.Properties;

public class Configurations {


    private static Properties prop = new Properties();

    /**
     * sets the searching algorithm
     * @param search searching algorithm
     */
    public static void setSearchingAlgorithm(ISearchingAlgorithm search) {
        prop.setProperty("search.SearchingAlgorithm", search.getName());
        try {
            OutputStream output = new FileOutputStream("resources/config.properties");
            prop.store(output, null);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * gets the searching algorithm
     * @return ISearchingAlgorithm
     */
    public static ISearchingAlgorithm getSearchingAlgorithm() {
        ISearchingAlgorithm searchingAlgorithm = new BestFirstSearch();
        try {
            InputStream input = new FileInputStream("resources/config.properties");
            prop.load(input);
            if (!prop.isEmpty()) {
                switch (prop.getProperty("search.SearchingAlgorithm")) {
                    case "BreadthFirstSearch":
                        searchingAlgorithm = new BreadthFirstSearch();
                        break;
                    case "BestFirstSearch":
                        searchingAlgorithm = new BestFirstSearch();
                        break;
                    case "DepthFirstSearch":
                        searchingAlgorithm = new DepthFirstSearch();
                        break;
                }
            }
            input.close();
            return searchingAlgorithm;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchingAlgorithm;
    }

    /**
     * sets the maze generator type
     * @param generator ImazeGenerator
     */
    public static void setMazeGenerator(IMazeGenerator generator) {
        prop.setProperty("maze.MazeGenerator", generator.getGeneratorName());
        try {
            OutputStream output = new FileOutputStream("resources/config.properties");
            prop.store(output, null);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * gets the maze generator type
     * @return IMazeGenerator
     */
    public static IMazeGenerator getMazeGenerator() {
        IMazeGenerator mazeGenerator = new MyMazeGenerator();
        try {
            InputStream input = new FileInputStream("resources/config.properties");
            prop.load(input);
            if (!prop.isEmpty()) {
                switch (prop.getProperty("maze.MazeGenerator")) {
                    case "EmptyMazeGenerator":
                        mazeGenerator = new EmptyMazeGenerator();
                        break;
                    case "SimpleMazeGenerator":
                        mazeGenerator = new SimpleMazeGenerator();
                        break;
                    case "MyMazeGenerator":
                        mazeGenerator = new MyMazeGenerator();
                        break;
                }
            }
            input.close();
            return mazeGenerator;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mazeGenerator;
    }

    /**
     * sets the number of threads that can eun together
     * @param int numberOfThreads
     */
    public static void setThreadPoolNumber(int numberOfThreads) {
        prop.setProperty("server.ThreadPool", "" + numberOfThreads);
        try {
            OutputStream output = new FileOutputStream("resources/config.properties");
            prop.store(output, null);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * gets the number of threads that can run together
     * @return int
     */
    public static int getThreadPoolNumber() {
        int threadNumber = 2;
        try {
            InputStream input = new FileInputStream("resources/config.properties");
            prop.load(input);
            if (!prop.isEmpty())
                threadNumber = Integer.parseInt(prop.getProperty("server.ThreadPool"));
            input.close();
            return threadNumber;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return threadNumber;
    }


}

