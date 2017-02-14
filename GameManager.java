/*
 * Name:  Allston Fojas
 * Email: allstonfojas@gmail.com
 * Date:  February 7, 2017
 * File:  GameManager.java
 * File Header:
 * This file contains methods that create an object to manage the 4Sqaure game 
 * and be able to play the game. This file contains the method to allow the 
 * user to properly play the 4Sqaure game with added functionality, such as being 
 * able to expand the board and undo the last move. This file also contains 
 * the method to print the controls of the game to the user.
 */

import java.util.*;
import java.io.*;

/* 
 * Class Header: 
 * This class contains different methods that I wrote that create an object to 
 * manage the 4Sqaure game and be able to play the game. This class contains the 
 * actual 4Sqaure board that the user plays and the file to save the board to 
 * when exiting the game.
 */
public class GameManager {
	// Instance variables
	private Board board; // The actual 4Sqaure board
	private String outputFileName; // File to save the board to when exiting

	/**
	 * GameManager Constructor, Generate new game
	 * @param outputBoard The name of the output file, boardSize The size 
	 * of the board, random A random object
	 * @return none, it's a constructor
	 **/
	public GameManager(String outputBoard, int boardSize, Random random) {
		System.out.println("Generating a New Board");
		board = new Board(random, boardSize); //makes a new board
		outputFileName = outputBoard; //makes outputFileName the same as 
		//outputBoard
	}


	/**	
	 * GameManager Constructor, Load a saved game
	 * @param inputBoard The name of the input file, outputBoard The name 
	 * of the output file, random A random object
	 * @return none, it's a constructor
	 **/
	public GameManager(String inputBoard, String outputBoard, Random random) 
		throws IOException {
		System.out.println("Loading Board from " + inputBoard);
		board = new Board(random, inputBoard); //makes a new board
		outputFileName = outputBoard; //makes outputFileName the same as 
		//outputBoard
	}

	// Main play loop
	// Takes in input from the user to specify moves to execute
	// valid moves are:
	//      k - Move up
	//      j - Move Down
	//      h - Move Left	
	//      l - Move Right
	//      q - Quit and Save Board
	//
	//  If an invalid command is received then print the controls
	//  to remind the user of the valid moves.
	//
	//  Once the player decides to quit or the game is over,
	//  save the game board to a file based on the outputFileName
	//  string that was set in the constructor and then return
	//  If the game is over print "Game Over!" to the terminal
	/**
	 * Takes in input from the user to specify moves to execute
	 * valid moves
	 * @param none
	 * @return void
	 **/
	public void play() throws IOException {
		int numOfMoves = 0;
		int numTimesUndo = 0;
		Scanner controls = new Scanner(System.in); //reads input from user
		this.printControls(); //prints controls for user
		System.out.println(board);
		System.out.println("Please enter a valid command");
		while (board.isGameOver() == false) { //allows user to keep playing 
			//until the game is over
			String myMove = controls.next(); //gets user's command
			if (myMove.equals("q") || board.isGameOver() == true) {
				board.saveBoard(outputFileName);
				System.out.println("Game Over! Board is saved");
				return;
			}
			if (myMove.equals("k")) { //moves board up
				board.saveBoard(outputFileName);
				boolean b = board.move(Direction.UP);
				if (b == true) {
					numOfMoves++;
					board.addRandomTile();
					System.out.println(board);
					System.out.println("Board has moved. " + 
							"Please enter another " + 
							"valid command.");
				} else {
					System.out.println(board);
					System.out.println("Error: board cannot " + 
							"move in that direction. " + 
							"Please enter a valid " + 
							"command");
				}
			} else if (myMove.equals("j")) { //moves board down
				board.saveBoard(outputFileName);
				boolean b = board.move(Direction.DOWN);
				if (b == true) {
					numOfMoves++;
					board.addRandomTile();
					System.out.println(board);
					System.out.println("Board has moved. " + 
							"Please enter another " + 
							"valid command.");
				} else {
					System.out.println(board);
					System.out.println("Error: board cannot " + 
							"move in that direction. " + 
							"Please enter a valid " + 
							"command");
				}
			} else if (myMove.equals("h")) { //moves board left
				board.saveBoard(outputFileName);
				boolean b = board.move(Direction.LEFT);
				if (b == true) {
					numOfMoves++;
					board.addRandomTile();
					System.out.println(board);
					System.out.println("Board has moved. " + 
							"Please enter another " + 
							"valid command.");
				} else {
					System.out.println(board);
					System.out.println("Error: board cannot " + 
							"move in that direction. " + 
							"Please enter a valid " + 
							"command");
				}
			} else if (myMove.equals("l")) { //moves board right
				board.saveBoard(outputFileName);
				boolean b = board.move(Direction.RIGHT);
				if (b == true) {
					numOfMoves++;
					board.addRandomTile();
					System.out.println(board);
					System.out.println("Board has moved. " + 
							"Please enter another " + 
							"valid command.");
				} else {
					System.out.println(board);
					System.out.println("Error: board cannot " + 
							"move in that direction. " + 
							"Please enter a valid " + 
							"command");
				}
			} else if (myMove.equals("u")) { //undoes last move
				if (numOfMoves == 0) {
					System.out.println(board);
					System.out.println("Error: User has " + 
							"not made any moves " + 
							"yet. Please enter a " + 
							"valid command");
				} else if (numTimesUndo == 2) {
					System.out.println(board);
					System.out.println("Error: User can " + 
							"only undo board once. " + 
							"Please enter a valid " + 
							"command");
					numTimesUndo = 0;
				} else {
					board.undo();
					System.out.println(board);
					System.out.println("User has selected " + 
							"undo command " + 
							"successfully. Please " + 
							"enter another valid " + 
							"command");
					numTimesUndo++;
					numOfMoves++;
				}
			} else if (myMove.equals("e")) { //expands board
				numOfMoves++;
				board.saveBoard(outputFileName);
				board.expand();
				System.out.println(board);
				System.out.println("Board has expanded " + 
						"successfully. Please enter " + 
						"another valid command.");	
			} else { //prompts user to enter a valid command
				this.printControls();
				System.out.println(board);
				System.out.println("Error: invalid command. " + 
						"Please enter a valid command");
			}
		}
		if (board.isGameOver() == true) { //saves board when game is over
			board.saveBoard(outputFileName);
			System.out.println("Game Over! Board is saved");
			return;
		}
	}

	/**
	 * Print the Controls for the Game
	 * @param none
	 * @return void
	 **/
	private void printControls() {
		System.out.println("  Controls:");
		System.out.println("    k - Move Up");
		System.out.println("    j - Move Down");
		System.out.println("    h - Move Left");
		System.out.println("    l - Move Right");
		System.out.println("    e - Expand Board");
		System.out.println("    u - Undo Last Move");
		System.out.println("    q - Quit and Save Board");
		System.out.println();
	}
}
