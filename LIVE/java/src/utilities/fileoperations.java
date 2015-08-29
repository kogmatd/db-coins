package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class fileoperations {

	/*
	 * Liest alle Wav-Dateien eines Verzeichnisses aus,
	 * die das Schema Klassenname + "_" + restlicherDateiname + ".wav" besitzen.
	 * und in den Ordnern beginnend mit "C" (für Summenaufnahmen) oder "E" (für Einzelaufnahmen) enthalten sind.
	 * Die Ordner müssen direkte Unterverzeichnisse sein, oder in Verzeichnissen mit C/E gekennzeichnet enthalten sein.
	 * Die Ordner müssen zusätzlich im "sig" Oberverzeichnis angelegt sein (z.B: $UASR_HOME-data/coins/common/sig/C001).
	 * Geschrieben wird der relativ zum sig-Verzeichnisses Verzeichnisname + Dateiname (ohne .wav) + Leerzeichen + Klassenname
	 * 
	 * parameter:
	 * dir: Unterverzeichnis von sig, enthält Dateien
	 * mic: (-1) Summe wird verwendet
	 *      (0...3) Ausgewaehltes Mikrofon
	 */
	public static String allWavpath(File dir, Integer mic) {	
		String out = new String();
		String path = new String();
		String name = new String();
		out = ""; //Sammlung aller Zeichen die ausgegeben werden sollen.
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()){
					path = files[i].getAbsolutePath();

					if (path.endsWith(".wav")) { //WAV-Datei gefunden
						try {
							path = path.substring((path.indexOf("sig")+3), (path.length()-4));
							name = path.substring((path.lastIndexOf("\\")+1),(path.lastIndexOf("_")));				
						} catch (StringIndexOutOfBoundsException e) {	
							//Dateinamen mit falschem Schema werden ignoriert
						}
						
						if (mic != -1) { //Mikrofonauswahl, Ausgabe der Dateinamen + Klasse
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
				} else if (files[i].isDirectory()) { //Verzeichnis gefunden, rekursiver Aufruf
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
	

	/*
	 * userdialog lässt den Benutzer ein entsprechendes Verzeichnis oder eine Datei auswählen
	 * 
	 * Parameter:
	 * dir: Vorgegebenes Standard-Verzeichnis
	 * readonly: TRUE: Datei lesen (Muss existieren)
	 *           FALSE: Datei schreiben (Kann existieren)
	 * scport: Übergebener Scanner für Eingaben
	 */
	public static File userdialog(File dir, boolean readonly, Scanner scport) {
		                                                     
		boolean loop = true;	
		Scanner sc = scport;
		File file = null; //Ausgabeverzeichnis/Datei
		int breakcount = 0; //Zaehler fuer Schleife
		String eingabe;
		while (loop) {
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
			
			System.out.println("Standardpfad (" + dir +") verwenden? j/n");
			eingabe = sc.next();
			
			if (!eingabe.equals("j")) {
				if (eingabe.equals("n")) {
					System.out.println("Bitte kompletten Pfad von Daten angeben!");
					eingabe = sc.next();	
					dir = new File (eingabe);
				} else {
					System.out.println("Bitte antworten Sie mit \"j\" oder \"n\"!");
					continue; //Fängt Operation von vorne an, falls falsche Eingabe
				}
			}
			
			System.out.println("Wollen Sie noch einen Unterordner/Dateinamen angeben? j/n");
			eingabe = sc.next();
			
			if (!eingabe.equals("n")) {
				if (eingabe.equals("j")) {
					System.out.println("Bitte Dateinamen/Ordnernamen angeben:");
					System.out.print(dir + "\\");
					eingabe = sc.next();	
					file = new File (dir + "\\" + eingabe);
				} else {
					System.out.println("Bitte antworten Sie mit \"j\" oder \"n\"!");
					continue;
				}
			} else {
				file = dir;
			}
			
			if (file.exists()) { //Datei existiert bereits
				if (readonly) { //Datei wird gelesen
					if (file.isDirectory()) {
						file = new File (file.toString() + "\\");
					}
					System.out.println("Operation wird ausgefuehrt...");
					return file;
				} else { //Datei soll geschrieben werden
					System.err.println("Achtung, Datei existiert bereits. Ueberschreiben? j/n");
					eingabe = sc.next();
					if (!eingabe.equals("j")){
						continue;
					} else {
						return file;
					}
				}
			} else { //Datei existiert nicht
				if (readonly) { //Datei soll gelesen werden
					System.out.println("Pfad existiert nicht, bitte erneut angeben!");
					continue;
				} else {		
					try { //Datei soll neu geschrieben werden
		                System.out.println("Datei wird geschrieben...");
		                file.createNewFile();
		            	return file;
		            } catch (IOException e) {
		                System.err.println("Ungenuegende Schreibrechte.");
		                continue;
		            }
				}
			}
		}
		return file;
	}
	
	/*
	 * Abfrage zur Auswahle eines Mikrofones (0-3) oder der Summe (-1)
	 * 
	 * parameter: scport: Uebergebener Scanner
	 */
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
				if (mic >= 0 && mic < 4) { //4 = Anzahl der Mikrofone
					break;
				} else {
					System.out.println("Falsche Eingabe.");
				}
			} else if (eingabe.equals("n")) {
				mic = -1;
				break;
			}
		}
		return mic;
	}
	
	/*
	 * Schreibt String in Datei
	 * 
	 * Parameter:
	 * standard = vorgegebener Standardpfad
	 * input = zu schreibender String
	 * scin = Übergebener Scanner für Eingabe
	 */
	public static boolean filewritedialog(File standard, String input, Scanner scin){
		Scanner sc = scin;
		System.out.print("Speicher-");
		File directory = userdialog(standard, false, sc); //wählt Ordnerpfad aus zum schreiben
		if (directory == null) {
			System.out.println("Speichern abgebrochen.");
			return false;
		} else {
			try {
				FileWriter fw = new FileWriter(directory); 
				fw.write(input);
				fw.flush(); //schreibt Datei
				fw.close();
			} catch (IOException e) {
				System.out.println("IO-Fehler.");
				return false;
			}
			System.out.println("Gespeichert!");
			return true;
		}
	}
	
	/*
	 * Wählt eine Datei aus, erneute Abfrage bei Verzeichnissen
	 * 
	 * Parameter:
	 * dir: Vorgegebener Standardpfad
	 * sc: Übergebener Scanner fuer Eingabe
	 */
	public static File filechoose(File dir, Scanner sc) {
		File directory = userdialog(dir, true, sc);
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
		System.out.println("Datei " + directory.toString() + " ausgewaehlt.");
		return directory;
	}
	
	
	
	
	/*
	 * Hauptprogramm
	 */
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
					String uasr = System.getenv("UASR_HOME");
					File dir = new File (uasr + "-data\\coins\\common\\sig\\");
					File directory = userdialog(dir, true, sc);
					if (directory == null) {
						System.out.println("Filelisten erstellen abgebrochen.");
					} else {
						String wavfilepaths = allWavpath(directory, mic);
						System.out.println(wavfilepaths);
						dir = new File (uasr + "-data\\coins\\common\\flists\\tmp");
						boolean jn = false;
						while (!filewritedialog(dir, wavfilepaths, sc) && !jn) {
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
							jn = !filewritedialog(dir, out, sc);
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
