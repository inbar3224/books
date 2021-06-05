package addContent;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ResultsScreenController implements Initializable {
	
	private String searchWord;
	private String[][] resultsArray;
	
	@FXML private Button b;
	
	@FXML
	private void p(ActionEvent event) {
		// printResults();
	}
	
	public ResultsScreenController(String searchWord, String[][] resultsArray) {
		super();
		this.searchWord = searchWord;
		this.resultsArray = resultsArray;
	}
		
	// Only for now
	public void printResults() {
		for(int i = 0; i < resultsArray.length; i++) {
			for(int j = 0; j < 5; j++) {
				if(resultsArray[i][j] != null) {
					System.out.print(resultsArray[i][j] + ", ");
				}
			}
			System.out.println();
		}		
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		printResults();	
	}
}
