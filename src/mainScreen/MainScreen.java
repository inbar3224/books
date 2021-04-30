package mainScreen;

import java.sql.Connection;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import homeScreen.HomeScreen;
import messages.AlertBox;
import messages.ConfirmBox;

public class MainScreen extends Application { 
	
	@FXML
	private Button pressButton;
	
	// when we enter the app, we have to be connected to the database. otherwise, there is a "fail" message
	@FXML
	private void connectionButton(ActionEvent e) {
		Connection start = DBConnection.connect();
		if(start != null) {
			HomeScreen.presentHome();
		}
		else {
			AlertBox.displayM("/messages/NoDatabase.fxml");
		}
	}
	
	// launching the java desktop app
	public static void main(String[] args) {
		launch(args); 
	}
	
	// showing main screen (primary stage)
	@Override
	public void start(Stage primaryStage) {
		try {
			// settings for stage
			Parent root = (Parent) FXMLLoader.load(getClass().getResource("MainScreen.fxml"));			
			
			Scene scene = new Scene(root);			
			
			primaryStage.setTitle("DreamReading");
			primaryStage.setScene(scene);
			// before the stage closes, we have to wait and confirm it
			primaryStage.setOnCloseRequest(e -> {
				e.consume();
				confirmClose(primaryStage);
			});
			primaryStage.setResizable(false);
			// showing stage
			primaryStage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	// we have to confirm we indeed wanted to close the application 
	public static void confirmClose(Stage primaryStage) {
		boolean result = ConfirmBox.displayM("/messages/ConfirmBox.fxml");
		if(result == true) {
			Platform.exit();
		}
	}
}
