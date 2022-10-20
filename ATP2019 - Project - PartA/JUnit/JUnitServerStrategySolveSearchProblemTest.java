import Client.*;
import Server.*;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JUnitServerStrategySolveSearchProblemTest {

    @Test
    public void testingSolve() {
        Server s = new Server(6000, 1000, new ServerStrategySolveSearchProblem());
        s.start();
        Client client = null;
        try {
            client = new Client(InetAddress.getLocalHost(), 6000, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        MyMazeGenerator mg = new MyMazeGenerator();
                        Maze maze = mg.generate(100, 100);
/*
                        maze.maze = new int[][]{
                                {1,0,0,1,1,0},
                                {1,1,0,1,1,0},
                                {1,0,0,1,0,1},
                                {1,1,0,1,1,1},
                                {1,1,0,0,1,1}

                        };
                        maze.startPosition = new Position(0,1);
                        maze.goalPosition = new Position(4,3);
*/

                        //maze.print();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server

                        //Print Maze Solution retrieved from the server
                        System.out.println(String.format("Solution steps: %s", mazeSolution));
                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                        for (int i = 0; i < mazeSolutionSteps.size(); i++) {
                            System.out.println(String.format("%s. %s", i, mazeSolutionSteps.get(i).toString()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        client.communicateWithServer();
        s.stop();
    }
}