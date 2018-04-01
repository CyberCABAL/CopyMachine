package logic.inputParsing;

public class Incrementor implements Segment {
	protected int startValue = 0;
	protected byte type = 0;
	protected String zeroes = "";

	
	public Incrementor(String data) {
		StringBuilder sb = new StringBuilder();
		int i = 0, length = data.length();
		for (; i < length && data.charAt(i) == '0'; i++) {sb.append('0');}
		zeroes = sb.toString();
		char c;
		for (; i < length; i++) {
			c = data.charAt(i);
			if (Character.isDigit(c)) {sb.append(c);}
			else if (c == '_') {type = 1;}
		}
		startValue = Integer.parseInt(sb.toString());
	}

	@Override
	public String getContent() {return zeroes + ((type == 0) ? startValue++ : convertWaypoint(startValue++));}
	
	private String convertWaypoint(int i) {
		String res = "";
		i++;
		while (i > 0) {
			int r = --i % 26;
			res = (char)(r + 65) + res;
			i = (i - r) / 26;
		}
		return res;
	}
}