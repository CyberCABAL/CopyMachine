package logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import logic.inputParsing.Parser;
import logic.inputParsing.ParserData;
import logic.inputParsing.Segment;
import javafx.stage.Stage;

/**
 * Contains the logic related to the copying.
 */
public class CopyMachine {
	private final static ExtensionFilter EF = new ExtensionFilter(
			"RA2/TS Maps",
			"*.map",
			"*.mpr",
			"*.yrm"
			);
	
	File target = null;
	
	public CopyMachine() {}

	public File getTarget() {return target;}

	/**
	 * Select a map file to copy to.
	 * @return Whether the file opened successfully or if anything was selected at all.
	 */
	public boolean selectFile() {
		FileChooser chooser = new FileChooser();
		if (target != null) {chooser.setInitialDirectory(target.getParentFile());}
		else {chooser.setInitialDirectory(new File(System.getProperty("user.home")));}
		chooser.getExtensionFilters().add(EF);
		chooser.setTitle("Select map file");
		File selection = chooser.showOpenDialog(new Stage());
		if (selection != null) {
			target = selection;
			return true;
		}
		else {return false;}
	}

	/**
	 * Loop through the entire file and add the extra sections.
	 * This actually deletes the original later and renames the copy to the same
	 * name. The reason for the whole copying business is so that if the program
	 * should crash halfway for any reason, the original map will be intact.
	 * 
	 * @param input The input section from the user.
	 * @return Whether the operation was successful.
	 */
	public boolean copy(String input, String sectionName, int amount) {
		File temp = new File(target.getParentFile() + File.separator + "temp");
		ParserData pData = Parser.parse(input);
		Segment[] data = pData.segments;
		int headerStart = pData.headerStart, end = headerStart + amount;
		boolean nameExists = (sectionName != null && sectionName.trim() != "");
		if (!nameExists) {sectionName = "Header";}
		try (
				BufferedWriter bw = new BufferedWriter(new FileWriter(temp, true));
				BufferedReader br = new BufferedReader(new FileReader(target))
				) {
			temp.createNewFile();
			String l = null, previous = null;
			boolean sectionFound = false, insertDone = false;
			while ((l = br.readLine()) != null) {
				if (l.trim().length() < 1) {
					bw.newLine();
					continue;
				}
				char c = l.charAt(0);
				if (!sectionFound && c == '[') {
					c = l.charAt(1);
					StringBuilder sb = new StringBuilder();
					for (int i = 1, length = l.length(); i < length && c != ']'; i++, c = l.charAt(i)) {sb.append(c);}
					if (sb.toString().equals(sectionName)) {sectionFound = true;}
				}
				else if (sectionFound && !insertDone && !Character.isDigit(c)) {
					// This takes in the buffered writer to save memory.
					if (nameExists) {
						listEntries(bw, previous, headerStart, end, pData.zeroes);
						bw.newLine();
					}
					while (amount-- > 0) {
						for (int i = 0; i < data.length; i++) {bw.write(data[i].getContent());}
						bw.newLine();
						bw.newLine();
					}
					insertDone = true;
				}
				bw.write(l);
				bw.newLine();
				if (!insertDone) {previous = l;}
			}
		}
		catch (IOException e0) {return false;}
		String copy = target.getAbsolutePath();
		if (target.delete()) {temp.renameTo(new File(copy));}
		else {
			System.out.println("Could not complete operation. This might be caused by the file being open in an other program.");
			temp.renameTo(new File(target.getParentFile() + File.separator + "copy.map"));
			return false;
		}
		return true;
	}
	
	/**
	 * Help method to create the list entries.
	 */
	private void listEntries(BufferedWriter bw, String previous, int start, int end, String zeroes) throws IOException {
		int writeStart = -1;
		StringBuilder sb = new StringBuilder();
		char c;
		for (int i = 0, l = previous.length(); i < l && writeStart == -1; i++) {
			c = previous.charAt(i);
			if (Character.isDigit(c)) {sb.append(c);}
			else {writeStart = Integer.parseInt(sb.toString());}
		}
		for (int i = start; i < end; i++) {bw.write(++writeStart + "=" + zeroes + i + "\n");}
	}
}
