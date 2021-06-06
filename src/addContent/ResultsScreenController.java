package addContent;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

// Note to self - we didn't set controller in the fxml file because we set it in the SearchScreenController class

public class ResultsScreenController implements Initializable {
	
	private String searchWord;
	private String[][] resultsArray;
	
	@FXML private TextField keyWord;
	@FXML private TableView<Book> tableView;
	@FXML private TableColumn<Book, String> name;
	@FXML private TableColumn<Book, String> author;
	@FXML private TableColumn<Book, String> seriesStandAlone;
	@FXML private TableColumn<Book, String> index;
	
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
	
	public ObservableList<Book> getBooks(){
		ObservableList<Book> options = FXCollections.observableArrayList();
		for(int i = 0; i < resultsArray.length; i++) {
			options.add(new Book(resultsArray[i][0], resultsArray[i][1], resultsArray[i][2], resultsArray[i][3]));
		}
		return options;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		keyWord.setText(searchWord);
		keyWord.setEditable(false);
		
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		name.getStyleClass().add("textArea");
		author.setCellValueFactory(new PropertyValueFactory<>("author"));
		author.getStyleClass().add("textArea");
		seriesStandAlone.setCellValueFactory(new PropertyValueFactory<>("seriesStandAlone"));
		seriesStandAlone.getStyleClass().add("textArea");
		index.setCellValueFactory(new PropertyValueFactory<>("index"));
		index.getStyleClass().add("textArea");
		
		tableView.setItems(getBooks());
		
		printResults();		
	}
}
