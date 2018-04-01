package logic.inputParsing;

public class ParserData {
	public String zeroes;
	public int headerStart;
	public Segment[] segments;
	
	ParserData(String zeroes, int headerStart, Segment[] segments) {
		this.zeroes = zeroes;
		this.headerStart = headerStart;
		this.segments = segments;
	}
}