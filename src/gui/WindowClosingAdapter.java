package gui;

import java.awt.event.*;

public class WindowClosingAdapter extends WindowAdapter{
	private static boolean exit;
	gui frame;
	
	public WindowClosingAdapter(boolean exitKnob, gui frame){
		exit = exitKnob;
		this.frame = frame;
	}
	
	public void windowClosing(WindowEvent event){
		event.getWindow().setVisible(false);
		event.getWindow().dispose();

		
		if (exit) {
	//		System.exit(0);
			frame.setVisible(false);
		}
	}
}
