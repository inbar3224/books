package messages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
	
	private static boolean answer;
		
	@FXML private Button yes;
	@FXML private Button no;
		
	// Confirm or deny
	@FXML
	private void getAnAnswer(ActionEvent event) {
		if(event.getSource() == yes) {
			answer = true;
		}
		else {
			answer = false;
		}
		Stage confirmScreenWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		confirmScreenWindow.close();
	}
	
	/* The caller asks us whether or not we want to close the application 
	 * We must return an answer */
	public boolean displayM() {
		Stage confirmStage = new Stage();
		// Settings for stage
		try {			
			Parent root = (Parent) FXMLLoader.load(ConfirmBox.class.getClass().getResource("/messages/ConfirmBox.fxml"));			
			Scene scene = new Scene(root);	
			confirmStage.setScene(scene);						
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		confirmStage.setTitle("DreamReading");			
		confirmStage.initModality(Modality.APPLICATION_MODAL);
		confirmStage.setResizable(false);
		confirmStage.setOnCloseRequest(e -> {
			answer = false;
			confirmStage.close();
		});		
		// Showing stage
		confirmStage.showAndWait();
		
		return answer;
	}	
}
