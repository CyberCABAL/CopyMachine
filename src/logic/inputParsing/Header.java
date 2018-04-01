package logic.inputParsing;

public class Header extends Incrementor {
	
	public Header(String data) {super(data);}

	@Override
	public String getContent() {return "[" + zeroes + startValue++ + "]";}
}