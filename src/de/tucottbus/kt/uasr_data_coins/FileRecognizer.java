package de.tucottbus.kt.uasr_data_coins;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.Scanner;

import static java.nio.file.StandardWatchEventKinds.*;
import gui.decodeMessage;
import gui.gui;

/*
 * author: Johannes Wawra
 * WhatchKey-Usage from 
 * https://docs.oracle.com/javase/tutorial/essential/io/notification.html
 * 2015-11-08
 * 
 * ProcessBuilder-Usage from
 * http://openbook.rheinwerk-verlag.de/javainsel9/javainsel_11_005.htm
 */

public class FileRecognizer extends Thread{
	
	
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
		String dirIn = "C:\\Users\\wawra\\workspace\\uasr-data\\coins\\common\\sig\\test\\test"; 				// read directory from input
		File dir = new File(dirIn);		// open dir
		File file = getLastFile(dir);   // opens last file in dir
		Path path = dir.toPath();
		
		WatchService newFileWatcher = null;
		try {
			newFileWatcher = FileSystems.getDefault().newWatchService();
			path.register(newFileWatcher, ENTRY_MODIFY);
		} catch (IOException e) {
			System.err.println("WatchService failed");
		}
		
		Boolean go = false;
	 	final gui guiPrg = new gui();
		
		while(true) {
			WatchKey key;
			try {
				key = newFileWatcher.take();
			} catch (InterruptedException e) {
				System.out.println("Interrupted. Programm ending.");
				break;
			}
			
			for(WatchEvent<?> event: key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();
				
				//Overflow, can occur always
				if (kind == OVERFLOW) {
					continue;
				}
				
				if (kind == ENTRY_MODIFY) {
					WatchEvent<Path> CreateEvent = (WatchEvent<Path>)event;
					Path filename = CreateEvent.context(); //Path of created file in context
				
					//try {
						Path child = path.resolve(filename);
						/*//checks if a WAV-File was changed, ignore if it was not
						if (!Files.probeContentType(child).equals("audio/x-wav")) {
							System.err.format("Changed File "
									+ "'%s' is not a waveform audio file", filename);
							continue;
						} 
					} catch (IOException x) {
						System.err.println("IO-Fehler" + x);
						continue;
						// does not work yet */
					//}
					go = true;
					String temp = startRecognizer(filename, dirIn);
					decodeMessage.decode(temp);
					guiPrg.setRecognized(decodeMessage.getCoin(), decodeMessage.getSure());
			        guiPrg.repaint();
				}
				
			}
			
			
			
			
			
			System.out.println(file.toString());
			
	/*		String msg = "";				// analyze file
			decodeMessage.decode(msg);
	        guiProg.setRecognized(decodeMessage.getCoin(), decodeMessage.getSure());
	        guiProg.repaint();
	        // wait for one second / change in filesystem */
		}
	}

	/*
	 * invokes Recognizer with given path
	 * Needs installed dLabPro in same directory like $UASR_HOME
	 */
	private static String startRecognizer(Path filename, String dirIn) {
		String uasr = System.getenv("UASR_HOME");
		String dlabDir = uasr.substring(0,uasr.length()-4) + "dLabPro"; //DLabPro instead of uasr
		String cmd = dlabDir + "\\bin.release\\recognizer -cfg " + uasr + "-data\\coins\\Versuch1" + "\\info\\recognizer.cfg "
						+ dirIn + "/" + filename.toString();
		ProcessBuilder recognizerProcess = new ProcessBuilder("cmd","/c","dir",cmd);
		System.out.println(cmd);
		
		Process p = null;
		try {
			p = recognizerProcess.start();
		} catch (IOException e) {
			System.err.println("IO-Fehler " + e);
		}
		Scanner sc = new Scanner(p.getErrorStream()).useDelimiter("\\Z");
		
		Boolean doRead = false;
		String temp = "Nothing yet written...";
		while (!doRead){
			temp = sc.nextLine();
			System.out.println("_"+temp);
			doRead = (temp.contains("res: "));
		}
		sc.close();
		return temp;
	}
	
	
	
}
