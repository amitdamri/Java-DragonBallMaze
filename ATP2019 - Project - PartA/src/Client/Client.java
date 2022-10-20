/**
 * This class represents the Client
 * @autor Dvir Simhon & Amit Damri
 * @version 1.0
 */
package Client;

import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private InetAddress serverIP;
    private int serverPort;
    private IClientStrategy clientStrategy;

    /**
     * Constructor
     * @param serverIP
     * @param serverPort
     * @param clientStrategy
     */
    public Client(InetAddress serverIP, int serverPort, IClientStrategy clientStrategy) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.clientStrategy = clientStrategy;
    }

    /**
     * Makes a connection to the server and executes the client strategy
     */
    public void communicateWithServer() {
        try {
            Socket theServer = new Socket(serverIP, serverPort);
            System.out.println(String.format("Client is connected to server (IP: %s, port: %s)", serverIP, serverPort));
            clientStrategy.clientStrategy(theServer.getInputStream(), theServer.getOutputStream());
            theServer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
