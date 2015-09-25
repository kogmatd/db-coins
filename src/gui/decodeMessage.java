package gui;

public class decodeMessage {
	static private int coin = 0;
	static private boolean sure = false;
	static private int start = -1;
	static private char[] coinCharArray = new char[3];
	
	public static void decode(String msg){
		start = msg.indexOf("res:") + 5;
		if (start >= 5){
			sure = (msg.charAt(start) != '(');
			if (!sure) {start += 1;}
			msg.getChars(start + 1, start + 4, coinCharArray, 0);
			coin = Integer.parseInt(String.valueOf(coinCharArray));
		} else {
			sure = true;
			coin = 0;
		}
	}
	
	public static int getCoin() {
		return coin;
	}
	
	public static boolean getSure() {
		return sure;
	}
}
