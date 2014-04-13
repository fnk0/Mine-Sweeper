package main;
/**
 * This class will handle the mouse occurances that can happen during the game.
 * 
 * @author Marcus Gabilheri
 * @version 1.0
 * @since Feb 15, 2014
 */
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class MouseClickHandler extends MouseAdapter {

	private GameLogic game;
	private JButton[][] buttons;
	private GamePanel panel;
	private CounterPanel counterPanel;
	private boolean enteredBomb;
	private boolean cheatMode = false;
	public final static int BOMB_IMAGE = 10;
	public final static int FLAG_IMAGE = 11;
	public final static int EMPTY_IMAGE = 12;
	public final static int ROLLOVER_IMAGE = 13;

	/**
	 * Cosntructor for the class
	 * 
	 * @param game
	 *            current game being played
	 * @param buttons
	 *            the 2D array of JButtons related to the board
	 * @param pressedButton
	 *            the pressedButton
	 * @param border
	 *            the size of it's button border.
	 */
	public MouseClickHandler(GameLogic game, JButton[][] buttons, GamePanel panel,
			CounterPanel counterPanel) {
		this.game = game;
		this.buttons = buttons;
		this.panel = panel;
		this.counterPanel = counterPanel;
	}

	/**
	 * Gets the name of the Image resource for the button
	 * 
	 * @param result
	 *            take as param the result of surrounding mines, a bomb or a
	 *            flag for a specific cell
	 * @return the path to the graphics asset
	 */
	public String getImageNameResource(int result) {

		if (result == GameLogic.BOMB) {
			return "assets//bomb.png";
		} else if (result == GameLogic.FLAG) {
			return "assets//flag.png";
		} else if (result == GameLogic.EMPTY) {
			return "assets//empty.png";
		}
		return "assets//number" + result + ".png";
	}

	/**
	 * Gets the ImageIcon for this object
	 * 
	 * @param result
	 *            the path to a image asset
	 * @return ImageIcon an ImageIcon.
	 */
	public ImageIcon getImageResource(String result, JButton button) {

		ImageIcon buttonIcon = new ImageIcon(getClass().getResource(result));
		ImageIcon buttonIconResized = new ImageIcon(buttonIcon.getImage()
				.getScaledInstance((int) button.getBounds().getWidth(),
						(int) button.getBounds().getHeight(),
						java.awt.Image.SCALE_SMOOTH));
		return buttonIconResized;
	}

	/**
	 * @Override the mousePressed class handles when a button has been pressed
	 * @param event
	 */
	@Override
	public void mousePressed(MouseEvent event) {	

		if (game.isGameOn()) {
			int result = 'D';
			JButton button = (JButton) event.getSource();
			String[] getParams = button.getToolTipText().split(" ");
			int row = Integer.parseInt(getParams[0]);
			int col = Integer.parseInt(getParams[1]);
			boolean winTheGame = false;
			int playedCell = game.checkPlayedBoard(row, col);
			int[][] board = game.getBoard();
			
			// Right Click stuff
			if (SwingUtilities.isRightMouseButton(event)) {
				if (playedCell != 'Y') {
					if (playedCell == 'F') {
						game.setPlayedBoardValue(row, col, 'N');
						result = MouseClickHandler.EMPTY_IMAGE;
						counterPanel.setBombCounter(true);
					} else {
						game.setPlayedBoardValue(row, col, 'F');
						result = MouseClickHandler.FLAG_IMAGE;
						winTheGame = game.setFlag(row, col);
						counterPanel.setBombCounter(false);
					}
				}
			} else {
				// Normal click stuff
				if (playedCell == 'N'
						&& playedCell != 'F') {
					result = game.move(row, col, false);
					if (result == GameLogic.BOMB) {
						for (int r = 0; r < board.length; r++) {
							for (int c = 0; c < board[0].length; c++) {
								if (board[r][c] == GameLogic.BOMB) {
									buttons[r][c].setIcon(MineSweeperFrame.getIcon(MouseClickHandler.BOMB_IMAGE));
								}
							}
						}
						counterPanel.setFaceName(CounterPanel.LOOSE_FACE);
						counterPanel.stopTimer();
						
						ImageIcon gameOverIcon = new ImageIcon(getClass().getResource("assets//game_over.png"));
						
						JOptionPane.showConfirmDialog(null, null, null, 
								JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
								gameOverIcon);

					}
					// Cascade Effect!
					if (result == 0) {
						getSurroundingZeros(row, col);
						game.setPlayedBoardValue(row, col, 'Y');
					}
				}
			}
			// Setting the icon part
			if (result != 'D' && result != GameLogic.BOMB) {
				if(result != 0 && result != MouseClickHandler.FLAG_IMAGE && result != MouseClickHandler.EMPTY_IMAGE) {
					game.setPlayedBoardValue(row, col, 'Y');
				}
				buttons[row][col].setIcon(MineSweeperFrame.getIcon(result));
				if(result != MouseClickHandler.EMPTY_IMAGE) {
					buttons[row][col].setRolloverIcon(null);
				} else { 
					buttons[row][col].setRolloverIcon(MineSweeperFrame.getIcon(MouseClickHandler.ROLLOVER_IMAGE));
				}
				if(winTheGame) {
					counterPanel.setFaceName(CounterPanel.WIN_FACE);
					counterPanel.stopTimer();
					ImageIcon winnerIcon = new ImageIcon(getClass().getResource("assets//winner.png"));
					ImageIcon resizedWinnerIcon = new ImageIcon(winnerIcon.getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH));
					JOptionPane.showConfirmDialog(null, "YOU WIN!!!!", null,
							JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, resizedWinnerIcon);
				}
			}
			//game.print2DArray(game.getPlayedBoard()); Debugging stuff
			panel.repaint();
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(cheatMode) {
			JButton button = (JButton) e.getSource();
			String[] getParams = button.getToolTipText().split(" ");
			int row = Integer.parseInt(getParams[0]);
			int col = Integer.parseInt(getParams[1]);
			if (game.isBomb(row, col)) {
				// Prints to the console bomb when I hover a mouse over something. For debugging purposes.
				//System.out.println("BOMB!!");	
				counterPanel.setFaceName(CounterPanel.CHEAT_FACE);
				enteredBomb = true;
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(cheatMode) {
			if(enteredBomb) {
				counterPanel.setFaceName(CounterPanel.DEFAULT_FACE);
				enteredBomb = false;
			}
		}	
	}

	/**
	 * Gets the surrounding zeros for this element
	 * 
	 * @param row the specific row for this move
	 * @param col the specific column for this move
	 */
	public void getSurroundingZeros(int row, int col) {
		game.setPlayedBoardValue(row, col, 'Y');
		int result =  'Z';
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				if(r == 1 && c == 1) {
					continue;
				}
				try {
					if (game.checkPlayedBoard((row + r) - 1, (col + c) - 1) != 'N') {
						continue;
					}
					
					result = game.move((row + r) - 1, (col + c) - 1, false);					
					buttons[(row + r) - 1][(col + c) - 1].setIcon(MineSweeperFrame.getIcon(result));
					buttons[(row + r) - 1][(col + c) - 1].setRolloverIcon(null);
					
							
				} catch (ArrayIndexOutOfBoundsException ex) {
					continue;
				}
				
				if (result == 0) {
					if (game.checkPlayedBoard((row + r) - 1, (col + c) - 1) == 'N') {
						getSurroundingZeros((row + r) - 1, col + c - 1);
					} 
				} else {
					game.setPlayedBoardValue((row + r) -1, (col + c) -1, 'Y');
				}
			}
		}	
	}
}
