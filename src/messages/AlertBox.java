package messages;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	
	/* Show alert message according to given fxml file - specific message for each problem */
	public void displayM(String fxmlFile) {
		Stage alertStage = new Stage();
		/* Settings for stage */
		try {			
			Parent root = (Parent) FXMLLoader.load(AlertBox.class.getClass().getResource(fxmlFile));			
			Scene scene = new Scene(root);
			alertStage.setScene(scene);			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		alertStage.setTitle("DreamReading");							
		alertStage.initModality(Modality.APPLICATION_MODAL);
		alertStage.setResizable(false);
		/* Showing stage */
		alertStage.showAndWait();		
	}
}
