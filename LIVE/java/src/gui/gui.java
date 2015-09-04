package gui;

import java.awt.*;
import java.awt.event.ComponentEvent;

public class gui extends Frame {

	int recognizedCoin = 001;
	int width;
	int height;
	
	public gui(int width, int height){
		super("Münzerkennung");
		addWindowListener(new WindowClosingAdapter(true));
		setBackground(Color.black);

		setSize(width,height);
		setVisible(true);		
	}
	
	public gui(){
		super("Münzerkennung");
		addWindowListener(new WindowClosingAdapter(true));
		addComponentListener(new ResizeListener(this));
		setBackground(Color.black);
		
		setSize(600,480);
		setVisible(true);
	}
	
	public void paint(Graphics g){
		width = getSize().width;
		height = getSize().height;
		
		String start = "Erkannte Münze:";
		Font topFont;
		topFont = new Font("SansSerif",Font.PLAIN,36);
		g.setFont(topFont);
		FontMetrics fm = g.getFontMetrics();
		int startLength = fm.stringWidth(start);
		g.setColor(Color.green);
		g.drawString(start, (width-startLength)/2, 150);
		
		Image img = null;
		switch(recognizedCoin){
		case 001: img = getToolkit().getImage(getClass().getResource("bitmaps\\C001.jpg")); break;
		case 002: img = getToolkit().getImage(getClass().getResource("bitmaps\\C002.jpg")); break;
		case 005: img = getToolkit().getImage(getClass().getResource("bitmaps\\C005.jpg")); break;
		case 010: img = getToolkit().getImage(getClass().getResource("bitmaps\\C010.jpg")); break;
		case 020: img = getToolkit().getImage(getClass().getResource("bitmaps\\C020.jpg")); break;
		case 050: img = getToolkit().getImage(getClass().getResource("bitmaps\\C050.jpg")); break;
		case 100: img = getToolkit().getImage(getClass().getResource("bitmaps\\C100.jpg")); break;
		case 200: img = getToolkit().getImage(getClass().getResource("bitmaps\\C200.jpg")); break;
		default:  img = getToolkit().getImage(getClass().getResource("bitmaps\\C000.jpg")); break;
		}
		g.drawImage(img, (width-100)/2, 200, this);
	}
	
	public static void main(String[] args){
		Frame window = new gui();
	}
}
