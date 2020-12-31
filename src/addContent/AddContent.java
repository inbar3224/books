package addContent;

import java.io.IOException;

import homeScreen.HomeScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddContent {
	
	static Stage searchStage;
	String input;
	@FXML
	private Label searchTitle;
	@FXML
	private TextField searchInput;
	@FXML
	private Button byFullName;
	@FXML
	private Button byAuthorName;
	@FXML
	private Button goBack;
	
	@FXML
	private void searchFullName(ActionEvent e) {
		input = searchInput.getText();
		HttpRequest.request(input, 1);
	}
	
	@FXML
	private void searchAuthorName(ActionEvent e) {
		input = searchInput.getText();
		HttpRequest.request(input, 2);
	}
	
	@FXML
	private void goBack(ActionEvent e) {
		HomeScreen.presentHome();
		searchStage.close();
	}
	
	public static void chooseSearchMethod(Stage caller) {
		try {
			Parent root = (Parent) FXMLLoader.load(AddContent.class.getClass().getResource("/addContent/AddContent.fxml"));			
			
			Scene scene = new Scene(root);			
			
			searchStage = new Stage();
			searchStage.setScene(scene);
			searchStage.setTitle("DreamReading");
			searchStage.setOnCloseRequest(e -> {
				e.consume();
				HomeScreen.presentHome();
				searchStage.close();
			});
			searchStage.show();
			
			caller.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
