package homeScreen;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import mainScreen.DreamReading;

public class HomeScreenController implements Initializable {
	
	@FXML private Button searchBooks;
	@FXML private Button viewMyLibrary;
	@FXML private Button calendar;
	@FXML private Button goBack;
	@FXML private TextArea preview;
	
	/* Go to Search screen */
	@FXML
	private void search(ActionEvent event) {
		Stage searchScreenWindow = DreamReading.getPrimaryStage();
		/* Settings for stage */
		try {			
			Parent searchScreenParent = FXMLLoader.load(getClass().getResource("/addContent/SearchScreen.fxml"));				
			Scene searchScreenScene = new Scene(searchScreenParent);
			searchScreenWindow.setScene(searchScreenScene);			
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		/* Showing stage */
		searchScreenWindow.show();
	}
	
	/* Go to Library screen */
	@FXML
	private void library(ActionEvent event) {
		Stage libraryScreenWindow = DreamReading.getPrimaryStage();
		/* Settings for stage */
		try {
			Parent libraryScreenParent = FXMLLoader.load(getClass().getResource("/myLibrary/MyLibraryScreen.fxml"));
			Scene libraryScreenScene = new Scene(libraryScreenParent);
			libraryScreenWindow.setScene(libraryScreenScene);
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		/* Showing stage */
		libraryScreenWindow.show();
	}
	
	/* Go to Calendar screen */
	@FXML
	private void Calendar(ActionEvent event) {
		Stage calendarScreenWindow = DreamReading.getPrimaryStage();
		/* Settings for stage */
		try {
			Parent calendarScreenParent = FXMLLoader.load(getClass().getResource("/calendar/CalendarScreen.fxml"));
			Scene calendarScreenScene = new Scene(calendarScreenParent);
			calendarScreenWindow.setScene(calendarScreenScene);
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		/* Showing stage */
		calendarScreenWindow.show();
	}
	
	/* Return to Main screen */
	@FXML
	private void goBack(ActionEvent event) {
		Stage mainScreenWindow = DreamReading.getPrimaryStage();
		/* Settings for stage */
		try {			
			Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/mainScreen/MainScreen.fxml"));				
			Scene mainScreenScene = new Scene(mainScreenParent);
			mainScreenWindow.setScene(mainScreenScene);			
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		/* Showing stage */
		mainScreenWindow.show();
	}
	
	/* Initialize all data for the screen */ 
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		/* Settings for the screen */
		preview.getStyleClass().add("textArea");
		preview.setText(ConstantStrings.generalText);
		preview.setEditable(false);
		
		searchBooks.setOnMouseEntered(e -> preview.setText(ConstantStrings.searchInstructions));
		searchBooks.setOnMouseExited(e -> preview.setText(ConstantStrings.generalText));
		viewMyLibrary.setOnMouseEntered(e -> preview.setText(ConstantStrings.libraryInstructions));
		viewMyLibrary.setOnMouseExited(e -> preview.setText(ConstantStrings.generalText));
		calendar.setOnMouseEntered(e -> preview.setText(ConstantStrings.calendarInstructions));
		calendar.setOnMouseExited(e -> preview.setText(ConstantStrings.generalText));
	}	
}
