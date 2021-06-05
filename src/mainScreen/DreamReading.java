package mainScreen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DreamReading extends Application {
	
	private static Stage realStage;
	
	// Launching the java desktop application
	public static void main(String[] args) {
		launch(args); 
	}
	
	// Showing main screen (primary stage)
	@Override
	public void start(Stage primaryStage) {
		try {
			setPrimaryStage(primaryStage);
			
			// Settings for stage
			Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));			
			Scene scene = new Scene(root);
			realStage.setScene(scene);
			// Showing stage
			realStage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/* So we'll be able to get the stage from anywhere in the application
	 * In order to change screens */
	public static Stage getPrimaryStage() {
        return realStage;
    }
	
	// Set temporary stage to be our real stage
    private void setPrimaryStage(Stage primaryStage) {
        realStage = primaryStage;
    }	
}
