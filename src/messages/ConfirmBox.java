package messages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
	
	private static boolean answer;
	private static Stage confirmStage;
	// @FXML
	// private static Label confirmMessage;
	@FXML
	private Button yes;
	@FXML
	private Button no;
	
	@FXML
	private void yes(ActionEvent e) {
		answer = true;
		confirmStage.close();
	}
	
	@FXML
	private void no(ActionEvent e) {
		answer = false;
		confirmStage.close();
	}	
	
	public static boolean displayM() {
		try {
			Parent root = (Parent) FXMLLoader.load(ConfirmBox.class.getClass().getResource("/messages/ConfirmBox.fxml"));			
			
			Scene scene = new Scene(root);					
			
			confirmStage = new Stage();
			confirmStage.setScene(scene);
			confirmStage.setTitle("DreamReading");			
			confirmStage.setOnCloseRequest(e -> {
				answer = false;
				confirmStage.close();
			});		
			confirmStage.initModality(Modality.APPLICATION_MODAL);		
			confirmStage.showAndWait();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return answer;
	}	
}
