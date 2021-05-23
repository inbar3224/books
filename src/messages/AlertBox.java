package messages;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	
	// Show alert message according to given fxml file - specific message for each problem
	public static void displayM(String fxmlFile) {
		try {
			// Settings for stage
			Parent root = (Parent) FXMLLoader.load(AlertBox.class.getClass().getResource(fxmlFile));			
			
			Scene scene = new Scene(root);			
			
			Stage alertStage = new Stage();
			alertStage.setTitle("DreamReading");	
			alertStage.setScene(scene);					
			alertStage.initModality(Modality.APPLICATION_MODAL);
			alertStage.setResizable(false);
			// Showing stage
			alertStage.showAndWait();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
