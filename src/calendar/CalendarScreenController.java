package calendar;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import addContent.Book;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CalendarScreenController implements Initializable {
	
	@FXML DatePicker calendar;
	@FXML TableView<Book> tableView1;
	@FXML private TableColumn<Book, String> name1;
	@FXML private TableColumn<Book, String> author1;
	@FXML private TableColumn<Book, String> seriesStandAlone1;
	@FXML private TableColumn<Book, String> index1;
	@FXML private TableColumn<Book, String> published1;
	@FXML TableView<Book> tableView2;
	@FXML private TableColumn<Book, String> name2;
	@FXML private TableColumn<Book, String> author2;
	@FXML private TableColumn<Book, String> seriesStandAlone2;
	@FXML private TableColumn<Book, String> index2;
	@FXML private TableColumn<Book, String> published2;
	@FXML Button goBack;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		LocalDate tDate = calendar.getValue();
		if(tDate != null) {
			
		}		
	}
	
	

}
