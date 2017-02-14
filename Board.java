/*
 * Name:  Allston Fojas
 * Email: allstonfojas@gmail.com
 * Date:  February 7, 2017
 * File:  Board.java
 * File Header:
 * This file contains methods that creates and changes the 4Square board. 
 * This file has methods to save the board to an output file, add a random tile 
 * to the board each time a valid move is made, and flip the board in different
 * ways. This file also has the methods that allow the tiles on the board to 
 * move in each direction and a method to determine if the game is over. 
 * These different methods allow the user to properly play the 4Square game.
 */

/**
 * Sample Board
 * <p/>
 * 0   1   2   3
 * 0   -   -   -   -	
 * 1   -   -   -   -
 * 2   -   -   -   -
 * 3   -   -   -   -
 * <p/>
 * The sample board shows the index values for the columns and rows
 * Remember that you access a 2D array by first specifying the row
 * and then the column: grid[row][column]
 */

import java.util.*;
import java.io.*;

/* 
 * Class Header: 
 * This class contains different methods that I wrote that creates the 4Square 
 * board and makes the board move in the direction specified by the user. 
 * This class includes the actual 4Square grid that the user sees and the 
 * user's score.
 */
public class Board {
	//Instance variables
	public final int NUM_START_TILES = 2; //number of starting tiles
	public final int TWO_PROBABILITY = 90; //probability of having a 2 tile
	public int GRID_SIZE; //the grid size	


	private final Random random; //random object
	private int[][] grid; //the actual grid
	private int score; //the player's score

	public int[][] oldGrid; //the old grid
	public int oldScore; //the player's old score

	/**
	 * Board Constructor, Constructs a fresh board with random tiles
	 * @param random A random object, boardSize The size of the board
	 * @return none, it's a constructor
	 **/
	public Board(Random random, int boardSize) {
		this.random = random; 
		GRID_SIZE = boardSize;
		grid = new int[GRID_SIZE][GRID_SIZE]; //makes grid
		score = 0;
		for (int tile = 0; tile < NUM_START_TILES; tile++) {
			this.addRandomTile(); //puts 2 new random tiles on grid
		}
	}

	/**
	 * Board Constructor, Constructs a board based off of an input file
	 * @param random A random object, inputBoard The name of the input file
	 * @return none, it's a constructor
	 **/
	public Board(Random random, String inputBoard) throws IOException {
		Scanner scan = new Scanner(new File(inputBoard)); //gets existing board
		this.random = random;
		GRID_SIZE = scan.nextInt(); //gets grid size from scan
		score = scan.nextInt(); //gets score from scan
		grid = new int[GRID_SIZE][GRID_SIZE]; //makes grid
		int row = 0;
		int col = 0;
		while (scan.hasNext()) { //arranges existing board
			int gridVal = scan.nextInt();
			grid[row][col] = gridVal;	
			col++;
			if (col % GRID_SIZE == 0) {
				row++;
				col = 0;
			}
		}
	}

