import Server.*;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

public class JUnitServerTest {

    public static void main(String[] args){
        StartingServer();
    }

    private static void StartingServer() {
        Server server = new Server(
                5400,
                1000,
                new ServerStrategyGenerateMaze()
                /*new ServerStrategyObjectArrayListAddValues()*/
        );
        server.start();
        StartCLI();
        server.stop();
    }

    private static void StartCLI(){
        System.out.println("Server started!");
        System.out.println("Enter 'exit' to close server.");
        Scanner reader = new Scanner(System.in);

        do
        {
            System.out.print(">>");
        } while (!reader.next().toLowerCase().equals("exit"));
    }
}
