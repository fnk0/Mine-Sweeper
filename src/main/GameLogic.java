package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

/**
 * 
 * @author Marcus Gabilheri
 * @version 1.0
 * @since Feb 15, 2014 This class handles the logic behind this implementation
 *        of the classic game Mine Sweeper
 */
public class GameLogic implements Serializable {

	private static final long serialVersionUID = -1953371603039334266L;
	private int[][] board, playedBoard;
	private int bombs, level, row, col;
	private boolean firstMove;
	private int gameStatus;

	// Constants to keep my sanity!!
	public static final int GAME_ON = 499;
	public static final int GAME_OFF = 500;
	public static final int NEW_GAME = 501;
	public static final int FIRST_GAME = 502;
	public static final int BOMB = 199;
	public static final int FLAG = 'F';
	public static final int EMPTY = '?';
	public static final int EASY = 0;
	public static final int MEDIUM = 1;
	public static final int HARD = 2;
	public static final int CUSTOM = 3;
	public static final int QUIT = 4;
	public static final int LOAD = 5;
	public static final int SAVE = 6;

	/**
	 * Empty Constructor
	 */
	public GameLogic() {
		level = GameLogic.EASY;
		firstMove = true;
	}
	
	/**
	 * saveGame loads an file that holds the current state of a game.
	 * @param gameLogic the game to be saved
	 * @param gameName the name of the file
	 */	
	public void saveGame(GameLogic gameLogic, String path) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(path)));
			out.writeObject(gameLogic);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * LoadGame loads an file and starts a new game based on the current state of the file.
	 * @param gameName the name of the file.
	 * @return gameLogic an GameLogic object holding the current status of a game
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("resource")
	public void loadGame(String filePath, MineSweeperFrame frame) {
		GameLogic gameLogic = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(filePath)));
		
			try {
				gameLogic = (GameLogic) in.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}						
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		frame.newGame(gameLogic, true);
		
	}

	/**
	 * Constructor were the user can specify how many cells and how many bombs
	 * he wants
	 * 
	 * @aparam level the level of the game
	 * @param row
	 *            number of rows
	 * @param col
	 *            number of cells
	 * @param bombs
	 *            number of rows
	 */
	public GameLogic(int level, int row, int col, int bombs) {
		this.level = level;
		this.row = row;
		this.col = col;
		board = new int[row][col];
		playedBoard = new int[row][col];
		this.bombs = bombs;
		firstMove = true;
	}

	/**
	 * Constructor based on the level chosen for the game. takes 4 main options
	 * EASY = 9x9 with 10 bombs MEDIUM = 15x15 with 20 bombs HARD = 20x20 with
	 * 30 bombs EXPERT = 25x25 with 40 bombs
	 * 
	 * @param level
	 *            the level of the game
	 */
	public GameLogic(int level) {
		this.level = level;
		firstMove = true;
		switch (level) {
		case EASY:
			board = new int[9][9];
			playedBoard = new int[9][9];
			bombs = 10;
			break;
		case MEDIUM:
			board = new int[15][15];
			playedBoard = new int[15][15];
			bombs = 25;
			break;
		case HARD:
			board = new int[20][20];
			playedBoard = new int[20][20];
			bombs = 60;
			break;
		case CUSTOM:
			board = new int[row][col];
			playedBoard = new int[row][col];
			break;
		}
	}
	
	/**
	 * Starts the game.
	 */
	public void startGame() {
		gameStatus = GAME_ON;
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[0].length; c++) {
				board[r][c] = '?';
			}
		}

		for (int r = 0; r < playedBoard.length; r++) {
			for (int c = 0; c < playedBoard[0].length; c++) {
				playedBoard[r][c] = 'N';
			}
		}
	}

	/**
	 * Prints an 2D integer array
	 * 
	 * @param array2d
	 */
	public void print2DArray(int[][] array2D) {
		for (int r = 0; r < array2D.length; r++) {
			for (int c = 0; c < array2D[0].length; c++) {
				if (array2D[r][c] == EMPTY || board[r][c] == BOMB) {
					System.out.print("[" + "?" + "]");
				} else if (array2D[r][c] == FLAG) {
					System.out.print("[" + "F" + "]");
				} else {
					System.out.print("[" + array2D[r][c] + "]");
				}
			}
			System.out.println();
		}
	}

	/**
	 * Generates Bombs and places randomly on the board.
	 */
	public void generateBombs() {
		Random random = new Random();

		for (int i = bombs; i > 0; i--) {
			int row = random.nextInt(board.length);
			int col = random.nextInt(board[0].length);

			if (board[row][col] == GameLogic.BOMB) {
				i++;
				continue;
			} else {
				board[row][col] = GameLogic.BOMB;
			}
		}
	}

	/**
	 * Generates a single bomb
	 */
	public void generateBomb() {
		Random random = new Random();
		int row = random.nextInt(board.length);
		int col = random.nextInt(board.length);
		if (board[row][col] != GameLogic.BOMB) {
			board[row][col] = GameLogic.BOMB;
		} else {
			generateBomb();
		}
	}

	/**
	 * Checks if the games is in ON or OFF Mode
	 * 
	 * @return boolean with the game status
	 */
	public boolean isGameOn() {
		if (gameStatus == GAME_ON) {
			return true;
		}

		return false;
	}

	/**
	 * Checks if the specific cell is a bomb or not
	 * 
	 * @param row
	 *            the specific row for this move
	 * @param col
	 *            the specific column for this move
	 * @return boolean
	 */
	public boolean isBomb(int row, int col) {

		if (board[row][col] == BOMB) {
			return true;
		}

		return false;
	}

	/**
	 * Gets how many surrounding bombs there is for a specific cell.
	 * 
	 * @param row
	 *            the specific row for this move
	 * @param col
	 *            the specific column for this move
	 * @return int with the number of surrounding bombs
	 */
	public int getSurroundingBombs(int row, int col) {
		int counter = 0;

		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				try {
					if (board[(row + r) - 1][(col + c) - 1] == BOMB) {
						counter++;
					}
				} catch (ArrayIndexOutOfBoundsException ex) {
				}
			}
		}
		return counter;
	}

	/**
	 * Handles whenever the player makes a move to a specific cell
	 * 
	 * @param row
	 *            the specific row for this move
	 * @param col
	 *            the specific column for this move
	 * @param isFirstTime
	 *            boolean if is the first time that the player is playing will
	 *            not check for bombs in that spot
	 * @return int the number of surrounding bombs or the a bomb
	 */
	public int move(int row, int col, boolean isFirstTime) {
		if (isFirstTime != true) {
			if (isBomb(row, col)) {
				// System.out.println("GAME OVER");
				if (firstMove) {
					generateBomb();
					board[row][col] = '?';
					bombs--;
				} else {
					gameStatus = GAME_OFF;
					return GameLogic.BOMB;
				}
			}
		}
		firstMove = false;
		board[row][col] = getSurroundingBombs(row, col);

		return getSurroundingBombs(row, col);
	}

	/**
	 * Sets that specific cell to a FLAG IF all the bombs have been flagged the
	 * player wins the game
	 * 
	 * @param row
	 *            the specific row for this move
	 * @param col
	 *            the specific col for this move
	 */
	public boolean setFlag(int row, int col) {
		if (board[row][col] == BOMB) {
			bombs--;
			if (bombs == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if this specific cell have been played or not
	 * 
	 * @param row
	 *            the specific row for this move
	 * @param col
	 *            the specific column for this move
	 * @return an int to specify if that spot has been played or not.
	 */
	public int checkPlayedBoard(int row, int col) {

		int checked = 0;

		try {
			checked = playedBoard[row][col];
		} catch (ArrayIndexOutOfBoundsException ex) {

		}

		return checked;
	}

	/**
	 * Sets the value of that specific cell in the playedBoard
	 * 
	 * @param row
	 *            the specific row for this move
	 * @param colthe
	 *            specific column for this move
	 * @param value
	 *            the value to set that specific cell;
	 */
	public void setPlayedBoardValue(int row, int col, int value) {
		playedBoard[row][col] = value;
	}

	/**
	 * gets the current playedBoard
	 * 
	 * @return int[][] the played board to see which cells have been played
	 *         already
	 */
	public int[][] getPlayedBoard() {
		return playedBoard;
	}

	/**
	 * Gets the level for this game
	 * 
	 * @return the level of the game
	 */
	public int getGameLevel() {
		return level;
	}

	/**
	 * Gets the current bomb count for this game
	 * 
	 * @return the bomb count
	 */
	public int getBombCount() {
		return bombs;
	}

	/**
	 * Gets the number of rows
	 * 
	 * @return int the number of rows
	 */
	public int getBoardRowSize() {
		return board.length;
	}

	/**
	 * Gets the number of columns
	 * 
	 * @return the number of columns
	 */
	public int getBoardColumnSize() {
		return board[0].length;
	}

	/**
	 * Gets the board for this current game
	 * 
	 * @return int[][] the current board
	 */
	public int[][] getBoard() {
		return board;
	}

	/**
	 * Sets the Game to it's ON state mode
	 */
	public void setGameON() {
		gameStatus = GAME_ON;
	}

	/**
	 * Sets the game to it's OFF state mode
	 */
	public void setGameOFF() {
		gameStatus = GAME_OFF;
	}
}