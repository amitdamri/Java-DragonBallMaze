package Server.Strategies;

import Server.IServerStrategy;

import java.io.*;

public class ServerStrategyStringReverser implements IServerStrategy {

    @Override
    public void serverStrategy(InputStream inFromClient, OutputStream outToClient) {
        BufferedReader fromClient = new BufferedReader(new InputStreamReader(inFromClient));
        BufferedWriter toClient = new BufferedWriter(new PrintWriter(outToClient));

        String clientCommand;
        try {
            while (fromClient != null && !(clientCommand = fromClient.readLine()).equals("exit")) {
                Thread.sleep(5000);
                toClient.write(new StringBuilder(clientCommand).reverse().toString() + "\n");
                toClient.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}