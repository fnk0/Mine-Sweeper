package main;
/**
 * This class will handle the menu clicks that can happen during the game.
 * @author Marcus Gabilheri
 * @version 1.0
 * @since Feb 15, 2014
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;

public class MenuListener implements ActionListener {
	
	private int actionType, gameLevel;
	private MineSweeperFrame frame;
	
	public MenuListener(int actionType, int gameLevel, MineSweeperFrame frame) {
		this.actionType = actionType;
		this.gameLevel = gameLevel;
		this.frame = frame;

	}
	
	public MenuListener(int actionType) {
		this.actionType = actionType;
	}
	
	public MenuListener(int actionType, MineSweeperFrame frame) {
		this.actionType = actionType;
		this.frame = frame;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		GameLogic game;
		if(actionType == GameLogic.NEW_GAME) {
			if(gameLevel == GameLogic.CUSTOM) {
				 int[] gameSettings = frame.setCustomGame();
				 game = new GameLogic(gameSettings[0], gameSettings[1], gameSettings[2], gameSettings[3]);
			} else {
				game = new GameLogic(gameLevel);
			}
						
			frame.newGame(game, false);
		} else if(actionType == GameLogic.QUIT){
			System.exit(0);
		} else if(actionType == GameLogic.LOAD) {
			getLoadFileChooser();
		} else if(actionType == GameLogic.SAVE) {
			getSaveFileChooser();
		}
	}
	
	public void getSaveFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("MineSweeper: Save Game");
		fileChooser.setApproveButtonText("Save Game");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);	
		
		int getOK = fileChooser.showSaveDialog(null);
		
		if(getOK == JFileChooser.APPROVE_OPTION) {
			frame.getCurrentGame().saveGame(frame.getCurrentGame(), fileChooser.getSelectedFile().getAbsolutePath());
		}
	}
	
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void getLoadFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		
		fileChooser.setApproveButtonText("Load Game");
		fileChooser.setDialogTitle("MineSweeper: Load Game");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		int getOK = fileChooser.showOpenDialog(null);
		
		if(getOK == JFileChooser.APPROVE_OPTION) {
			//System.out.println("File: " + fileChooser.getSelectedFile().getAbsolutePath());
			try {
				
				frame.getCurrentGame().loadGame(fileChooser.getSelectedFile().getAbsolutePath(), frame);
	
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
