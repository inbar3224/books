package mainScreen;

import java.sql.Connection;

import javafx.application.Application;
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
	
	DBConnection dbConnection;
	@FXML
	private Button press;
	
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
	
	public static void main(String[] args) {
		launch(args); 
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = (Parent) FXMLLoader.load(getClass().getResource("MainScreen.fxml"));			
			
			Scene scene = new Scene(root);			
			
			primaryStage.setTitle("DreamReading");
			primaryStage.setScene(scene);			
			primaryStage.setOnCloseRequest(e -> {
				e.consume();
				confirmClose(primaryStage);
			});			
			primaryStage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void confirmClose(Stage primaryStage) {
		boolean result = ConfirmBox.displayM();
		if(result == true) {
			primaryStage.close();
		}
	}
}
