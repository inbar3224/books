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
	
	/* Show a yes / no question according to given fxml file - specific message for each question 
	 * Also, return the answer to the caller */
	public boolean displayM(String fxmlFile) {
		try {
			// Settings for stage
			Parent root = (Parent) FXMLLoader.load(ConfirmBox.class.getClass().getResource(fxmlFile));			
			
			Scene scene = new Scene(root);	
			
			Stage confirmStage = new Stage();			
			confirmStage.setScene(scene);
			confirmStage.setTitle("DreamReading");			
			confirmStage.setOnCloseRequest(e -> {
				answer = false;
				confirmStage.close();
			});		
			confirmStage.initModality(Modality.APPLICATION_MODAL);
			confirmStage.setResizable(false);
			// Showing stage
			confirmStage.showAndWait();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return answer;
	}	
}
