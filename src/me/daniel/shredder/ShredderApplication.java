package me.daniel.shredder;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ShredderApplication extends Application {

	public static void main(String[] args) throws IOException {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("shredder.fxml"));
		
		stage.setTitle("File Shredder");
		stage.setScene(new Scene(root, 549, 530));
		stage.setResizable(false);
		stage.show();
	}

}
