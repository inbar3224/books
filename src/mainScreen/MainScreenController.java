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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import messages.ConfirmBox;

public class MainScreenController implements Initializable {
	
	@FXML private TextArea preview;
	@FXML private Button pressButton;
	
	/* We have to be connected to the database in order to use the application properly. 
	 * Otherwise, there is a "fail" message */
	@FXML 
	private void connectionButton(ActionEvent event) {
		DBConnection temp = new DBConnection();
		Connection start = temp.connect();
		
		if(start != null) {
			Stage homeScreenWindow = DreamReading.getPrimaryStage();
			/* Settings for stage */
			try {				
				Parent homeScreenParent = FXMLLoader.load(getClass().getResource("/homeScreen/HomeScreen.fxml"));				
				Scene homeScreenScene = new Scene(homeScreenParent);
				homeScreenWindow.setScene(homeScreenScene);											
			}
			catch (Exception exception) {
				exception.printStackTrace();
			}
			/* Showing stage */
			homeScreenWindow.show();
		}		
	}

	/* We have to confirm we indeed wanted to close the application */ 
	public void confirmClose(Stage primaryStage) {
		ConfirmBox confirmClose = new ConfirmBox();
		boolean result = confirmClose.displayM();
		if(result == true) {
			Platform.exit();
		}
	}
	
	/*Initialize all data for the screen */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Stage window = DreamReading.getPrimaryStage();
		
		window.setTitle("DreamReading");
		preview.getStyleClass().add("textArea");
		preview.setEditable(false);
		/* Before the stage closes, we have to wait and confirm the closing request */
		window.setOnCloseRequest(e -> {
			e.consume();
			confirmClose(window);
		});
		window.setResizable(false);
	}	
}
