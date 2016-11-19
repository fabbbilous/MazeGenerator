import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

public class Maze {

	private Cell[][] cells;
	private Stack<Cell> memory;
	private float[] bias;
	
	//last bias value gets calculated from the 3 bias inputs (bias1 + bias2 + bias3 + bias4 = 1)
	public Maze(int width, int length, int initialCellWidth, int initialCellLength, int goalWidth, int goalLength, float biasTop, float biasRight, float biasBottom, String path) {
		//build cell map
		cells = new Cell[width][length];
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < length; j++) {
				cells[i][j] = new Cell(i, j);
			}
		}
		//surround with visited
		for(Cell cell : cells[0]) {
			cell.visit();
		}
		for(Cell cell : cells[width - 1]) {
			cell.visit();
		}
		for(Cell[] cellLine : cells) {
			cellLine[0].visit();
			cellLine[length - 1].visit();
		}
		//set bias
		bias = new float[4];
		bias[0] = biasTop;
		bias[1] = biasRight;
		bias[2] = biasBottom;
		//run algorithm
		memory = new Stack<Cell>();
		depthFistBacktrack(cells[initialCellWidth][initialCellLength]);
		//paint image
		BufferedImage image = new BufferedImage(length * 30, width * 30, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2 = image.createGraphics();
		g2.setColor(Color.black);
		g2.fillRect(0, 0, image.getWidth(), image.getHeight());
		g2.setColor(Color.red);
		g2.fillRect(initialCellWidth * 30 + 5, initialCellLength * 30 + 5, 20, 20);
		g2.fillRect(goalWidth * 30 + 5, goalLength * 30 + 5, 20, 20);
		g2.setColor(Color.white);
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < length; j++) {
				if(cells[i][j].getWall(1) == true) {
					g2.drawLine(j * 30 + 2, i * 30 + 2, j * 30 + 30 - 2, i * 30 + 2);
				}
				if(cells[i][j].getWall(2) == true) {
					g2.drawLine(j * 30 + 30 - 2, i * 30 + 2, j * 30 + 30 - 2, i * 30 + 30 - 2);
				}
				if(cells[i][j].getWall(3) == true) {
					g2.drawLine(j * 30 + 2, i * 30 + 30 - 2, j * 30 + 30 - 2, i * 30 + 30 - 2);
				}
				if(cells[i][j].getWall(4) == true) {
					g2.drawLine(j * 30 + 2, i * 30 + 2, j * 30 + 2, i * 30 + 30 - 2);
				}
			}
		}
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		//save image
		File imageFile = new File(path);
		try {
			ImageIO.write(image, "jpg", imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void depthFistBacktrack(Cell initialCell) {
		initialCell.visit();
		depthFirst(initialCell);
	}
	
	private void depthFirst(Cell cell) {
		//check if neighbors are visited
		boolean allNeighborsVisited = true;
		boolean[] neighborArray = neighborsVisited(cell);
		for(boolean bool : neighborArray) {
			if(bool == false) {
				allNeighborsVisited = false;
				break;
			}
		}
		//check 2 possibilities
		if(allNeighborsVisited == false) {
			//there are unvisited neighbors
			memory.push(cell);
			int chosenNeighbor = chooseNeighbor(neighborArray);
			cell.breakWall(chosenNeighbor);	//break wall of current cell to selected cell
			switch(chosenNeighbor) {	//break wall of selected cell to current cell
				case 1: //chosen cell is on the top
					cells[cell.getPosWidth() - 1][cell.getPosLength()].breakWall(3);
					cells[cell.getPosWidth() - 1][cell.getPosLength()].visit();
					depthFirst(cells[cell.getPosWidth() - 1][cell.getPosLength()]);
					break;
				case 2: //chosen cell is on the right
					cells[cell.getPosWidth()][cell.getPosLength() + 1].breakWall(4);
					cells[cell.getPosWidth()][cell.getPosLength() + 1].visit();
					depthFirst(cells[cell.getPosWidth()][cell.getPosLength() + 1]);
					break;
				case 3: //chosen cell is on the bottom
					cells[cell.getPosWidth() + 1][cell.getPosLength()].breakWall(1);
					cells[cell.getPosWidth() + 1][cell.getPosLength()].visit();
					depthFirst(cells[cell.getPosWidth() + 1][cell.getPosLength()]);
					break;
				case 4: //chosen cell is on the left
					cells[cell.getPosWidth()][cell.getPosLength() - 1].breakWall(2);
					cells[cell.getPosWidth()][cell.getPosLength() - 1].visit();
					depthFirst(cells[cell.getPosWidth()][cell.getPosLength() - 1]);
					break;
			}
		} else if(!memory.isEmpty()) {
			//every neighbor is visited
			depthFirst(memory.pop());	//go back to last cell
		}
	}
	
	private boolean[] neighborsVisited(Cell cell) {
		int cellPosWidth = cell.getPosWidth();
		int cellPosLength = cell.getPosLength();
		boolean[] visited = new boolean[] {false, false, false, false};
		//check for neighbors
		if(cells[cellPosWidth - 1][cellPosLength].isVisited()) {
			visited[0] = true;
		}
		if(cells[cellPosWidth][cellPosLength + 1].isVisited()) {
			visited[1] = true;
		}
		if(cells[cellPosWidth + 1][cellPosLength].isVisited()) {
			visited[2] = true;
		}
		if(cells[cellPosWidth][cellPosLength - 1].isVisited()) {
			visited[3] = true;
		}
		return visited;
	}
	
	private int chooseNeighbor(boolean[] neighborArray) {
		int result = -1;
		while(result == -1) {
			//create bias by 
			int max1 = (int) (bias[0] * 100);
			int max2 = (int) (bias[1] * 100) + max1;
			int max3 = (int) (bias[2] * 100) + max2;
			int check = 0;
			int rand = ThreadLocalRandom.current().nextInt(1, 101);	//random number from 1 to 101 (including 1, excluding 101)
			if(rand <= max1) {
				check = 1;
			} else if(rand > max1 && rand <= max2) {
				check = 2;
			} else if(rand > max2 && rand <= max3) {
				check = 3;
			} else if(rand > max3) {
				check = 4;
			}
			if(neighborArray[check - 1] == false) {
				result = check;
			}
		}
		return result;
	}
	
}