	/**
	 * Saves the current board to a file
	 * @param outputBoard The name of the output file
	 * @return void
	 **/
	public void saveBoard(String outputBoard) throws IOException {
		PrintWriter console = new PrintWriter(outputBoard); //place to save board
		console.println(GRID_SIZE); //saves grid size
		console.println(score); //saves score
		oldScore = score; //assigns old score to the current score
		oldGrid = new int[GRID_SIZE][GRID_SIZE];
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[0].length; col++) {
				console.print(grid[row][col] + " "); //saves board
				oldGrid[row][col] = grid[row][col]; //deep copy
			}
			console.println();
		}
		console.flush(); 
		console.close(); //closes console to save board & file
	}

	/**
	 * Allows the user to undo the last move
	 * @param none
	 * @return void
	 **/
	public void undo() {
		score = oldScore; //assigns current score to the old score
		for (int row = 0; row < oldGrid.length; row++) { //deep copy
			for (int col = 0; col < oldGrid[0].length; col++) {
				grid[row][col] = oldGrid[row][col];
			}
		}
	}

	/**
	 * Adds a random tile (of value 4 or 16) to a random empty space 
	 * on the board
	 * @param none
	 * @return void
	 **/
	public void addRandomTile() {
		int limit = 100; //limit to make value between 0-99
		int count = 0; //the number of empty spaces
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[0].length; col++) {
				if (grid[row][col] == 0) {
					count++; //counts number of empty spaces
				}
			}
		}
		if (count == 0) { //returns same grid if there are no empty spaces
			return;
		}
		int location = random.nextInt(count)+1; //random location to add tile
		int value = random.nextInt(limit); //determines if a 2 or 4 tile is added
		count = 0; //reassigns count to 0 to reuse variable
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[0].length; col++) {
				if (grid[row][col] == 0) {
					count++; //recounts number of empty spaces
				}
				if (count == location) { //checks if the random 
					//tile's location is correct
					if (value < TWO_PROBABILITY) {
						grid[row][col] = 4; //adds 4 tile
						return;
					} else {
						grid[row][col] = 16; //adds 16 tile
						return;
					}
				}

			}
		}
	}

	/**
	 * Flip the board horizontally or vertically, Rotate the board by 90 
	 * degrees clockwise or 90 degrees counter-clockwise.
	 * @param change The value corresponding to a specific board change
	 * @return void
	 **/
	public void flip(int change) {
		if (change < 1 || change > 5) { //checks if change is valid
			System.out.println("Error: Invalid integer input for" 
					+ "change variable");
		}
		if (change == 1) {	
			this.flipHorizontal(); //flips board horizontally
		} else if (change == 2) {
			this.flipVertical(); //flips board vertically
		} else if (change == 3) {
			this.rotateClockwise(); //rotates board clockwise
		} else if (change == 4) {
			this.rotateCounterClockwise(); //rotates board counterclockwise
		} else if (change == 5) { //flips board diagonally
			this.rotateCounterClockwise();
			this.flipHorizontal();
		}
	}

	/**
	 * Flip the board horizontally
	 * @param none
	 * @return void
	 **/
	public void flipHorizontal() {
		int limit1 = grid[0].length/2; //half of grid's width
		int limit2 = grid.length; //the grid's length
		int width = grid[0].length; //the grid's width
		int leftNum = 0; //number on left half of grid
		int rightNum = 0; //number on right half of grid
		int tempNum; //temp number to swap leftNum and rightNum
		for (int row = 0; row < limit2; row++) { //swaps leftNum & rightNum
			for (int col = 0; col < limit1; col++) {
				leftNum = grid[row][col];
				rightNum = grid[row][width-1-col];
				tempNum = rightNum;
				rightNum = leftNum;
				leftNum = tempNum;
				grid[row][col] = leftNum;
				grid[row][width-1-col] = rightNum;
			}
		}
	}

	/**
	 * Flip the board vertically
	 * @param none
	 * @return void
	 **/
	public void flipVertical() {
		int limit1 = grid[0].length; //the grid's width
		int limit2 = grid.length/2; //half of grid's length
		int height = grid.length; //the grid's length
		int topNum = 0; //number on top half of grid
		int bottomNum = 0; //number on bottom half of grid
		int tempNum; //temp number to swap topNum and bottomNum
		for (int col = 0; col < limit1; col++) { //swaps topNum & bottomNum
			for (int row = 0; row < limit2; row++) {
				topNum = grid[row][col];
				bottomNum = grid[height-1-row][col];
				tempNum = bottomNum;
				bottomNum = topNum;
				topNum = tempNum;
				grid[row][col] = topNum;
				grid[height-1-row][col] = bottomNum;
			}
		}
	}

	/**
	 * Rotate the board by 90 degrees counter-clockwise
	 * @param none
	 * @return void
	 **/
	public void rotateCounterClockwise() {
		int gridVal; //gets each value in the grid 
		for (int row = 0; row < grid.length; row++) { //transposes grid
			for (int col = row; col < grid[0].length; col++) {
				gridVal = grid[row][col];
				grid[row][col] = grid[col][row];
				grid[col][row] = gridVal;	
			}
		}
		for (int row = 0; row < grid.length/2; row++) { //rotates grid 
			//counterclockwise
			for (int col = 0; col < grid[0].length; col++) {
				gridVal = grid[row][col];
				grid[row][col] = grid[grid.length-1-row][col];
				grid[grid.length-1-row][col] = gridVal;
			}
		}
	}

	/**
	 * Rotate the board by 90 degrees clockwise
	 * @param none
	 * @return void
	 **/
	public void rotateClockwise() {
		int gridVal; //gets each value in the grid
		for (int row = 0; row < grid.length; row++) { //transposes grid
			for (int col = row; col < grid[0].length; col++) {
				gridVal = grid[row][col];
				grid[row][col] = grid[col][row];
				grid[col][row] = gridVal;	
			}
		}
		for (int col = 0; col < grid[0].length/2; col++) { //rotates grid
			//clockwise
			for (int row = 0; row < grid.length; row++) {
				gridVal = grid[row][col];
				gridVal = grid[row][grid[0].length-1-col];
				grid[row][grid[0].length-1-col] = gridVal;
			}
		}
		this.flipHorizontal(); //flips grid horizontally to complete rotation
	}

	//Complete this method ONLY if you want to attempt at getting the extra credit
	//Returns true if the file to be read is in the correct format, else return
	//false
	/**
	 * Returns true if the file to be read is in the correct format, 
	 * else return false
	 * @param inputFile The name of the input file
	 * @return true if the file to be read is in the correct format, 
	 * else return false
	 **/
	public static boolean isInputFileCorrectFormat(String inputFile) {
		//The try and catch block are used to handle any exceptions
		//Do not worry about the details, just write all your 
		//conditions inside the try block
		try {
			//write your code to check for all conditions and 
			//return true if it satisfies
			//all conditions else return false
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Performs a move Operation
	 * @param direction The direction in which all the tiles on the board move
	 * @return true If the move is valid, false If the move is invalid
	 **/
	public boolean move(Direction direction) {
		if (direction == Direction.LEFT && this.canMove(direction)) {
			this.moveLeft(); //moves board left
			return true;
		} else if (direction == Direction.RIGHT && this.canMove(direction)) {
			this.moveRight(); //moves board right
			return true;
		} else if (direction == Direction.UP && this.canMove(direction)) {
			this.moveUp(); //moves board up
			return true;
		} else if (direction == Direction.DOWN && this.canMove(direction)) {
			this.moveDown(); //moves board down
			return true;
		}
		return false;
	}


	/**
	 * Moves board to the left
	 * @param none
	 * @return void
	 **/
	public void moveLeft() {
		for (int row = 0; row < grid.length; row++) { //add numbers
			for (int col = 0; col < grid[0].length-1; col++) {
				for (int c = col+1; c < grid[0].length; c++) {
					if (grid[row][col] == grid[row][c] && 
							grid[row][col] != 0) {
						if (grid[row][c-1] != 0 || 
								grid[row][col+1] != 0) {
							if (grid[row][col+1] != 
									grid[row][c] 
									&& grid[row][c-1] 
									!= grid[row][col]) {
								break;
									}
								}
						grid[row][col] *= 4;
						grid[row][c] = 0;
						score += grid[row][col]; //adds score
						break;
							}
				}
			}
		}
		for (int row = 0; row < grid.length; row++) { //shift numbers
			for (int col = 0; col < grid[0].length-1; col++) {
				for (int c = col+1; c < grid[0].length; c++) {
					if (grid[row][col] == 0) {
						grid[row][col] = grid[row][c];
						grid[row][c] = 0;
					}
				}
			}
		}
	}

	/**
	 * Moves board to the right
	 * @param none
	 * @return void
	 **/
	public void moveRight() {
		for (int row = 0; row < grid.length; row++) { //add numbers
			for (int col = grid[0].length-1; col >= 1; col--) {
				for (int c = col-1; c >= 0; c--) {
					if (grid[row][col] == grid[row][c] && 
							grid[row][col] != 0) {
						if (grid[row][c+1] != 0 || 
								grid[row][col-1] != 0) {
							if (grid[row][c+1] != 
									grid[row][col] 
									&& grid[row][col-1] 
									!= grid[row][c]) {
								break;
									}
								}
						grid[row][col] *= 4;
						grid[row][c] = 0;
						score += grid[row][col]; //adds score
						break;
							}
				}
			}
		}
		for (int row = 0; row < grid.length; row++) { //shift numbers
			for (int col = grid[0].length-2; col >= 0; col--) {
				for (int c = 1; c < grid[0].length-col; c++) {
					if (grid[row][col+c] == 0) {
						grid[row][col+c] = grid[row][col+c-1];
						grid[row][col+c-1] = 0;
					}
				}
			}
		}
	}

	/**
	 * Moves board up
	 * @param none
	 * @return void
	 **/
	public void moveUp() {
		for (int col = 0; col < grid[0].length; col++) { //add numbers
			for (int row = 0; row < grid.length-1; row++) {
				for (int r = row+1; r < grid.length; r++) {
					if (grid[row][col] == grid[r][col] && 
							grid[row][col] != 0) {
						if (grid[row+1][col] != 0 || 
								grid[r-1][col] != 0) {
							if (grid[row+1][col] != 
									grid[r][col] 
									&& grid[r-1][col] 
									!= grid[row][col]) {
								break;
									}
								}
						grid[row][col] *= 4;
						grid[r][col] = 0;
						score += grid[row][col]; //adds score
						break;
							}
				}
			}
		}
		for (int col = 0; col < grid[0].length; col++) { //shift numbers
			for (int row = 0; row < grid.length-1; row++) {
				for (int r = row+1; r < grid.length; r++) {
					if (grid[row][col] == 0) {
						grid[row][col] = grid[r][col];
						grid[r][col] = 0;
					}
				}
			}
		}
	}

	/**
	 * Moves board down
	 * @param none
	 * @return void
	 **/
	public void moveDown() {
		for (int col = 0; col < grid[0].length; col++) { //add numbers
			for (int row = grid.length-1; row > 0; row--) {
				for (int r = row-1; r >= 0; r--) {
					if (grid[row][col] == grid[r][col] && 
							grid[row][col] != 0) {					
						if (grid[row-1][col] != 0 || 
								grid[r+1][col] != 0) {
							if (grid[row-1][col] != 
									grid[r][col] 
									&& grid[r+1][col] 
									!= grid[row][col]) {
								break;
									}
								}		
						grid[row][col] *= 4;
						grid[r][col] = 0;
						score += grid[row][col]; //adds score
						break;	
							}
				}
			}
		}
		for (int col = 0; col < grid[0].length; col++) { //shift numbers
			for (int row = grid.length-1; row > 0; row--) {
				for (int r = row-1; r >= 0; r--) {
					if (grid[row][col] == 0) {
						grid[row][col] = grid[r][col];
						grid[r][col] = 0;
					}
				}
			}
		}
	}

	/**
	 * Determines if the board can move in a certain direction
	 * @param direction The direction the board should move in
	 * @return true If the board can move in the specific direction, 
	 * false If the board cannot move in the specific direction
	 **/
	public boolean canMove(Direction direction) {
		if (direction == Direction.LEFT) { //checks if board can move left
			return this.canMoveLeft();
		} else if (direction == Direction.RIGHT) { //checks if board can move right
			return this.canMoveRight();
		} else if (direction == Direction.UP) { //checks if board can move up
			return this.canMoveUp();
		} else if (direction == Direction.DOWN) { //checks if board can move down
			return this.canMoveDown();
		}
		return false;
	}

	/**
	 * Determines if the board can move left
	 * @param none
	 * @return true If the board can move left, false If the board 
	 * cannot move left
	 **/
	private boolean canMoveLeft() {
		for (int row = 0; row < grid.length; row++) { //first loop
			for (int col = 0; col < grid[0].length-1; col++) {
				for (int c = col+1; c < grid[0].length; c++) {
					if (grid[row][col] == grid[row][c] && 
							grid[row][col] != 0) {
						if (grid[row][col+1] == 
								grid[row][c] && 
								grid[row][c-1] 
								== grid[row][col]) {
							return true;
								}
						if (grid[row][c-1] != 0 || 
								grid[row][col+1] != 0) {
							if (grid[row][col+1] == 0 || 
									grid[row][c-1] == 0) {
								return true;
									}
								}
							}
				}
			}
		}
		for (int row = 0; row < grid.length; row++) { //second loop
			for (int col = 0; col < grid[0].length-1; col++) {
				for (int c = col+1; c < grid[0].length; c++) {
					if (grid[row][col] == 0 && grid[row][c] != 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Determines if the board can move right
	 * @param none
	 * @return true If the board can move right, false If the board 
	 * cannot move right
	 **/
	private boolean canMoveRight() {
		for (int row = 0; row < grid.length; row++) { //first loop
			for (int col = grid[0].length-1; col >= 1; col--) {
				for (int c = col-1; c >= 0; c--) {
					if (grid[row][col] == grid[row][c] && 
							grid[row][col] != 0) {
						if (grid[row][c+1] == grid[row][col] 
								&& grid[row][col-1] 
								== grid[row][c]) {
							return true;
								}
						if (grid[row][c+1] != 0 || 
								grid[row][col-1] != 0) {
							if (grid[row][c+1] == 0 || 
									grid[row][col-1] == 0) {
								return true;
									}
								}
							}
				}
			}
		}
		for (int row = 0; row < grid.length; row++) { //second loop
			for (int col = grid[0].length-1; col >= 1; col--) {
				for (int c = col-1; c >= 0; c--) {
					if (grid[row][col] == 0 && grid[row][c] != 0) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Determines if the board can move up
	 * @param none
	 * @return true If the board can move up, false If the board 
	 * cannot move up
	 **/
	private boolean canMoveUp() {
		for (int col = 0; col < grid[0].length; col++) { //first loop
			for (int row = 0; row < grid.length-1; row++) {
				for (int r = row+1; r < grid.length; r++) {
					if (grid[row][col] == grid[r][col] && 
							grid[row][col] != 0) {
						if (grid[row+1][col] == grid[r][col] 
								&& grid[r-1][col] 
								== grid[row][col]) {
							return true;
								}
						if (grid[row+1][col] != 0 || grid[r-1][col] != 0) {
							if (grid[row+1][col] == 0 || 
									grid[r-1][col] == 0) {
								return true;
									}
						}
							}
				}
			}
		}

		for (int col = 0; col < grid[0].length; col++) { //second loop
			for (int row = 0; row < grid.length-1; row++) {
				for (int r = row+1; r < grid.length; r++) {
					if (grid[row][col] == 0 && grid[r][col] != 0) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Determines if the board can move down
	 * @param none
	 * @return true If the board can move down, false If the board 
	 * cannot move down
	 **/
	private boolean canMoveDown() {
		for (int col = 0; col < grid[0].length; col++) { //first loop
			for (int row = grid.length-1; row > 0; row--) {
				for (int r = row-1; r >= 0; r--) {
					if (grid[row][col] == grid[r][col] && 
							grid[row][col] != 0) {
						if (grid[row-1][col] == grid[r][col] 
								&& grid[r+1][col] 
								== grid[row][col]) {
							return true;
								}
						if (grid[row-1][col] != 0 || 
								grid[r+1][col] != 0) {
							if (grid[row-1][col] == 0 || 
									grid[r+1][col] == 0) {
								return true;
									}
								}
							}
				}
			}
		}

		for (int col = 0; col < grid[0].length; col++) { //second loop
			for (int row = grid.length-1; row > 0; row--) {
				for (int r = row-1; r >= 0; r--) {
					if (grid[row][col] == 0 && grid[r][col] != 0) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Expands the current grid
	 * @param none
	 * @return void
	 **/
	public void expand() {
		GRID_SIZE++; //increments grid size
		int[][] newGrid = new int[GRID_SIZE][GRID_SIZE];
		for (int row = 0; row < GRID_SIZE-1; row++) { //deep copy
			for (int col = 0; col < GRID_SIZE-1; col++) {
				newGrid[row][col] = grid[row][col];
			}
		}
		grid = newGrid; //assigns grid to the expanded grid
	}

	/**
	 * Check to see if we have a game over
	 * @param none
	 * @return true If the game is over, false If the game is not over
	 **/
	public boolean isGameOver() {
		if (this.canMove(Direction.LEFT) == false && 
				this.canMove(Direction.RIGHT) == false && 
				this.canMove(Direction.UP) == false && 
				this.canMove(Direction.DOWN) == false) { 
			return true;
				}
		return false;
	}

	/**
	 * Return the reference to the 4Square Grid
	 * @param none
	 * @return grid
	 **/
	public int[][] getGrid() {
		return grid;
	}

	/**
	 * Return the score
	 * @param none
	 * @return score
	 **/
	public int getScore() {
		return score;
	}

	@Override
	/**
	 * Puts the grid in a StringBuilder object, then prints grid as a String
	 * @param none
	 * @return The grid as a String
	 **/
	public String toString() {
		StringBuilder outputString = new StringBuilder();
		outputString.append(String.format("Score: %d\n", score));
		for (int row = 0; row < GRID_SIZE; row++) {
			for (int column = 0; column < GRID_SIZE; column++)
				outputString.append(grid[row][column] == 0 ? "    -" :
						String.format("%5d", grid[row][column]));

			outputString.append("\n");
		}
		return outputString.toString();
	}
}
