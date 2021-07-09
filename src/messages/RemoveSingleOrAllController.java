package messages;

import addContent.SingleOrAllOutcomeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RemoveSingleOrAllController {
	
	private SingleOrAllOutcomeListener listener;
	private static int status = 2;
	private static boolean answer;
	
	@FXML private Button single;
	@FXML private Button all;
		
	// Choose how to add the item
	@FXML
	private void getAnAnswer(ActionEvent event) {
		if(event.getSource() == single) {
			status = 1;
			answer = true;
		}
		else {
			status = 1;
			answer = false;
		}
		Stage QuestionScreenWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		QuestionScreenWindow.close();
	}
	
	// set listener
	public void setSingleOrAllOutcomeListener(SingleOrAllOutcomeListener listener) {
		this.listener = listener;
	}
	
	/* The caller asks us whether we want to add a single book or a whole series 
	 * We must return an answer */
	public void displayQuestion() {
		Stage questionOfRemovalStage = new Stage();
		// Settings for stage
		try {			
			Parent root = (Parent) FXMLLoader.load(ConfirmBox.class.getClass().getResource("/messages/RemoveSingleOrAll.fxml"));			
			Scene scene = new Scene(root);	
			questionOfRemovalStage.setScene(scene);						
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		questionOfRemovalStage.setTitle("DreamReading");			
		questionOfRemovalStage.initModality(Modality.APPLICATION_MODAL);
		questionOfRemovalStage.setResizable(false);
		// In case we don't want to add anything
		questionOfRemovalStage.setOnCloseRequest(e -> {
			status = 2;
			listener.decision(status, answer);
			questionOfRemovalStage.close();
		});		
		// Showing stage
		questionOfRemovalStage.showAndWait();
		listener.decision(status, answer);
	}	
}
