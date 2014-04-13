package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class SmileFaceListener extends MouseAdapter {
	
	CounterPanel panel;
	
	public SmileFaceListener(CounterPanel panel) {
		this.panel = panel;
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		panel.setFaceName(CounterPanel.WIN_FACE);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		panel.setFaceName(CounterPanel.DEFAULT_FACE);
	}
}
