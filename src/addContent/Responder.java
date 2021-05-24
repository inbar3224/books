package addContent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mainScreen.DreamReading;
import messages.AlertBox;

public class Responder implements ResultListener {
	
	// Execute the abstract method - deliver the appropriate response
	@Override
	public void response(int result) {		
		// No Internet
		if(result == 0) {
			AlertBox.displayM("/messages/NoInternet.fxml");
		}
		// No results
		else if(result == 1) {
			AlertBox.displayM("/messages/NoResults.fxml");
		}
		// results
		else if(result == 2) {
			try {
				Stage resultsScreenWindow = DreamReading.getPrimaryStage();	
				
				// Settings for stage
				Parent resultsScreenParent = FXMLLoader.load(getClass().getResource("/addContent/ResultsScreen.fxml"));				
				Scene resultsScreenScene = new Scene(resultsScreenParent);
				resultsScreenWindow.setScene(resultsScreenScene);
				// Showing stage
				resultsScreenWindow.show();							
			}
			catch (Exception exception) {
				exception.printStackTrace();
			}		
		}		
	}
}
