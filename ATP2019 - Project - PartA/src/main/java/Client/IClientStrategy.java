/**
 * Interface of the Client Strategy
 * @autor Dvir Simhon & Amit Damri
 * @version 1.0
 */
package Client;

import java.io.InputStream;
import java.io.OutputStream;

public interface IClientStrategy {

    /**
     * The Strategy of the client
     * @param inFromServer InputStream to read from the server
     * @param outToServer outputStream to write to the server
     */
    void clientStrategy(InputStream inFromServer, OutputStream outToServer);
}
