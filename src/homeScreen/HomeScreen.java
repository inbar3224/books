package homeScreen;

import addContent.AddContent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomeScreen {
	
	static Stage homeStage;
	@FXML
	private Button addContent;
	@FXML
	private Button myLibrary;
	@FXML
	private Button calendar;
	@FXML
	private Button myChallenge;
	@FXML
	private Button instructions;
	
	@FXML
	private void search(ActionEvent e) {
		AddContent.chooseSearchMethod(homeStage);
	}
	
	@FXML
	private void library(ActionEvent e) {
		
	}
	
	@FXML
	private void list(ActionEvent e) {
		
	}
	
	@FXML
	private void challenge(ActionEvent e) {
		
	}
	
	@FXML
	private void instructions(ActionEvent e) {
		
	}
		
	public static void presentHome() {
		try {
			Parent root = (Parent) FXMLLoader.load(HomeScreen.class.getClass().getResource("/homeScreen/HomeScreen.fxml"));
			
			Scene scene = new Scene(root);
					
			homeStage = new Stage();
			homeStage.setScene(scene);
			homeStage.setTitle("DreamReading");				
			homeStage.show();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
