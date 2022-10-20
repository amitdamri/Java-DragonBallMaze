import Server.Configurations;
import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.search.DepthFirstSearch;
import org.junit.jupiter.api.Test;


public class JUnitConfigurationTest {

    @Test
    public void test() {
        Configurations.setMazeGenerator(new EmptyMazeGenerator());
        Configurations.setSearchingAlgorithm(new DepthFirstSearch());
        Configurations.setThreadPoolNumber(4);
    }
@Test
    public void test2(){
        System.out.println((Configurations.getMazeGenerator()).getGeneratorName());
        System.out.println((Configurations.getSearchingAlgorithm().getName()));
        System.out.println(Configurations.getThreadPoolNumber());
    }
}
