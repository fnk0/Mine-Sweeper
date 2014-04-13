package main;
/**
 * CounterPanel class for the bomb counter, game stat logo and timer.
 * @author Marcus Gabilheri
 * @version 1.0
 * @since Feb 15, 2014
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CounterPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private int bombs, currentFace;
	private long time;
	private JLabel bombCounter, timeLabel, faceLabel;
	private Timer timer;
	
	// Face image constants
	public static final int DEFAULT_FACE = 1;
	public static final int WIN_FACE = 2;
	public static final int LOOSE_FACE = 3;
	public static final int CHEAT_FACE = 4;
	
	/**
	 * Constructor for the CounterPanel class
	 * @param game the current game being played
	 */
	public CounterPanel(GameLogic game) {
		this.bombs = game.getBombCount();
		setSize(getWidth(), 150);
		setBackground(Color.BLACK);
		
		// timer stuff
		time = 0L;
		int delay = 1000; 
		
		  ActionListener timeCounter = new ActionListener() {
		      public void actionPerformed(ActionEvent evt) {
		          time++;
		      }
		  };
		  
		timer = new Timer(delay, timeCounter);
		timer.start();

		setLayout(new GridLayout(0, 3));
		
		bombCounter = new JLabel();
		timeLabel = new JLabel();
		faceLabel = new JLabel();
		
		// Alignment and other style stuff for the labels
		bombCounter.setForeground(Color.WHITE);
		timeLabel.setForeground(Color.WHITE);
		currentFace = DEFAULT_FACE;
		Font font = new Font("Serif", 40, 30);
		bombCounter.setFont(font);
		timeLabel.setFont(font);
		timeLabel.setHorizontalAlignment((int) JLabel.LEFT_ALIGNMENT);
		faceLabel.setHorizontalAlignment((int) JLabel.CENTER_ALIGNMENT);

		// setting the values of the Labels
		bombCounter.setText("Bombs: " + bombs);
		faceLabel.setIcon(getImageResource("assets//face" + currentFace + ".png"));
		faceLabel.addMouseListener(new SmileFaceListener(this));
		timeLabel.setText((time / 1000) + "s");
		
		// Adding the labels
		add(bombCounter);
		add(faceLabel);
		add(timeLabel);

		repaint();
	}
	
	/**
	 * Sets the bomb counter of the current game
	 * @param add integer to increment or decrement the bombs.
	 */
	public void setBombCounter(boolean add) {
		if (add) {
			bombs++;
		} else {
			bombs--;
		}
	}
	
	/**
	 * Stops the current game timer
	 */
	public void stopTimer() {
		timer.stop();
	}
	
	/**
	 * Sets the face to be displayed on the middle of the panel
	 * @param faceNum int the number of the face
	 */
	public void setFaceName(int faceNum) {
		currentFace = faceNum;
	}
	
	/**
	 * Gets the current face being displayed
	 * @return int the number related to that image
	 */
	public int getFaceNum() {
		return currentFace;
	}
	
	/**
	 * gets the face icon for the top panel
	 * @param imageResource String with the path to the desired image
	 * @return ImageIcon the icon resized to 50x50 pixels
	 */
	public  ImageIcon getImageResource(String imageResource) {
		
		ImageIcon imageIcon = new ImageIcon(getClass().getResource(imageResource));
		ImageIcon imageIconResized = new ImageIcon(imageIcon.getImage()
				.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
		
		return imageIconResized;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		bombCounter.setText("Bombs: " + bombs);
		faceLabel.setIcon(getImageResource("assets//face" + currentFace + ".png"));
		timeLabel.setText(time + " s");
		
		invalidate();
		validate();
		repaint();
	}
}
