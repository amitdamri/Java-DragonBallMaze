import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class JUnitMazeTest {


    @Test
    public void testByteMaze() throws InterruptedException {
        MyMazeGenerator m = new MyMazeGenerator();
        Maze maze = m.generate(100, 100);
        System.out.println(maze.getGoalPosition() + "" + maze.getStartPosition());
        //maze.print();
        byte[] b = maze.toByteArray();
        System.out.print(b.length);
        System.out.println(Arrays.toString(b));
    }

    //need to remove comment in maze class
    @Test
    public void testMazeSize() {

        MyMazeGenerator m = new MyMazeGenerator();
        Maze maze = m.generate(1000, 1000);
        byte[] b = maze.toByteArray();
        System.out.println(b.length);
        //System.out.println(i + " ****** " );
    }

    @Test
    public void test() {
        MyMazeGenerator m = new MyMazeGenerator();
        Maze maze = m.generate(10, 10);
        byte[] b = maze.toByteArray();
        System.out.println(b.length);
    }

    @Test
    public void test10() {
        MyMazeGenerator m = new MyMazeGenerator();

        for (int i=0;i<1000;i++) {
            Maze maze = m.generate((int)Math.random() * 1000, (int)Math.random() * 1000);
            System.out.println("Success " + i);
        }
    }
/*
    @Test
    public void test2() {
        for (int i=0;i<20;i++) {
            MyMazeGenerator m = new MyMazeGenerator();
            Maze maze = m.generate(10, 10);
            byte[] b = maze.toByteArray();
            Maze maze2 = new Maze(b);
            boolean areMazesEquals = true;
            System.out.println("*************TEST " + i + "*******************");
            System.out.println(maze.maze.length == maze2.maze.length);
            System.out.println(maze.maze[0].length == maze2.maze[0].length);
            System.out.println(maze.getStartPosition().equals(maze2.getStartPosition()));
            System.out.println(maze.getGoalPosition().equals(maze2.getGoalPosition()));
            for (int j = 0; j < maze.maze.length; j++) {
                areMazesEquals = Arrays.equals(maze.maze[j], maze2.maze[j]);
                if (areMazesEquals == false) {
                    System.out.println(false);
                    break;
                }
            }
            System.out.println(areMazesEquals);
        }
    }
    */

}