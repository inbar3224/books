package addContent;

import java.net.URL;
import java.util.ResourceBundle;

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

public class SearchScreenController implements Initializable {
	
	@FXML private TextField searchInput;
	@FXML private Button byFullName;
	@FXML private Button byAuthorName;
	@FXML private Button goBack;
	
	@FXML
	private void chooseSearchMethod(ActionEvent event) {
		HttpRequest httpRequest = new HttpRequest();
		int status;
		
		// Values we need for the search
		String input = searchInput.getText();
				
		// Listener awaits results from search
		InitiateContainer initiateContainer = new InitiateContainer();
		Responder responder = new Responder();
		initiateContainer.addListener(responder);
				
		// Passing values to HTTP request according to the button we selected 
		if(event.getSource() == byFullName) {
			status = httpRequest.request(input, 1);
		}
		else {
			status = httpRequest.request(input, 2);
		}
		
		// Get a response
		initiateContainer.getAResponse(status);		
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
