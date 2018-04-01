package logic.inputParsing;

public class StaticText implements Segment{
	private String data;
	
	public StaticText(String data) {this.data = data;}

	@Override
	public String getContent() {return data;}

}
