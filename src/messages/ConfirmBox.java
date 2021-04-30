package messages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
	
	private static boolean answer;
	private static Stage confirmStage;
	
	@FXML
	private Button yes;
	@FXML
	private Button no;
	
	// confirm
	@FXML
	private void yes(ActionEvent e) {
		answer = true;
		confirmStage.close();
	}
	
	// refuse confirmation	
	@FXML
	private void no(ActionEvent e) {
		answer = false;
		confirmStage.close();
	}	
	
	// show a yes / no question according to the given fxml file - specific message for each question
	// also, return the answer to the caller
	public static boolean displayM(String fxmlFile) {
		try {
			Parent root = (Parent) FXMLLoader.load(ConfirmBox.class.getClass().getResource(fxmlFile));			
			
			Scene scene = new Scene(root);					
			
			confirmStage = new Stage();
			confirmStage.setScene(scene);
			confirmStage.setTitle("DreamReading");			
			confirmStage.setOnCloseRequest(e -> {
				answer = false;
				confirmStage.close();
			});		
			confirmStage.initModality(Modality.APPLICATION_MODAL);
			confirmStage.setResizable(false);
			confirmStage.showAndWait();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return answer;
	}	
}
