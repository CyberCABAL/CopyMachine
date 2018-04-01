package logic.inputParsing;

import java.util.ArrayList;

public class Parser {
	public static ParserData parse(String input) {
		ArrayList<Segment> s = new ArrayList<Segment>();
		StringBuilder sb = new StringBuilder();
		char[] data = input.toCharArray();
		String zeroes = "";
		int headerStart = -1;
		char c;
		for (int i = 0, l = input.length(); i < l; i++) {
			c = data[i];
			switch (c) {
			case '{':
			case '[':
				if (sb.length() > 0) {s.add(new StaticText(sb.toString()));}
				sb.setLength(0);
				break;
			case '}':
				s.add(new Incrementor(sb.toString()));
				sb.setLength(0);
				break;
			case ']':
				String temp = sb.toString();	// Could go back in the array, and just count length with int.
				sb.setLength(0);
				int j = 0, length = temp.length();
				for (; j < length && temp.charAt(j) == '0'; j++) {sb.append('0');}
				zeroes = sb.toString();
				for (; j < length; j++) {sb.append(temp.charAt(j));}
				temp = sb.toString();
				headerStart = Integer.parseInt(temp);
				s.add(new Header(temp));
				sb.setLength(0);
				break;
			default:
				sb.append(c);
				break;
			}
		}
		if (sb.length() > 0) {s.add(new StaticText(sb.toString()));}
		return new ParserData(zeroes, headerStart, s.toArray(new Segment[0]));
	}
}
