package addContent;

import java.net.URL;
import java.util.ResourceBundle;

import homeScreen.HomeScreenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mainScreen.DreamReading;
import messages.AlertBox;

public class SearchScreenController implements Initializable {
	
	// Temporarily
	public static int Status = 0;
	
	@FXML private TextField searchInput;
	@FXML private Button byFullName;
	@FXML private Button byAuthorName;
	@FXML private Button goBack;
	
	@FXML
	private void chooseSearchMethod(ActionEvent event) {
		String input = searchInput.getText();
		if(event.getSource() == byFullName) {
			HttpRequest.request(input, 1);
			HomeScreenController.results = 1;
		}
		else {
			HttpRequest.request(input, 2);
			HomeScreenController.results = 2;
		}
		
		// No Internet
		if(Status == 0) {
			AlertBox.displayM("/messages/NoInternet.fxml");
		}
		// No results
		else if(Status == 1) {
			AlertBox.displayM("/messages/NoResults.fxml");
		}
		// results
		else if(Status == 2) {
			
		}
	}
	
	// Returns to home screen
	@FXML
	private void goBack(ActionEvent event) {
		try {
			Stage homeScreenWindow = DreamReading.getPrimaryStage();	
			
			// Settings for stage
			Parent homeScreenParent = FXMLLoader.load(getClass().getResource("/homeScreen/HomeScreen.fxml"));				
			Scene homeScreenScene = new Scene(homeScreenParent);
			homeScreenWindow.setScene(homeScreenScene);
			// Showing stage
			homeScreenWindow.show();							
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}	
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {}
}
