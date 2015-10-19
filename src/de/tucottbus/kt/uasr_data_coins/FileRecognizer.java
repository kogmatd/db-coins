package de.tucottbus.kt.uasr_data_coins;
import java.io.*;

import gui.decodeMessage;
import gui.gui;

public class FileRecognizer {
	
	
	/*
	 * returns last closed file in dir
	 */
	public static File getNextFile(File dir, Integer Nbr){
		File file = dir; //TODO
		
		return file;
	}
	
	/*
	 * returns number of files in dir
	 */
	public static Integer getFiles(File dir){
		File[] files = dir.listFiles();
		return (files.length);
	}
	
	

	public static void main(String [ ] args) {
		final gui guiProg = new gui();	// create gui

		String dirIn = ""; 				// read directory from input
		File dir = new File(dirIn);		// open dir
		Integer nbr = getFiles(dir);
		File file = getNextFile(dir, nbr);	// open last file in dir  

		while (file != null) {
			String msg = "";				// analyze file
			decodeMessage.decode(msg);
	        guiProg.setRecognized(decodeMessage.getCoin(), decodeMessage.getSure());
	        guiProg.repaint();
	        nbr += 1;
	        file = getNextFile(dir, nbr);	// wait for next file			
		}
	}
	
	
	
}
