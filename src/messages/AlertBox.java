package messages;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	
	static Stage alertStage;
				
	public static void displayM(String fxmlFile) {
		try {
			Parent root = (Parent) FXMLLoader.load(AlertBox.class.getClass().getResource(fxmlFile));			
			
			Scene scene = new Scene(root);			
			
			alertStage = new Stage();
			alertStage.setScene(scene);
			alertStage.setTitle("DreamReading");			
			alertStage.initModality(Modality.APPLICATION_MODAL);		
			alertStage.showAndWait();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
