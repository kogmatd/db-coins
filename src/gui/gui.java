package gui;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;

public class gui extends Frame {

	private int recognizedCoin = 0;
	private boolean sure = false;             //Bestätigte Erkennung der Münze
	private int width;                        //Breite des Fensters
	private String abs;                       //Pfad relativ zu uasr-data-coins
	private String rel = "";                  //Pfad von uasr-data-coins
	private String start = "Erkannte Münze:"; //Oberer String
	private String notSure = "?";			  //Zeichen neben der Münze für Rückweisung
	private String yesSure = "!";             //Zeichen neben der Münze für Akzeptanz
	private String history;  //Strings für die Ausgabe der bisherigen Ergebnisse
	private String c001;
	private String c002;
	private String c005;
	private String c010;
	private String c020;
	private String c050;
	private String c100;
	private String c200;
	private String notr;
	private Image img;      //Darstellung der Münze
	private boolean exit = false;              //Angabe ob Programm beendet werden soll
	private int[][] coinHistory = {
			{0,0}, //[0][0] 1cent, [0][1] sure1cent
			{0,0}, //[1][0] 2cent, [1][1] sure...
			{0,0}, //[2] 5cent
			{0,0}, //[3] 10cent
			{0,0}, //[4] 20cent
			{0,0}, //[5] 50cent
			{0,0}, //[6] 1euro
			{0,0}, //[7] 2euro
			{0,0}  //[8] no recognition
	};
	private int allCoins;   //Zähler für alle Ergebnisse
	
	
	public gui(int w, int h){
		super("Münzerkennung");
		addWindowListener(new WindowClosingAdapter(true, this));
		this.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent e) {
				repaint();
			}
		});
		setBackground(Color.black);

		setSize(w,h);
		setVisible(true);
		
		img = null;
		abs = "\\src\\resources";
		try {
			rel = new File(".").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("HOME Verzeichnis nicht gefunden.");
		}
	}
	
	public gui(){
		this(400,600);
	}

	
	public void resetAllCoins(){
		allCoins = 0;
	}
	
	public void setRecognized(int coin, boolean s){
		recognizedCoin = coin;
		sure = s;
		allCoins += 1;
		switch (coin) {
		case   1: {coinHistory[0][0] += 1; break;}
		case   2: {coinHistory[1][0] += 1; break;}
		case   5: {coinHistory[2][0] += 1; break;}
		case  10: {coinHistory[3][0] += 1; break;}
		case  20: {coinHistory[4][0] += 1; break;}
		case  50: {coinHistory[5][0] += 1; break;}
		case 100: {coinHistory[6][0] += 1; break;}
		case 200: {coinHistory[7][0] += 1; break;}
		default : {coinHistory[8][0] += 1; break;}
		}
		if (sure)
		{
			switch (coin) {
			case   1: {coinHistory[0][1] += 1; break;}
			case   2: {coinHistory[1][1] += 1; break;}
			case   5: {coinHistory[2][1] += 1; break;}
			case  10: {coinHistory[3][1] += 1; break;}
			case  20: {coinHistory[4][1] += 1; break;}
			case  50: {coinHistory[5][1] += 1; break;}
			case 100: {coinHistory[6][1] += 1; break;}
			case 200: {coinHistory[7][1] += 1; break;}
			default : {                                   //this occurs only if the message wasn't a coin recognition event
				coinHistory[8][1] += 1;   
				allCoins -= 1; 
				coinHistory[8][0] -= 1;
				break;} 
			}
		}	
	}
	
	public void close() {
		exit = true;
	}
	
	public boolean closed(){
		return exit;
	}
	
	
	public void paint(Graphics g){
		width = getSize().width;
	
		Font topFont;
		Font SureFont;
		Font bottomFont;
		topFont = new Font("SansSerif",Font.PLAIN,36);
		SureFont = new Font("Serif",Font.PLAIN,72);
		bottomFont = new Font("SansSerif",Font.PLAIN,16);
		g.setFont(topFont);
		FontMetrics fm = g.getFontMetrics();
		int startLength = fm.stringWidth(start);
		g.setColor(Color.green);
		g.drawString(start, (width-startLength)/2, 150);
		
		switch(recognizedCoin){
		case   1: img = getToolkit().getImage(rel + abs + "\\C001.png"); break;
		case   2: img = getToolkit().getImage(rel + abs + "\\C002.png"); break;
		case   5: img = getToolkit().getImage(rel + abs + "\\C005.png"); break;
		case  10: img = getToolkit().getImage(rel + abs + "\\C010.png"); break;
		case  20: img = getToolkit().getImage(rel + abs + "\\C020.png"); break;
		case  50: img = getToolkit().getImage(rel + abs + "\\C050.png"); break;
		case 100: img = getToolkit().getImage(rel + abs + "\\C100.png"); break;
		case 200: img = getToolkit().getImage(rel + abs + "\\C200.png"); break;
		default:  img = getToolkit().getImage(rel + abs + "\\C000.png"); break;
		}
		g.drawImage(img, (width-100)/2, 200, 110, 110, this);
		
		g.setFont(SureFont);
		if (sure){
			g.drawString(yesSure,(width+150)/2, 275);
		} else {
			g.drawString(notSure,(width+150)/2, 275);
		}
		
		g.setFont(bottomFont);
		history = (allCoins) + " Wuerfe davon: \n";
		c001 = "  1 Cent: " + coinHistory[0][0] + "; davon sicher: " + coinHistory[0][1];
		c002 = "  2 Cent: " + coinHistory[1][0] + "; davon sicher: " + coinHistory[1][1];
		c005 = "  5 Cent: " + coinHistory[2][0] + "; davon sicher: " + coinHistory[2][1];
		c010 = "10 Cent: " + coinHistory[3][0] + "; davon sicher: " + coinHistory[3][1];
		c020 = "20 Cent: " + coinHistory[4][0] + "; davon sicher: " + coinHistory[4][1];
		c050 = "50 Cent: " + coinHistory[5][0] + "; davon sicher: " + coinHistory[5][1];
		c100 = "  1 Euro: " + coinHistory[6][0] + "; davon sicher: " + coinHistory[6][1];
		c200 = "  2 Euro: " + coinHistory[7][0] + "; davon sicher: " + coinHistory[7][1];
		notr = "nicht erkannt: " + coinHistory[8][0];
		fm = g.getFontMetrics();
		int historyLength = fm.stringWidth(c001);
		g.drawString(history, (width-historyLength)/2, 400);
		g.drawString(c001, (width-historyLength)/2, 420);
		g.drawString(c002, (width-historyLength)/2, 440);
		g.drawString(c005, (width-historyLength)/2, 460);
		g.drawString(c010, (width-historyLength)/2, 480);
		g.drawString(c020, (width-historyLength)/2, 500);
		g.drawString(c050, (width-historyLength)/2, 520);
		g.drawString(c100, (width-historyLength)/2, 540);
		g.drawString(c200, (width-historyLength)/2, 560);
		g.drawString(notr, (width-historyLength)/2, 580);
	}
	public static void main(String[] args){
		Frame window = new gui();
	}
}
