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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Server {
    private int port;
    private int listeningInterval;
    private IServerStrategy serverStrategy;
    private volatile boolean stop;
    private ExecutorService executor;
    public static final Logger LOG = LogManager.getLogger(); //Log4j2


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
            LOG.info(String.format("Server starter at %s!", serverSocket));
            LOG.info(String.format("Server's Strategy: %s", serverStrategy.getClass().getSimpleName()));
            LOG.info("Server is waiting for clients...");

            while (!stop) {
                try {
                    Socket clientSocket = serverSocket.accept(); // blocking call
                    LOG.info(String.format("Client excepted: %s", clientSocket));
                    executor.execute(() -> {
                        handleClient(clientSocket);
                        LOG.info(String.format("Finished handle client: %s", clientSocket));
                    });
                } catch (SocketTimeoutException e) {
                    LOG.debug("Socket Timeout - No clients pending!");
                }
            }

            executor.shutdown(); //shutdown executor
            executor.awaitTermination(15, TimeUnit.MINUTES); //wait maximum 15 minutes for all tasks to complete. After that exit.
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            LOG.error("Error await termination for ThreadPool");
            e.printStackTrace();
        }
    }

    /**
     * handles an accepted client
     * @param clientSocket
     */
    private void handleClient(Socket clientSocket) {
        try {
            LOG.info(String.format("Handling client with socket: %s", clientSocket.toString()));
            serverStrategy.serverStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
            clientSocket.close();
        } catch (IOException e) {
            LOG.error("IOException", e);
            e.printStackTrace();
        }
    }

    /**
     * stops the server
     */
    public void stop() {
        LOG.info("Stopping server..");
        stop = true;
    }
}
