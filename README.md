# MazeGenerator

This java program generates a maze and saves it to the input path (f.e. `somewhere/maze.jpg`) as jpg image.

Here the test class to show how it works:

```java
Maze maze = new Maze(WIDTH, LENGTH, START_W, START_L, END_W, END_L, BIAS_TOP, BIAS_RIGHT, BIAS_BOTTOM, PATH_FOR_IMAGE);
```
The sum of every bias should be 1, so `BIAS_LEFT` is not needed.

### Example:

![maze image](https://github.com/TheCookieOfDoom/MazeGenerator/blob/master/pictures/image.jpg "The bias for right and left results in more horizontal structures as seen here.")

The following code generates the maze in the picture:
```java
Maze maze = new Maze(30, 40, 4, 27, 36, 9, (float) 0.1, (float) 0.4, (float) 0.1, "C:/someFolder/maze.jpg");
```
