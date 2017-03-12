/*
 * Name:  Allston Fojas
 * Email: amfojas@ucsd.edu
 * Login: cs8bwahf
 * Date:  March 8, 2017
 * File:  Gui4Square.java
 * Sources of Help: CSE 8B Textbook, TA's and Tutors, Google
 * File Header:
 * This file contains methods that creates and changes the user interface that 
 * the player sees when the player plays the game. This file also contains 
 * methods that put a game over screen to tell the player when the game is over.
 */

import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

/* 
 * Class Header: 
 * This class contains methods that I wrote that creates and changes the user 
 * interface that the player sees when the player plays the game. This class 
 * also contains the different colors and texts of the board and the actual
 * board that the player sees.
 */
public class Gui4Square extends Application
{
	private String outputBoard; // The filename for where to save the Board
	private Board board; // The 4Square Game Board

	private static final int TILE_WIDTH = 106;

	private static final int TEXT_SIZE_LOW = 55; // Low value tiles (2,4,8,etc)
	private static final int TEXT_SIZE_MID = 45; // Mid value tiles 
	//(128, 256, 512)
	private static final int TEXT_SIZE_HIGH = 35; // High value tiles 
	//(1024, 2048, Higher)

	// Fill colors for each of the Tile values
	private static final Color COLOR_EMPTY = Color.rgb(238, 228, 218, 0.35);
	private static final Color COLOR_2 = Color.rgb(255, 0, 0);
	private static final Color COLOR_4 = Color.rgb(237, 224, 0);
	private static final Color COLOR_8 = Color.rgb(0, 177, 121);
	private static final Color COLOR_16 = Color.rgb(0, 255, 0);
	private static final Color COLOR_32 = Color.rgb(0, 0, 255);
	private static final Color COLOR_64 = Color.rgb(246, 0, 59);
	private static final Color COLOR_128 = Color.rgb(237, 207, 114);
	private static final Color COLOR_256 = Color.rgb(237, 204, 97);
	private static final Color COLOR_512 = Color.rgb(237, 200, 80);
	private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
	private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
	private static final Color COLOR_OTHER = Color.BLACK;
	private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);

	private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242); 
	// For tiles >= 8

	private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101); 
	// For tiles < 8	

	private GridPane pane; //the grid pane

	private Rectangle[][] rectArray; //array of tiles on the GUI board
	private Text[][] textArray; //array of text numbers on the GUI board
	private Text title; //the display of the game title
	private Text score; //the display of the player's score
	private int STAGE_WIDTH = 600; //width of window
	private int STAGE_HEIGHT = 600; //height of window
	private StackPane pane2; //stack pane for the title and score
	private int scoreVal = 0; //the player's score
	private StackPane gameOverPane; //stack pane when the game is over
	private Text gameOver; //the game over text
	private double padNum1 = 11.5; //padding for the pane
	private double padNum2 = 12.5; //padding for the pane
	private double padNum3 = 13.5; //padding for the pane
	private double padNum4 = 14.5; //padding for the pane
	private int gapNum = 15; //horizontal and vertical gap for the grid pane
	private int pane2Margin = 25; //margin for the first stack pane
	private int pane2Margin2 = 100; //margin for the first stack pane
	private int gameOverSize = 80; //size of the game over text
	private int rectHeightChange = 200; //size of the tile height
	private int rectWidthChange = 150; //size of the tile width

	/**
	 * Creates the initial game board GUI that the player sees
	 * @param primaryStage The stage on which the game board GUI is on
	 * @return void
	 **/
	@Override
	public void start(Stage primaryStage)
	{
		// Process Arguments and Initialize the Game Board
		processArgs(getParameters().getRaw().toArray(new String[0]));
		// Create the pane that will hold all of the visual objects
		pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(padNum1, padNum2, padNum3, padNum4));
		pane.setStyle("-fx-background-color: rgb(187, 173, 160)");
		// Set the spacing between the Tiles
		pane.setHgap(gapNum); 
		pane.setVgap(gapNum);
		rectArray = new Rectangle[board.GRID_SIZE][board.GRID_SIZE];
		textArray = new Text[board.GRID_SIZE][board.GRID_SIZE];
		pane2 = new StackPane();
		pane2.getChildren().add(pane);
		Scene scene = new Scene(pane2, STAGE_WIDTH, STAGE_HEIGHT);
		primaryStage.setTitle("Gui4Square");
		primaryStage.setScene(scene);
		primaryStage.show();
		title = new Text(); //text for the title
		title.setText("4Square");
		title.setFont(Font.font("Arial", FontWeight.BOLD, TEXT_SIZE_MID));
		title.setFill(COLOR_OTHER);
		pane2.getChildren().add(title);
		pane2.setMargin(title, new Insets(pane2Margin, 0, 0, pane2Margin2));
		StackPane.setAlignment(title, Pos.TOP_LEFT);
		score = new Text(); //text for the score display
		score.setText("Score: " + scoreVal);
		score.setFont(Font.font("Arial", FontWeight.BOLD, TEXT_SIZE_HIGH));
		score.setFill(COLOR_OTHER);
		pane2.getChildren().add(score);
		pane2.setMargin(score, new Insets(pane2Margin, pane2Margin2, 0, 0));
		StackPane.setAlignment(score, Pos.TOP_RIGHT);
		this.setRandomTilesToBoard(); //sets random tiles to start game
		int[][] boardGrid = board.getGrid(); //gets int values from board
		for (int x = 0; x < board.GRID_SIZE; x++) {
			for (int y = 0; y < board.GRID_SIZE; y++) {
				resizeWindow(rectArray[y][x], primaryStage);
			}
		}
		scene.setOnKeyPressed(new myKeyHandler());
	}

	/**
	 * Sets the random tiles to the board GUI
	 * @param none
	 * @return void
	 **/
	public void setRandomTilesToBoard() {
		int[][] boardGrid = board.getGrid(); //gets int values from board
		for (int x = 0; x < board.GRID_SIZE; x++) {
			for (int y = 0; y < board.GRID_SIZE; y++) {
				Rectangle square = new Rectangle();
				square.setWidth(TILE_WIDTH);
				square.setHeight(TILE_WIDTH);
				square.setFill(COLOR_EMPTY);
				pane.add(square, x, y);
				rectArray[x][y] = square;
				Text number = new Text();
				number.setFont(Font.font("Arial", FontWeight.BOLD, 
							TEXT_SIZE_LOW));
				pane.add(number, x, y);
				textArray[x][y] = number;
				if (boardGrid[x][y] != 0) {
					String numberString = Integer.toString(
							boardGrid[x][y]);
					number.setText(numberString);
					if (boardGrid[x][y] == 4) {
						square.setFill(COLOR_2);
						number.setFill(COLOR_VALUE_DARK);
					} else if (boardGrid[x][y] == 16) {
						square.setFill(COLOR_4);
						number.setFill(COLOR_VALUE_DARK);
					}
					GridPane.setHalignment(number, HPos.CENTER);
				}
			}
		}
	}

	/**
	 * Updates the board GUI after each valid move
	 * @param none
	 * @return void
	 **/
	public void updateGUIBoard() {
		int[][] boardGrid = board.getGrid(); //gets int values from board
		for (int x = 0; x < board.GRID_SIZE; x++) {
			for (int y = 0; y < board.GRID_SIZE; y++) {
				if (boardGrid[x][y] != 0) {
					Text number = textArray[y][x];
					String numberString = Integer.toString(
							boardGrid[x][y]);
					number.setText(numberString);
					GridPane.setHalignment(number, HPos.CENTER);
					score.setText("Score: " + scoreVal);
				}
				if (boardGrid[x][y] == 0) {
					Text number = textArray[y][x];
					number.setText("");
					GridPane.setHalignment(number, HPos.CENTER);
					score.setText("Score: " + scoreVal);
				}
			}
		}
		this.updateGUIText(); //updates the GUI text
		this.updateGUIColor(); //updates the GUI colors
	}

	/**
	 * Updates the board GUI with different colors corresponding to the 
	 * number
	 * @param none
	 * @return void
	 **/
	public void updateGUIColor() {
		int[][] boardGrid = board.getGrid(); //gets int values from board
		for (int x = 0; x < board.GRID_SIZE; x++) {
			for (int y = 0; y < board.GRID_SIZE; y++) {
				if (boardGrid[x][y] == 0) {
					rectArray[y][x].setFill(COLOR_EMPTY);
				} else if (boardGrid[x][y] == 4) {
					rectArray[y][x].setFill(COLOR_2);
					textArray[y][x].setFill(COLOR_VALUE_DARK);
				} else if (boardGrid[x][y] == 16) {
					rectArray[y][x].setFill(COLOR_4);
					textArray[y][x].setFill(COLOR_VALUE_DARK);
				} else if (boardGrid[x][y] == 64) {
					rectArray[y][x].setFill(COLOR_16);
					textArray[y][x].setFill(COLOR_VALUE_LIGHT);
				} else if (boardGrid[x][y] == 256) {
					rectArray[y][x].setFill(COLOR_32);
					textArray[y][x].setFill(COLOR_VALUE_LIGHT);
				} else if (boardGrid[x][y] == 1024) {
					rectArray[y][x].setFill(COLOR_64);
					textArray[y][x].setFill(COLOR_VALUE_LIGHT);
				} else if (boardGrid[x][y] == 4096) {
					rectArray[y][x].setFill(COLOR_128);
					textArray[y][x].setFill(COLOR_VALUE_LIGHT);
				} else if (boardGrid[x][y] > 4096) {
					rectArray[y][x].setFill(COLOR_OTHER);
					textArray[y][x].setFill(COLOR_VALUE_LIGHT);
				}
			}
		}
	}

	/**
	 * Updates the board GUI with different text corresponding to the 
	 * number
	 * @param none
	 * @return void
	 **/
	public void updateGUIText() {
		int[][] boardGrid = board.getGrid(); //gets int values from board
		for (int x = 0; x < board.GRID_SIZE; x++) {
			for (int y = 0; y < board.GRID_SIZE; y++) {
				if (boardGrid[x][y] == 4) {
					textArray[y][x].setFont(Font.font("Arial", 
								FontWeight.BOLD, 
								TEXT_SIZE_LOW));
				} else if (boardGrid[x][y] == 16) {
					textArray[y][x].setFont(Font.font("Arial", 
								FontWeight.BOLD, 
								TEXT_SIZE_LOW));
				} else if (boardGrid[x][y] == 64) {
					textArray[y][x].setFont(Font.font("Arial", 
								FontWeight.BOLD, 
								TEXT_SIZE_LOW));
				} else if (boardGrid[x][y] == 256) {
					textArray[y][x].setFont(Font.font("Arial", 
								FontWeight.BOLD, 
								TEXT_SIZE_MID));
				} else if (boardGrid[x][y] == 1024) {
					textArray[y][x].setFont(Font.font("Arial", 
								FontWeight.BOLD, 
								TEXT_SIZE_HIGH));
				} else if (boardGrid[x][y] == 4096) {
					textArray[y][x].setFont(Font.font("Arial", 
								FontWeight.BOLD, 
								TEXT_SIZE_HIGH));
				} else if (boardGrid[x][y] > 4096) {
					textArray[y][x].setFont(Font.font("Arial", 
								FontWeight.BOLD, 
								TEXT_SIZE_HIGH));
				}
			}
		}
	}

	/**
	 * Puts the game over screen when the game is over
	 * @param none
	 * @return void
	 **/
	public void putGameOverScreen() {
		gameOverPane = new StackPane(); //creates a new pane for game over
		pane2.getChildren().add(gameOverPane);
		gameOverPane.setStyle("-fx-background-color: rgb(238, 228, 218, 0.73)");
		gameOver = new Text();
		gameOver.setText("Game Over!");
		gameOver.setFont(Font.font("Arial", FontWeight.BOLD, gameOverSize));
		gameOver.setFill(COLOR_OTHER);
		pane2.getChildren().add(gameOver);
		StackPane.setAlignment(gameOver, Pos.CENTER);
	}

	/**
	 * Resizes the window of the game board
	 * @param mySquare The current rectangle on the board, primaryStage The 
	 * stage on which the board is on
	 * @return void
	 **/
	private void resizeWindow(Rectangle mySquare, Stage primaryStage) {
		int boardSize = board.GRID_SIZE;
		int heightChange = rectHeightChange + (boardSize-1) * gapNum;
		mySquare.heightProperty().bind((primaryStage.heightProperty().subtract(
						heightChange)).divide(boardSize));
		int widthChange = rectWidthChange + (boardSize-1) * gapNum;
		mySquare.widthProperty().bind((primaryStage.widthProperty().subtract(
						widthChange)).divide(boardSize));
	}

	/* 
	 * Class Header: 
	 * This class contains a method that I wrote that handles all of the key 
	 * board presses that the player does to play the game. This class 
	 * implements the EventHandler interface to help with handling the key 
	 * presses.
	 */
	private class myKeyHandler implements EventHandler<KeyEvent>
	{
		/**
		 * Handles the key presses done by the player
		 * @param e The key event done by the player
		 * @return void
		 **/
		@Override
		public void handle(KeyEvent e)
		{
			if (e.getCode() == KeyCode.UP) { //moves board up
				if (board.move(Direction.UP)) {
					System.out.println("Moving Up");
					board.addRandomTile();
					scoreVal = board.getScore();
					updateGUIBoard();
					if (board.isGameOver()) {
						putGameOverScreen();
					}
				}
			} else if (e.getCode() == KeyCode.DOWN) { //moves board down
				if (board.move(Direction.DOWN)) {
					System.out.println("Moving Down");
					board.addRandomTile();
					scoreVal = board.getScore();
					updateGUIBoard();
					if (board.isGameOver()) {
						putGameOverScreen();
					}
				}
			} else if (e.getCode() == KeyCode.LEFT) { //moves board left
				if (board.move(Direction.LEFT)) {
					System.out.println("Moving Left");
					board.addRandomTile();
					scoreVal = board.getScore();
					updateGUIBoard();
					if (board.isGameOver()) {
						putGameOverScreen();
					}
				}
			} else if (e.getCode() == KeyCode.RIGHT) { //moves board right
				if (board.move(Direction.RIGHT)) {
					System.out.println("Moving Right");
					board.addRandomTile();
					scoreVal = board.getScore();
					updateGUIBoard();
					if (board.isGameOver()) {
						putGameOverScreen();
					}
				}
			} else if (e.getCode() == KeyCode.S) { //saves board
				try {
					board.saveBoard(outputBoard);
					System.out.println("Saving Board to 4Square.board");
				} catch (IOException ex) { 
					System.out.println("saveBoard threw an Exception");
				}			
			} else if (e.getCode() == KeyCode.R) { //rotates board
				if (!(board.isGameOver())) {
					board.flip(3); //rotates clockwise
					System.out.println("Rotating Board");
					updateGUIBoard();
				} else if (board.isGameOver()) {
					putGameOverScreen();
				}
			}
		}
	}

	/** DO NOT EDIT BELOW */

	/**
	 * Process the command line arguments
	 * @param args The command line arguments
	 * @return void
	 **/
	// The method used to process the command line arguments
	private void processArgs(String[] args)
	{
		String inputBoard = null;   // The filename for where to load the Board
		int boardSize = 0;          // The Size of the Board

		// Arguments must come in pairs
		if((args.length % 2) != 0)
		{
			printUsage();
			System.exit(-1);
		}

		// Process all the arguments 
		for(int i = 0; i < args.length; i += 2)
		{
			if(args[i].equals("-i"))
			{   // We are processing the argument that specifies
				// the input file to be used to set the board
				inputBoard = args[i + 1];
			}
			else if(args[i].equals("-o"))
			{   // We are processing the argument that specifies
				// the output file to be used to save the board
				outputBoard = args[i + 1];
			}
			else if(args[i].equals("-s"))
			{   // We are processing the argument that specifies
				// the size of the Board
				boardSize = Integer.parseInt(args[i + 1]);
			}
			else
			{   // Incorrect Argument 
				printUsage();
				System.exit(-1);
			}
		}

		// Set the default output file if none specified
		if(outputBoard == null)
			outputBoard = "4Square.board";
		// Set the default Board size if none specified or less than 2
		if(boardSize < 2)
			boardSize = 4;

		// Initialize the Game Board
		try{
			if(inputBoard != null)
				board = new Board(new Random(), inputBoard);
			else
				board = new Board(new Random(), boardSize);
		}
		catch (Exception e)
		{
			System.out.println(e.getClass().getName() + 
					" was thrown while creating a " +
					"Board from file " + inputBoard);
			System.out.println("Either your Board(String, Random) " +
					"Constructor is broken or the file isn't " +
					"formated correctly");
			System.exit(-1);
		}
	}

	/**
	 * Print the Usage Message
	 * @param none
	 * @return void
	 **/
	// Print the Usage Message 
	private static void printUsage()
	{
		System.out.println("Gui4Square");
		System.out.println("Usage:  Gui4Square [-i|o file ...]");
		System.out.println();
		System.out.println("  Command line arguments come in pairs of the "+ 
				"form: <command> <argument>");
		System.out.println();
		System.out.println("  -i [file]  -> Specifies a 4Square board that " + 
				"should be loaded");
		System.out.println();
		System.out.println("  -o [file]  -> Specifies a file that should be " + 
				"used to save the 4Square board");
		System.out.println("                If none specified then the " + 
				"default \"4Square.board\" file will be used");  
		System.out.println("  -s [size]  -> Specifies the size of the 4Square" + 
				"board if an input file hasn't been"); 
		System.out.println("                specified.  If both -s and -i" + 
				"are used, then the size of the board"); 
		System.out.println("                will be determined by the input" +
				" file. The default size is 4.");
	}
}
