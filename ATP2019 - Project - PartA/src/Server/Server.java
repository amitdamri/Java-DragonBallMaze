/**
 * this class represents the server
 * @autor Dvir Simhon & Amit Damri
 * @version 1.0
 */
package Server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;



public class Server {
    private int port;
    private int listeningInterval;
    private IServerStrategy serverStrategy;
    private volatile boolean stop;
    private ExecutorService executor;

    /**
     * Constructor
     * @param port
     * @param listeningInterval
     * @param serverStrategy
     */
    public Server(int port, int listeningInterval, IServerStrategy serverStrategy) {
        this.port = port;
        this.listeningInterval = listeningInterval;
        this.serverStrategy = serverStrategy;
        this.executor = Executors.newFixedThreadPool(Configurations.getThreadPoolNumber());

    }

    /**
     * starts the server
     */
    public void start() {
        new Thread(() -> runServer()).start();
    }

    /**
     * runs the server
     */
    private void runServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningInterval);
            System.out.println(String.format("Server starter at %s!", serverSocket));
            System.out.println(String.format("Server's Strategy: %s", serverStrategy.getClass().getSimpleName()));
            System.out.println("Server is waiting for clients...");

            while (!stop) {
                try {
                    Socket clientSocket = serverSocket.accept(); // blocking call
                    System.out.println(String.format("Client excepted: %s", clientSocket));
                    executor.execute(() -> {
                        handleClient(clientSocket);
                        System.out.println(String.format("Finished handle client: %s", clientSocket));
                    });
                } catch (SocketTimeoutException e) {
                    System.out.println("Socket Timeout - No clients pending!");
                }
            }

            executor.shutdown(); //shutdown executor
            executor.awaitTermination(15, TimeUnit.MINUTES); //wait maximum 15 minutes for all tasks to complete. After that exit.
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Error await termination for ThreadPool");
            e.printStackTrace();
        }
    }

    /**
     * handles an accepted client
     * @param clientSocket
     */
    private void handleClient(Socket clientSocket) {
        try {
            System.out.println(String.format("Handling client with socket: %s", clientSocket.toString()));
            serverStrategy.serverStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * stops the server
     */
    public void stop() {
        System.out.println("Stopping server..");
        stop = true;
    }
}
