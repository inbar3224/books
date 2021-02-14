package addContent;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class SearchResults implements Initializable {
	
	public static Stage tableStage;
	public static String input;
	@FXML
	private static TableView<String> table;
	@FXML
	private Button followBook;
	@FXML
	private Button followSeries;
	@FXML
	public static Label searchKey;
	@FXML
	private Button goBack;
	
	@FXML
    public static void init() {
        searchKey.setText(input);
    }
	
	@FXML
	private void goBack(ActionEvent e) {
		tableStage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("hiiiiiiiiiiiiiiii");
		Label temp = new Label(SearchResults.input);
		temp.setLayoutX(150);
		temp.setLayoutY(50);
		temp.setAlignment(Pos.CENTER);
		
		
		searchKey.setText(input);
		/*try {
			// searchKey = new Label(input);
			// searchKey.setText(input);
			System.out.println("h222");
		}
		catch (Exception e) {
			// TODO: handle exception
		}*/
		// searchKey.setText(input);		
	}
	
	/*public static void launchResultScreen() {
		try {
			Parent root = (Parent) FXMLLoader.load(SearchResults.class.getClass().getResource("/addContent/SearchResults.fxml"));
			
			Scene scene = new Scene(root);
						
			tableStage = new Stage();
			tableStage.setScene(scene);
			tableStage.setTitle("DreamReading");
			tableStage.initModality(Modality.APPLICATION_MODAL);
			tableStage.show();
			initialize();			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}	
	}*/
	
	
}
