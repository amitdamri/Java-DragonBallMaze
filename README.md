# Java-DragonBallMaze

<p align="center">
  <img width=600 height=300 src="https://i0.wp.com/www.lacasadeel.net/wp-content/uploads/2022/09/Sin-titulo-30.jpg?resize=696%2C391">
</p>

We implemented in Java (8) a maze game, while the GUI was written with XML in JavaFX. This task encompassed many advanced programming principles such as proper class and interface design, server/client, proper use of thread pools, binding and SOLID coding guidelines. It also involved several design patterns such as decorator, observer/observable and singleton to name a few. We also practice use with Maven repositories manager include log4j2 LogManager.
The complex mazes are based on <a href="https://en.wikipedia.org/wiki/Maze_generation_algorithm">Prim's algorithm</a>.
Try solve the dragon ball maze at different difficulties, enjoy!



#### PartA & PartB Code hierarchy
1. Algorithms package > mazeGenerators pacakge:
- Maze class - represent a maze
- MazeGenerator class - creates instances of maze objects.
- IMazeGenerator interface
- AMazeGenerator abstract class 
- EmptyMazeGenerator class - inherits the abstract class, creates an empty maze.
- SimpleMazeGenerator class - inherits the abstract class, creates an random maze.
- MyMazeGenerator class - according to Prim's algorithm.

2. Algorithms > search pacakge:
- classes - ASearchingAlgorithm, AState, MazeState, Solution
- interfaces - Searchable , ISearchingAlgorithm
- BreadthFirstSearch - implementation of BFS
- DepthFirstSearch - implementation of DFS
- BestFirstSearch - implementation of Best FS
- SearchableMaze - Object adapter to adapt Maze object into a search problem (ISearchable object).

3. IO Pacakge (Decorator Design Pattern):
- MyCompressorOutputStream - inherits OutputStream
- MyDecompressorInputStream - inherits InputStream

4. Server:
- ServerStrategyGenerateMaze - gets array, creates a maze according to the given sizes, compress it using MyCompressorOutputStream, and sends back to the client.
- ServerStrategySolveSearchProblem - the server gets from the client a Maze object and returns the Solution for this Maze.
- Configurations static class - import and export of all the properties that are needed in the program from config.properties file (in resources). 

5. Client:
- IClientStrategy

6. test package - unit tests of the different parts using JUnit

#### PartC Code hierarchy
Create a desktop application based on MVVM architecture (Model, View, View Model).
<p align="center">
  <img width=600 height=300 src="https://user-images.githubusercontent.com/49988048/196905927-6db99892-f4b4-41bb-bda3-75d878131a8f.png">
</p>

1. View - fxml, controller, IView interface - all the relevant files for the user interface.
2. ViewModel - responsible for the connection between the View and the Model.  
3. Model - IModel interface - responsible for: algorithms, connection to the servers, save the current maze and save the current position of the character.

Frameworks: Maven anf Log4j2
