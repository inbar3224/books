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
import javafx.stage.Modality;
import javafx.stage.Stage;
import mainScreen.MainScreen;

public class AddContent {
	
	public static Stage searchStage;
	private String input;
	
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
		searchStage.close();
		HomeScreen.presentHome();
	}
	
	public static void chooseSearchMethod() {
		try {
			Parent root = (Parent) FXMLLoader.load(AddContent.class.getClass().getResource("/addContent/AddContent.fxml"));			
			
			Scene scene = new Scene(root);			
			
			searchStage = new Stage();
			searchStage.setScene(scene);
			searchStage.setTitle("DreamReading");
			searchStage.setOnCloseRequest(e -> {
				e.consume();
				MainScreen.confirmClose(searchStage);
			});
			searchStage.initModality(Modality.APPLICATION_MODAL);
			searchStage.setResizable(false);
			searchStage.show();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}	
	}	
}
