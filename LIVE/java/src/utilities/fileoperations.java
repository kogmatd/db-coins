package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class fileoperations {

	public static String allWavpath(File dir, Integer mic) {	
		String out = new String();
		String path = new String();
		String name = new String();
		out = "";
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()){
					path = files[i].getAbsolutePath();
					if (path.endsWith(".wav")) {
						path = path.substring((path.indexOf("sig")+3), (path.length()-4));
						name = path.substring((path.lastIndexOf("\\")+1),(path.lastIndexOf("_")));
						if (mic != -1) {
							switch (mic) {
							case 0: if(path.endsWith("ai0")) {
									out += path + " " + name + "\n";
								}
								break;
							case 1: if(path.endsWith("ai1")) {
									out += path + " " + name + "\n";
								}
								break;
							case 2: if(path.endsWith("ai2")) {
									out += path + " " + name + "\n";
								}
								break;
							case 3: if(path.endsWith("ai3")) {
									out += path + " " + name + "\n";
								}
								break;
							}
						} else {
							out += path + " " + name + "\n";						
						}
					}
				} else if (files[i].isDirectory()) {
					if (mic == -1) {
						if (files[i].getName().startsWith("C")) {
							out += allWavpath(files[i], mic);
						}
					} else {
						if (files[i].getName().startsWith("E")) {
							out += allWavpath(files[i], mic);
						}
					}
				}
			}
		}
		return out;
	}
	

	public static File userdialog(File dir, Scanner scport) {
		                                                     
		boolean jn = false;	
		Scanner sc = scport;
		while (!jn) {
			int breakcount = 0;
			System.out.println("Standardpfad verwenden? j/n");
			String eingabe = sc.next();
			
			if (!eingabe.equals("j")) {
				if (eingabe.equals("n")) {
					System.out.println("Bitte kompletten Pfad von Daten angeben!");
					eingabe = sc.next();	
					dir = new File (eingabe);
				} else {
					continue;
				}
			}
			
			if (dir.exists()) {
				System.out.println("Operation wird ausgef�hrt...");
				jn = true;
			} else {
				System.out.println("Pfad existiert nicht, bitte erneut angeben!");
				jn = false;
				breakcount++;
				if (breakcount > 5) {
					System.out.println("Operation abbrechen? j/n");
					eingabe = sc.next();
					if (eingabe.equals("j")) {
						return null;
					} else if (eingabe.equals("n")) {
						breakcount = 0;
					}
				}
			}	
		}
		return dir;
	}
	
	public static Integer micselect(Scanner scport) {
		boolean jn = false;	
		Scanner sc = scport;
		String eingabe = new String();
		Integer mic = new Integer(5);
		while (!jn) {
			System.out.println("Einzelaufnahmen verwenden? j/n");
			eingabe = sc.next();
			if (eingabe.equals("j")) {
				System.out.println("Welches Mikrofon? 0-3");
				eingabe = sc.next();
				mic = Integer.valueOf(eingabe);
				if (mic >= 0 && mic < 4) {
					jn = true;
				} else {
					System.out.println("Falsche Eingabe.");
				}
			} else if (eingabe.equals("n")) {
				mic = -1;
				jn = true;
			}
		}
		return mic;
	}
	
	public static boolean filewrite(File f, String out){
		try {
			FileWriter fw = new FileWriter(f);
			fw.write(out);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			System.out.println("IO-Fehler.");
		}	
			
		return true;
	}

	public static boolean filewritedialog(File standard, String filename, String input, Scanner scin){
		Scanner sc = scin;
		System.out.print("Speicher-");
		File directory = userdialog(standard, sc);
		if (directory == null) {
			System.out.println("Speichern abgebrochen.");
			return false;
		} else {
			String filelistpath = directory.toString();
			File filelistfile = new File (filelistpath + "\\" +filename);
			System.out.println("Speichern unter: \n"
					+ filelistfile.toString());

			if (filelistfile.exists()) {
				System.err.println("Achtung, Datei existiert bereits. �berschreiben? j/n");
				String eingabe = sc.next();
				while (!eingabe.equals("j")){
					if (eingabe.equals("n")) {
						System.out.println("Operation abgebrochen.");
						return false;
					}	
					System.out.println("Bitte geben Sie \"j\" f�r �berschreiben ein"
							+ "oder \"n\" f�r Abbruch!");
				}
				
			}
				
		    if (filelistfile != null) {
	            try {
	                filelistfile.createNewFile();
	            } catch (IOException e) {
	                System.err.println("Ungen�gende Schreibrechte.");
	                return false;
	            }
		    }
			filewrite(filelistfile, input);
			System.out.println("Gespeichert!");
			return true;
		}
	}
	
	
	public static File filechoose(File dir, Scanner sc) {
		File directory = userdialog(dir, sc);
		while (!directory.isFile()){
			System.out.print("Bitte noch den Dateinamen angeben (Standard: log.txt): ");
			String Dateiname = sc.next();
			File tempdir = directory;
			directory = new File (directory.toString() + "\\" + Dateiname);
			if (!directory.exists()) {
				System.err.println("Datei " + directory.toString() + " existiert nicht.");
				directory = tempdir;
			}
		}
		System.out.println("Datei " + directory.toString() + " ausgew�hlt.");
		return directory;
	}
	
	
	
	
	
	public static void main(String [ ] args) {
		boolean abbruch = false;
		Scanner sc = new Scanner(System.in);
		while (!abbruch) {			
			System.out.println("Wollen Sie \n"
					+ "(1) Filelisten generieren, \n"
					+ "(2) Ergebnisse aus der Logdatei extrahieren oder \n"
					+ "(e) das Programm beenden?");
			String eingabe = new String();
			eingabe = sc.next();
			switch (eingabe) {
			
			case "1": 
				{ //FILELIST GENERIEREN
					Integer mic = micselect(sc);
					File dir = new File ("C:\\Users\\wawra\\workspace\\uasr-data\\coins\\common\\sig\\Vorversuch6Glas300\\");
					File directory = userdialog(dir, sc);
					if (directory == null) {
						System.out.println("Filelisten erstellen abgebrochen.");
					} else {
						String wavfilepaths = allWavpath(directory, mic);
						System.out.println(wavfilepaths);
						dir = new File ("C:\\Users\\wawra\\workspace\\uasr-data\\coins\\common\\flists\\tmp");
						boolean jn = false;
						while (!filewritedialog(dir,  "\\all.flst", wavfilepaths, sc) && !jn) {
							System.out.println("Erneut probieren? j/n");
							eingabe = sc.next();
							if (eingabe.equals("n")) {
								jn = true;
							}
						};
					}
				}
				break;
			
			case "2":
				{ // AUSWERTUNG LOGDATEI
					File dir = new File ("C:\\Users\\wawra\\workspace\\uasr-data\\coins\\common\\log\\");
					System.out.print("Logdatei-");
					File logfile = filechoose(dir, sc);
					FileReader freader = null;
					try {
						freader = new FileReader(logfile);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    BufferedReader reader = new BufferedReader(freader);
				    char[] output = new char[10000];
				    int endstring = 0;
				    String out = "Extrahierung Fehlgeschlagen.";
				    String n = "\n";
				    String t = "\t";
				    int a;
				    int b;
				    while(true) {
				    	String line = "test";
				        while(line != null) {
				        	if(line.indexOf("Correctness") > -1) {
				        		line.getChars( a=(line.indexOf("HMM") + 4), b=(line.indexOf(")")-1), output, endstring);
				        		endstring += b-a;
				        		t.getChars(0, 1, output, endstring);
				        		endstring += 1;
				        		line.getChars( a=(line.indexOf(":") + 2), b=(line.indexOf("%")-1), output, endstring);
				        		endstring += b-a;
				        		t.getChars(0, 1, output, endstring);
				        		endstring += 1;
				        		line.getChars( a=(line.indexOf("+") + 1), b=(line.indexOf("-", 5)-1), output, endstring);
				        		endstring += b-a;
				        		t.getChars(0, 1, output, endstring);
				        		endstring += 1;
				        		line.getChars( a=(line.indexOf("-", 5) + 1), b=(line.indexOf("(") -1), output, endstring);
				        		endstring += b-a;
				        		n.getChars(0, 1, output, endstring);
				        		endstring += 1;
				        		System.out.print(".");
				        	}
				        	if(line.indexOf("Vektor")> -1){  
				        		n.getChars(0, 1, output, endstring);
				        		endstring += 1;    		
				        		line.getChars( a=(line.indexOf("Vektor") - 2), b=(line.indexOf("}")), output, endstring);
				        		endstring += b-a;	
				        		n.getChars(0, 1, output, endstring);
				        		endstring += 1;
				        	}
				        	try {
								line = reader.readLine();
							} catch (IOException e) {
								System.err.println("IO FEHLER");
							}
					    }
				        
				        System.out.println("\nFertig.");
				        //out = new String(output);
				        
		
				        out = String.valueOf(output);
				       
						dir = new File ("C:\\Users\\wawra\\workspace\\uasr-data\\coins\\common\\log\\");
						boolean jn = true;
						while (jn) {
							System.out.print("Bitte zu speichernden Dateinamen angeben: ");
							eingabe = sc.next();
							jn = !filewritedialog(dir, eingabe, out, sc);
							if (jn) {
								System.out.println("Erneut probieren? j/n");
								eingabe = sc.next();
								if (eingabe.equals("j")) {
									continue;
								}
							}
							break;
						};
						break;
				    }
				    
				    
				}
				break;
			
			case "e":
				abbruch = true;	
				break;
			
			default: 
			{
				System.out.println("Falsche Eingabe. Bitte nur eine Ziffer angeben. \n"
						+ "Wenn Sie das Programm beenden wollen geben Sie bitte \"e\" ein. ");		
			}
			
			
			} 
		}
	}
}
