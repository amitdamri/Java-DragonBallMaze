import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;



class JUnitTestingBestFirstSearch {

    private BestFirstSearch b = new BestFirstSearch();
    private MyMazeGenerator generator = new MyMazeGenerator();

    JUnitTestingBestFirstSearch() {
    }


    @Test
    public void testWrongParametersMazeInput() {

        Maze maze = generator.generate(4,-10);
        ISearchable searchable = new SearchableMaze(maze);
        Solution sol = b.solve(searchable);
        assertNotEquals(null, sol.getSolutionPath());

    }

    @Test
    public void getName() {

        assertEquals("BestFirstSearch", b.getName());

    }

    @Test
    public void setPriority() {

        Position position = new Position(1,1);
        AState state = new MazeState(position, null, 10);
        assertEquals(10, state.getCost());

    }

    @Test
    void solve() {
        Solution s = b.solve(null);
        System.out.println(s.toString());
        assertEquals(true, true);
    }

    @Test
    void testSolve() {
        ISearchable s = new SearchableMaze((Maze)null);
        this.b.solve(s);
        assertEquals(true, true);
    }

    @Test
    void test2() {
        IMazeGenerator maze = new MyMazeGenerator();
        Maze mazeC = maze.generate(0, 0);
        ISearchable s = new SearchableMaze(mazeC);
        this.b.solve(s);
        assertEquals(true, true);
    }

    @Test
    void test3() {
        IMazeGenerator maze = new MyMazeGenerator();
        Maze mazeC = maze.generate(-1, -1);
        ISearchable s = new SearchableMaze(mazeC);
        this.b.solve(s);
        assertEquals(true, true);
    }

    @Test
    public void testNull(){
        Solution sol = new Solution();
        System.out.println(sol.getSolutionPath().get(0));
    }
}