/**
 * Represents a server strategy the generates mazes
 * @autor Dvir Simhon & Amit Damri
 * @version 1.0
 */
package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;

import java.io.*;

public class ServerStrategyGenerateMaze implements IServerStrategy {

    /**
     * reads the maze size from the client and generates a maze
     * @param inFromClient InputStream to read from the client
     * @param outToClient outputStream to write to the client
     */
    @Override
    public void serverStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {

            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            OutputStream compressor = new MyCompressorOutputStream(buffer);
            toClient.flush();


            //creates the maze
            int[] mazeSize = (int[]) fromClient.readObject();
            IMazeGenerator mazeGenerator = Configurations.getMazeGenerator();
            Maze mazeToCreate = mazeGenerator.generate(mazeSize[0],mazeSize[1]);
            Server.LOG.info("Maze Information - number of rows: " + mazeSize[0] + ", number of columns: " + mazeSize[1]);
            //compress the maze and puts it in byte object
            compressor.write(mazeToCreate.toByteArray());
            compressor.close();
            byte[] maze = buffer.toByteArray();
            toClient.writeObject(maze);

            buffer.close();
            toClient.close();
            fromClient.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
