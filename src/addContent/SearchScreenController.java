package addContent;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
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
	
	@FXML private TextField searchInput;
	@FXML private Button byFullName;
	@FXML private Button byAuthorName;
	@FXML private Button goBack;
	
	/* Gets input from user
	 * Send an HTTP request according to selected search method
	 * Once a search outcome has been dispatched from the HTTPRequest instance,
	 * The listener performs a proper response */
	@FXML
	private void chooseSearchMethod(ActionEvent event) {
		String input = searchInput.getText();		
		HttpRequest httpRequest = new HttpRequest();
		
		SearchOutcomeListener listener = new SearchOutcomeListener() {			
			@Override
			public void onEvent(int status, ObservableList<Book> resultsArray) {
				// No Internet
				if(status == 0) {
					AlertBox noInternet = new AlertBox();
					noInternet.displayM("/messages/NoInternet.fxml");
				}
				// No results
				else if(status == 1) {
					AlertBox noResults = new AlertBox();
					noResults.displayM("/messages/NoResults.fxml");
				}
				// results
				else if(status == 2) {
					Stage resultsScreenWindow = DreamReading.getPrimaryStage();	
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/addContent/ResultsScreen.fxml"));						
					ResultsScreenController controller = new ResultsScreenController(input, resultsArray);
					loader.setController(controller);
					// Settings for stage
					try {												
						Parent resultsScreenParent = loader.load();						
						Scene resultsScreenScene = new Scene(resultsScreenParent);
						resultsScreenWindow.setScene(resultsScreenScene);													
					}
					catch (Exception exception) {
						exception.printStackTrace();
					}
					// Showing stage
					resultsScreenWindow.show();
				}		
			}
		};		
		httpRequest.setSearchOutcomeListener(listener);
		
		if(event.getSource() == byFullName) {
			httpRequest.request(input, 1);
		}
		else {
			httpRequest.request(input, 2);
		}		
	}
	
	// Returns to home screen
	@FXML
	private void goBack(ActionEvent event) {
		Stage homeScreenWindow = DreamReading.getPrimaryStage();		
		// Settings for stage
		try {			
			Parent homeScreenParent = FXMLLoader.load(getClass().getResource("/homeScreen/HomeScreen.fxml"));				
			Scene homeScreenScene = new Scene(homeScreenParent);
			homeScreenWindow.setScene(homeScreenScene);										
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		// Showing stage
		homeScreenWindow.show();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {}
}
