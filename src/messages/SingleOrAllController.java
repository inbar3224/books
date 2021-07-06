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

public class SingleOrAllController {
	
	private static boolean answer;
	
	@FXML private Button single;
	@FXML private Button all;
		
	// Choose how to add the item
	@FXML
	private void getAnAnswer(ActionEvent event) {
		if(event.getSource() == single) {
			answer = true;
		}
		else {
			answer = false;
		}
		Stage DecisionScreenWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		DecisionScreenWindow.close();
	}
	
	/* The caller asks us whether we want to add a single book or a whole series 
	 * We must return an answer */
	public boolean displayQuestion() {
		Stage questionStage = new Stage();
		// Settings for stage
		try {			
			Parent root = (Parent) FXMLLoader.load(ConfirmBox.class.getClass().getResource("/messages/SingleOrAll.fxml"));			
			Scene scene = new Scene(root);	
			questionStage.setScene(scene);						
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		questionStage.setTitle("DreamReading");			
		questionStage.initModality(Modality.APPLICATION_MODAL);
		questionStage.setResizable(false);
		/*questionStage.setOnCloseRequest(e -> {
			answer = false;
			questionStage.close();
		});*/		
		// Showing stage
		questionStage.showAndWait();
		
		return answer;
	}	
}
