package de.tucottbus.kt.uasr_data_coins;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.*;
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
	public static File getLastFile(File dir){
		File[] files = dir.listFiles();
		Integer nbr = files.length-1;
		return (files[nbr]);
	}
	

	public static void main(String [ ] args) {
		final gui guiProg = new gui();	// create gui
		String dirIn = "C:/Users/wawra/workspace/uasr-data/coins/common/sig/test/test"; 				// read directory from input
		File dir = new File(dirIn);		// open dir
		File file = getLastFile(dir);   // opens last file in dir
		Path path = dir.toPath();
		
		WatchService newFileWatcher = null;
		try {
			newFileWatcher = FileSystems.getDefault().newWatchService();
			path.register(newFileWatcher, ENTRY_CREATE, ENTRY_MODIFY);
		} catch (IOException e) {
			System.err.println("WatchService failed");
		}

		while(true) {
			WatchKey key;
			try {
				key = newFileWatcher.take();
			} catch (InterruptedException e) {
				System.out.println("Interrupted. Program ending.");
				break;
			}
			
			
			
			
			System.out.println(file.toString());
			
	/*		String msg = "";				// analyze file
			decodeMessage.decode(msg);
	        guiProg.setRecognized(decodeMessage.getCoin(), decodeMessage.getSure());
	        guiProg.repaint();
	        // wait for one second / change in filesystem */
		}
	}
	
	
	
}
