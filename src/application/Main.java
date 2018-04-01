package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage pStage) {
		Parent root = null;
		try {root = FXMLLoader.load(getClass().getResource("Main.fxml"));}
		catch (IOException e0) {e0.printStackTrace();}
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		pStage.setTitle("Copy Machine");
		pStage.setScene(scene);
		pStage.setResizable(false);
		pStage.show();
	}
	
	public static void main(String[] args) {
		//System.out.println("Activating...");
		launch(args);
	}
}