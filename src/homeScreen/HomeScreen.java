package homeScreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import addContent.AddContent;
import mainScreen.MainScreen;

public class HomeScreen {
	
	public static Stage homeStage;
	public static int responseStatus = 0;
	public static Initiater initiater;
	public static Responder responder;
		
	@FXML
	private Button searchBooks;
	@FXML
	private Button viewMyLibrary;
	@FXML
	private Button booksCalendar;
	@FXML
	private Button myReadingChallenges;
	@FXML
	private Button instructions;
	@FXML
	private Button goBack;
	
	// creates a listener and then sends us to choose a search method
	// once the search is complete, the listener would get the result and show us the appropriate response 
	@FXML
	private void search(ActionEvent e) {
		homeStage.close();
		
		initiater = new Initiater();
        responder = new Responder();
        initiater.addListener(responder);
		
		AddContent.chooseSearchMethod();       
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
	
	// returns to main screen
	@FXML
	private void goBack(ActionEvent e) {
		homeStage.close();
	}
	
	// show the home screen stage
	public static void presentHome() {
		try {
			Parent root = (Parent) FXMLLoader.load(HomeScreen.class.getClass().getResource("/homeScreen/HomeScreen.fxml"));
			
			Scene scene = new Scene(root);
					
			homeStage = new Stage();
			homeStage.setScene(scene);
			homeStage.setTitle("DreamReading");	
			homeStage.initModality(Modality.APPLICATION_MODAL);
			homeStage.setOnCloseRequest(e -> {
				e.consume();
				MainScreen.confirmClose(homeStage);
			});
			homeStage.setResizable(false);
			homeStage.show();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
