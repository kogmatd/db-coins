package gui;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;

public class gui extends Frame {

	private int recognizedCoin = 4;
	private boolean sure = false;
	private int width;
	private int height;
	private String abs;
	private String rel = "";
	private String start = "Erkannte Münze:";
	private String notSure = "?";	
	private String yesSure = "!";
	private Image img;
	private boolean exit = false;
	
	public gui(int w, int h){
		super("Münzerkennung");
		addWindowListener(new WindowClosingAdapter(true, this));
		setBackground(Color.black);

		setSize(w,h);
		setVisible(true);
		
		img = null;
		abs = "\\LIVE\\java\\src\\img";
		try {
			rel = new File(".").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("HOME Verzeichnis nicht gefunden.");
		}
	}
	
	public gui(){
		this(640,480);
	}
	
	public void setRecognizedCoin(int coin){
		recognizedCoin = coin;
	}
	
	public void setSure (boolean s){
		sure = s;
	}
	
	public void close() {
		exit = true;
	}
	
	public boolean closed(){
		return exit;
	}
	
	public void paint(Graphics g){
		width = getSize().width;
		height = getSize().height;

		Font topFont;
		Font SureFont;
		topFont = new Font("SansSerif",Font.PLAIN,36);
		SureFont = new Font("Serif",Font.PLAIN,72);
		g.setFont(topFont);
		FontMetrics fm = g.getFontMetrics();
		int startLength = fm.stringWidth(start);
		g.setColor(Color.green);
		g.drawString(start, (width-startLength)/2, 150);
		
		switch(recognizedCoin){
		case   1: img = getToolkit().getImage(rel + abs + "\\C001.jpg"); break;
		case   2: img = getToolkit().getImage(rel + abs + "\\C002.jpg"); break;
		case   5: img = getToolkit().getImage(rel + abs + "\\C005.jpg"); break;
		case  10: img = getToolkit().getImage(rel + abs + "\\C010.jpg"); break;
		case  20: img = getToolkit().getImage(rel + abs + "\\C020.jpg"); break;
		case  50: img = getToolkit().getImage(rel + abs + "\\C050.jpg"); break;
		case 100: img = getToolkit().getImage(rel + abs + "\\C100.jpg"); break;
		case 200: img = getToolkit().getImage(rel + abs + "\\C200.jpg"); break;
		default:  img = getToolkit().getImage(rel + abs + "\\C000.png"); break;
		}
		g.drawImage(img, (width-100)/2, 200, this);
		
		g.setFont(SureFont);
		if (sure){
			g.drawString(yesSure,(width+150)/2, 275);
		} else {
			g.drawString(notSure,(width+150)/2, 275);
		}
	}
	public static void main(String[] args){
		Frame window = new gui();
	}
}
