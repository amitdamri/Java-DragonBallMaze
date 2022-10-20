/**
 * This Interface represents the server strategy
 * @autor Dvir Simhon & Amit Damri
 * @since 16.5.2019
 * @version 1.0
 **/
package Server;

import java.io.InputStream;
import java.io.OutputStream;

public interface IServerStrategy {

    /**
     * The Strategy of the server
     * @param inFromClient InputStream to read from the client
     * @param outToClient outputStream to write to the client
     */
    void serverStrategy(InputStream inFromClient, OutputStream outToClient);

}
