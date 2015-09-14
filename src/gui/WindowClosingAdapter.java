package gui;

import java.awt.Frame;
import java.awt.event.*;

import de.tucottbus.kt.uasr_data_coins.CoinsRecognizer;

public class WindowClosingAdapter extends WindowAdapter{
	private static boolean exit;
	gui frame;
	
	public WindowClosingAdapter(boolean exit, gui frame){
		this.exit = exit;
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
