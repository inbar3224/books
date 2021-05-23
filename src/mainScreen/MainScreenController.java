package mainScreen;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import messages.AlertBox;
import messages.ConfirmBox;

public class MainScreenController implements Initializable {
	
	@FXML private Button pressButton;
	
	/* We have to be connected to the database in order to use the application properly. 
	 * Otherwise, there is a "fail" message
	 */
	@FXML 
	private void connectionButton(ActionEvent event) {
		Connection start = DBConnection.connect();
		if(start != null) {
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
		else {
			AlertBox.displayM("/messages/NoDatabase.fxml");
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Stage window = DreamReading.getPrimaryStage();
		
		window.setTitle("DreamReading");
		// Before the stage closes, we have to wait and confirm the closing request
		window.setOnCloseRequest(e -> {
			e.consume();
			confirmClose(window);
		});
		window.setResizable(false);
	}
	
	// We have to confirm we indeed wanted to close the application 
	public static void confirmClose(Stage primaryStage) {
		boolean result = ConfirmBox.displayM("/messages/ConfirmBox.fxml");
		if(result == true) {
			Platform.exit();
		}
	}
}
