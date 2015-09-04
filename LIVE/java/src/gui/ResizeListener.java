package gui;

import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ResizeListener implements ComponentListener {

	Frame window;
	
	ResizeListener(Frame window) {
		this.window = window;
	}
	
	@Override
	public void componentHidden(ComponentEvent arg0) {
		window.repaint();
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		window.repaint();
	}
	
	@Override
	public void componentResized(ComponentEvent arg0) {
		window.repaint();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		window.repaint();
	}

}
