package main;

/**
 * Frame Class for this Mine Sweep implementation.
 * @author Marcus Gabilheri
 * @version 1.0
 * @since February 15, 2014
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MineSweeperFrame extends JFrame {

	private static final long serialVersionUID = 1822516888544075185L;
	private static int XSIZE = 750;
	private static int YSIZE = 750;
	public Container contentPane;
	private JPanel gameContainer;
	private GamePanel gamePanel;
	private CounterPanel counterPanel;
	private static ImageIcon[] icons;
	private ImageIcon resizedGameLogo;
	private GameLogic game;

	/**
	 * Constructor for this class.
	 * 
	 * @param game
	 *            the game to be played
	 * @param newGame
	 *            check if is a newGame or a game that has been played already
	 */
	public MineSweeperFrame() {

		setSize(XSIZE, YSIZE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		Object[] selValues = { "Custom", "Hard", "Medium", "Easy" };
		
		ImageIcon gameLogo = new ImageIcon(getClass().getResource("assets//bomb.png"));
		resizedGameLogo = new ImageIcon(gameLogo.getImage().getScaledInstance(100, 75, java.awt.Image.SCALE_SMOOTH));
		
		int result = JOptionPane.showOptionDialog(null, "Welcome to Minesweeper!!",
				"MineSweeper", JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE, resizedGameLogo, selValues, selValues[3]);
		
		if (result == 3) {
			game = new GameLogic(GameLogic.EASY);
		} else if (result == 2) {
			game = new GameLogic(GameLogic.MEDIUM);
		} else if (result == 1) {
			game = new GameLogic(GameLogic.HARD);
		} else {
			int[] gameSettings = setCustomGame();
			game = new GameLogic(gameSettings[0], gameSettings[1],
					gameSettings[2], gameSettings[3]);
		}

		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		// MENU BAR
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(getFileMenu(this));
		menuBar.add(getNewGameMenu(this));
		
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		// GAME AREA
		gameContainer = new JPanel();
		gameContainer.setLayout(new BorderLayout());
		contentPane.add(gameContainer, BorderLayout.CENTER);

		counterPanel = new CounterPanel(game);
		gamePanel = new GamePanel(game, counterPanel, false);

		gameContainer.add(gamePanel, BorderLayout.CENTER);
		gameContainer.add(counterPanel, BorderLayout.NORTH);
		
		setVisible(true);
		icons = resizeImages(gamePanel.getJButton(0, 0));
		
	}
	
	public static ImageIcon getIcon(int position) {
		
		return icons[position];
	}
	
	public ImageIcon[] resizeImages(JButton button) {
		ImageIcon[] iconArray = new ImageIcon[14];
		for(int i = 0; i < 14; i++) {
			ImageIcon buttonIcon = new ImageIcon(getClass().getResource("assets//number" + i + ".png"));
			ImageIcon buttonIconResized = new ImageIcon(buttonIcon.getImage()
					.getScaledInstance((int) button.getBounds().getWidth(),
							(int) button.getBounds().getHeight(),
							java.awt.Image.SCALE_SMOOTH));
			
			iconArray[i] = buttonIconResized;
		}
		return iconArray;
	}

	/**
	 * Starts a new game
	 * 
	 * @param game
	 *            starts a new MineSweeper game
	 */
	public void newGame(GameLogic game, boolean loadGame) {

		getContentPane().remove(gameContainer);
		
		gameContainer = new JPanel();
		gameContainer.setLayout(new BorderLayout());
		CounterPanel newCounter = new CounterPanel(game);
		GamePanel newGame = new GamePanel(game, newCounter, loadGame);

		gameContainer.add(newCounter, BorderLayout.NORTH);
		gameContainer.add(newGame, BorderLayout.CENTER);

		getContentPane().add(gameContainer);
		getContentPane().invalidate();
		getContentPane().validate();
		repaint();
		icons = resizeImages(newGame.getJButton(0, 0));
		
		if(loadGame) {
			/*
			JButton[][] buttons = gamePanel.getJButtons();
			System.out.println("Buttons Rows: " + buttons.length);
			System.out.println("Buttons Columns: " + buttons[0].length);
			System.out.println("Bord row size: " + game.getBoardRowSize());
			System.out.println("Bord col size: " + game.getBoardColumnSize());
			*/
			for(int r = 0; r < game.getBoardRowSize(); r++) {
				for(int c = 0; c < game.getBoardColumnSize(); c++) {
					int position = game.getBoard()[r][c];
					if(position != GameLogic.BOMB && position != GameLogic.EMPTY && position != GameLogic.FLAG) {
						//System.out.println("Position: " + position);
						newGame.getJButton(r, c).setIcon(icons[position]);
					} else if(game.getPlayedBoard()[r][c] == GameLogic.FLAG) {
						newGame.getJButton(r, c).setIcon(icons[MouseClickHandler.FLAG_IMAGE]);
						newGame.getJButton(r, c).setRolloverIcon(null);
					}
				}
			}
		}
		
		this.game = game;
		
	}

	/**
	 * Gets the parameters necessary to generate a custom game settings[0] =
	 * Game Level settings[1] = Row settings[2] = Column settings[3] = Bombs
	 * 
	 * @return int[] with the necessary parameters where:
	 * 
	 */
	public int[] setCustomGame() {
		int[] gameSettings = {GameLogic.CUSTOM, 10, 10, 15};
		JTextField rowField = new JTextField(5);
		JTextField columnField = new JTextField(5);
		JTextField bombField = new JTextField(5);
		JPanel customGamePanel = new JPanel();
		customGamePanel.setLayout(new GridLayout(5, 2));
		customGamePanel.add(new JLabel("rows: "));
		customGamePanel.add(rowField);
		customGamePanel.add(new JLabel("columns: "));
		customGamePanel.add(columnField);
		customGamePanel.add(new JLabel("bombs"));
		customGamePanel.add(bombField);

		JOptionPane.showConfirmDialog(null, customGamePanel, "Game Selection",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, resizedGameLogo);
		
		
		try {
			gameSettings[0] = GameLogic.CUSTOM;
			gameSettings[1] = Integer.parseInt(rowField.getText());
			gameSettings[2] = Integer.parseInt(columnField.getText());
			gameSettings[3] = Integer.parseInt(bombField.getText());
			
		} catch(Exception e) {
			
		}

		return gameSettings;
	}

	/**
	 * The menu bar for this game.
	 * 
	 * @return JMenubar the menu bar...
	 */
	public JMenu getNewGameMenu(MineSweeperFrame frame) {

		// The JMenuBar that will Control the Elements

		// The NewGame Menu elements
		JMenu newGame = new JMenu("New Game");
		JMenuItem easy = new JMenuItem("Easy");
		JMenuItem medium = new JMenuItem("Medium");
		JMenuItem hard = new JMenuItem("Hard");
		JMenuItem custom = new JMenuItem("Custom");
		
		// Adding Action Listener to the elements
		easy.addActionListener(new MenuListener(GameLogic.NEW_GAME,
				GameLogic.EASY, frame));
		medium.addActionListener(new MenuListener(GameLogic.NEW_GAME,
				GameLogic.MEDIUM, frame));
		hard.addActionListener(new MenuListener(GameLogic.NEW_GAME,
				GameLogic.HARD, frame));
		custom.addActionListener(new MenuListener(GameLogic.NEW_GAME,
				GameLogic.CUSTOM, frame));
		
		// Adding them to the newGame Menu
		newGame.add(easy);
		newGame.add(medium);
		newGame.add(hard);
		newGame.add(custom);
		return newGame;
	}
	
	public JMenu getFileMenu(MineSweeperFrame frame) {
				
		JMenu file = new JMenu("File");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem load = new JMenuItem("Load");
		JMenuItem quit = new JMenuItem("Quit");
		
		load.addActionListener(new MenuListener(GameLogic.LOAD, frame));
		save.addActionListener(new MenuListener(GameLogic.SAVE, frame));
		quit.addActionListener(new MenuListener(GameLogic.QUIT));
		
		file.add(load);
		file.add(save);
		file.add(quit);
		return file;
	}
	
	public GameLogic getCurrentGame() {
		return game;
	}

}
