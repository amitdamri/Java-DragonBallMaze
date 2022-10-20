import Client.*;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class JUnitClientTest {

    public static void main(String[] args) {
        ConnectToServer();
    }

    private static void ConnectToServer() {
        try {
            Client client = new Client(
                    InetAddress.getLocalHost(),
                    5400,
                    /*new ClientStrategyCLI()*/
                    new IClientStrategy() {
                        @Override
                        public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                            try {
                                ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                                ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                                toServer.flush();
                                int[] mazeDimensions = new int[]{10, 10};
                                toServer.writeObject(mazeDimensions); //send maze dimensions to server
                                toServer.flush();
                                byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                                InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                                byte[] decompressedMaze = new byte[1000000 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                                is.read(decompressedMaze); //Fill decompressedMaze with bytes
                                Maze maze = new Maze(decompressedMaze);
                                maze.print();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    /*new ClientStrategySendObjectArrayList()*/
            );
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
