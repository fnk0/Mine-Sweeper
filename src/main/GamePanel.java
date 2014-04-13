package main;

/**
 * GamePanel class, here's where the magic happens! :)
 * @author Marcus Gabilheri
 * @since Feb 15, 2014
 * @version 1.0
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GamePanel extends JPanel {
	
	private static final long serialVersionUID = 3176389280878672389L;
	private JButton[][] buttons;
	private GameLogic gameLogic;
	
	/**
	 * Constructor for the GamePanel class Takes a Game as a parameter
	 * 
	 * @param game
	 *            the current game for this panel
	 */
	public GamePanel(GameLogic game, CounterPanel counterPanel, boolean loadGame) {
		gameLogic = game;
		int row = game.getBoardRowSize();
		int col = game.getBoardColumnSize();
		//game.print2DArray(game.getBoard());
		setBackground(Color.BLACK);

		buttons = new JButton[row][col];
		//System.out.println("Buttons Rows: " + buttons.length);
		//System.out.println("Buttons Columns: " + buttons[0].length);
		setLayout(new GridLayout(row, col));
		
		ImageIcon empty = new ImageIcon(getClass().getResource(
				"assets//number12.png"));
		ImageIcon emptyHover = new ImageIcon(getClass().getResource(
				"assets//number13.png"));
		
		if(!loadGame) { 
			for (int r = 0; r < row; r++) {
				for (int c = 0; c < col; c++) {
	
					JButton x = new JButton(null, empty);
					x.setRolloverIcon(emptyHover);
					x.setToolTipText(r + " " + c);
					x.setBorder(BorderFactory.createLoweredBevelBorder());
					x.setVerticalAlignment(SwingConstants.CENTER);
					x.setHorizontalAlignment(SwingConstants.CENTER);
					x.setIconTextGap(0);
					x.addMouseListener(new MouseClickHandler(game, buttons, this,
							counterPanel));
					buttons[r][c] = x;
				}
			}
			for (JButton[] bR : buttons) {
				for (JButton bC : bR) {
					add(bC);
				}
			}
			game.startGame();
			game.generateBombs();
		} else {
			//System.out.println("Loading the game....");
			int[][] boardGame = game.getBoard();
			//game.print2DArray(boardGame);
			for (int r = 0; r < row; r++) {
				for (int c = 0; c < col; c++) {
					JButton x = new JButton();
					
					if(boardGame[r][c] == GameLogic.EMPTY || boardGame[r][c] == GameLogic.BOMB) {
						x.setIcon(empty);
					} else if(boardGame[r][c] != GameLogic.BOMB) {
						//System.out.println("assets//number" + boardGame[r][c] + ".png");
						ImageIcon icon = new ImageIcon(getClass().getResource("assets//number" + boardGame[r][c] + ".png"));
						x.setIcon(icon);
					}
					x.setRolloverIcon(emptyHover);
					x.setToolTipText(r + " " + c);
					x.setBorder(BorderFactory.createLoweredBevelBorder());
					x.setVerticalAlignment(SwingConstants.CENTER);
					x.setHorizontalAlignment(SwingConstants.CENTER);
					x.setIconTextGap(0);
					x.addMouseListener(new MouseClickHandler(game, buttons, this,
							counterPanel));
					buttons[r][c] = x;
				}
			}
			
			for (JButton[] bR : buttons) {
				for (JButton bC : bR) {
					add(bC);
				}
			}
		}
	}

	/**
	 * Method to get the current JButton 2D array
	 * 
	 * @return 2D array of JButtons.
	 */
	public JButton[][] getJButtons() {
		return buttons;
	}

	/**
	 * Method to get a a specific JButton in the current game.
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public JButton getJButton(int row, int col) {
		return buttons[row][col];
	}
	
	/**
	 * 
	 * @return GameLogic the current game being played.
	 */
	public GameLogic getCurrentGame() {
		return gameLogic;
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

	}
}
