package homeScreen;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import mainScreen.DreamReading;

public class HomeScreenController implements Initializable {
	
	@FXML private Button searchBooks;
	@FXML private Button viewMyLibrary;
	@FXML private Button calendar;
	@FXML private Button myReadingChallenges;
	@FXML private Button goBack;
	@FXML private TextArea preview;
	
	@FXML
	private void search(ActionEvent event) {
		try {
			Stage searchScreenWindow = DreamReading.getPrimaryStage();
			// Settings for stage
			Parent searchScreenParent = FXMLLoader.load(getClass().getResource("/addContent/SearchScreen.fxml"));				
			Scene searchScreenScene = new Scene(searchScreenParent);
			searchScreenWindow.setScene(searchScreenScene);
			// Showing stage
			searchScreenWindow.show();
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}			  
	}
	
	@FXML
	private void library(ActionEvent event) {
		
	}
	
	@FXML
	private void Calendar(ActionEvent event) {
		
	}
	
	@FXML
	private void challengeList(ActionEvent event) {
		
	}
	
	// Returns to main screen
	@FXML
	private void goBack(ActionEvent event) {
		try {
			Stage mainScreenWindow = DreamReading.getPrimaryStage();
			// Settings for stage
			Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/mainScreen/MainScreen.fxml"));				
			Scene mainScreenScene = new Scene(mainScreenParent);
			mainScreenWindow.setScene(mainScreenScene);
			// Showing stage
			mainScreenWindow.show();
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}	
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// Settings for the screen
		preview.getStyleClass().add("textArea");
		preview.setText(ConstantStrings.generalText);
		preview.setEditable(false);
		
		searchBooks.setOnMouseEntered(e -> preview.setText(ConstantStrings.searchInstructions));
		searchBooks.setOnMouseExited(e -> preview.setText(ConstantStrings.generalText));		
	}	
}