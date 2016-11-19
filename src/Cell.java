
public class Cell {

	private boolean[] walls;	//order: top, right, bottom, left
	private boolean visited;
	private int posWidth;
	private int posLength;
	
	public Cell(int posWidth, int posLength) {
		walls = new boolean[] {true, true, true, true};	//build all 4 walls
		visited = false;
		this.posWidth = posWidth;
		this.posLength = posLength;
	}
	
	public boolean getWall(int side) {
		return walls[side - 1];
	}
	
	public void breakWall(int side) {
		walls[side - 1] = false;
	}
	
	public void visit() {
		visited = true;
	}
	
	public boolean isVisited() {
		return visited;
	}
	
	public int getPosWidth() {
		return posWidth;
	}
	
	public int getPosLength() {
		return posLength;
	}
	
}
