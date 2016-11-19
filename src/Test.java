
public class Test {

  //BIAS_LEFT is not needed because it can be calculated, the sum of every bias should equal 1
	public static void main(String[] args) {
		Maze maze = new Maze(WIDTH, LENGTH, START_W, START_L, END_W, END_L, BIAS_TOP, BIAS_RIGHT, BIAS_BOTTOM, PATH_FOR_IMAGE);
	}
	
}
