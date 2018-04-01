package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import logic.CopyMachine;

/**
 * Code responding to the JavaFX buttons.
 */
public class Controls {
	private CopyMachine cm = new CopyMachine();
	private final static String[] statuses = {
			"You must select a file first.",
			"Operation was successful.",
			"Something went wrong.",
			"Amount must be an integer value!",
			"Section field is empty.",
			"Operation in progress..."
	};

	@FXML
	private Label status, selected;
	// @FXML
	// private Button execute;
	// Perhaps make it more restricted, and require to fill all fields?
	@FXML
	private TextField amount, type;
	@FXML
	private TextArea data;

	public void terminate() {
		//System.out.println("Terminating...");
		System.exit(0);
	}

	/**
	 * Select a file.
	 */
	public void select() {
		String text = null;
		if (cm.selectFile() || cm.getTarget() != null) {text = "Selected: " + cm.getTarget().getAbsolutePath();}
		else {text = "No file selected.";}
		selected.setText(text);
	}

	/*
	 * Do the selected task.
	 */
	public void execute() {
		if (cm.getTarget() == null) {
			status.setText(statuses[0]);
			return;
		}
		int sAmount = parseText(amount);
		if (sAmount < 1) {
			status.setText(statuses[3]);
			return;
		}
		String sData = data.getText();
		if (sData == null || sData.trim() == "") {
			status.setText(statuses[4]);
			return;
		}
		status.setText(statuses[5]);
		if (cm.copy(sData, type.getText(), sAmount)) {status.setText(statuses[1]);}
		else {status.setText(statuses[2]);}
	}

	/**
	 * Parse the text and check if everything is integers.
	 */
	private int parseText(TextField t) {
		String s = t.getText();
		if (!s.matches("[0-9]+")) {return -1;}
		return Integer.parseInt(s);
	}
}
