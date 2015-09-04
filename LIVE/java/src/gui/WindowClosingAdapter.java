package gui;

import java.awt.event.*;

public class WindowClosingAdapter extends WindowAdapter{
	private boolean exit;
	
	public WindowClosingAdapter(boolean exit){
		this.exit = exit;
	}
	
	public WindowClosingAdapter() {
		this(false);
	}
	
	public void windowClosing(WindowEvent event){
		event.getWindow().setVisible(false);
		event.getWindow().dispose();
		if (exit) {
			System.exit(0);
		}
	}
}
