package calendar;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import addContent.Book;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import mainScreen.DBConnection;
import mainScreen.DreamReading;

public class CalendarScreenController implements Initializable {
	
	private ObservableList<Book> library;
	private ObservableList<Book> permanent;
	private ObservableList<Book> specificDate;
	
	@FXML DatePicker calendar;
	@FXML TableView<Book> booksOfTheWeek;
	@FXML private TableColumn<Book, String> name1;
	@FXML private TableColumn<Book, String> author1;
	@FXML private TableColumn<Book, String> seriesStandAlone1;
	@FXML private TableColumn<Book, String> index1;
	@FXML private TableColumn<Book, String> published1;
	@FXML TableView<Book> booksByDate;
	@FXML private TableColumn<Book, String> name2;
	@FXML private TableColumn<Book, String> author2;
	@FXML private TableColumn<Book, String> seriesStandAlone2;
	@FXML private TableColumn<Book, String> index2;
	@FXML Button goBack;
	
	/* Return to home screen */
	@FXML
	private void goBack(ActionEvent event) {
		Stage homeScreenWindow = DreamReading.getPrimaryStage();		
		/* Settings for stage */ 
		try {			
			Parent homeScreenParent = FXMLLoader.load(getClass().getResource("/homeScreen/HomeScreen.fxml"));				
			Scene homeScreenScene = new Scene(homeScreenParent);
			homeScreenWindow.setScene(homeScreenScene);										
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		/* Showing stage */ 
		homeScreenWindow.show();
	}
	
	/* Read the entire existing library so we could have books' publication dates 
	 * In the case of failed connection or failed reading attempt - you'll be notified */
	public void readAll() {
		library = FXCollections.observableArrayList();
		DBConnection temp = new DBConnection();
		Connection connection = temp.connect();
		
		if(connection != null) {
			PreparedStatement pStatement = null;
			String sql = "SELECT * FROM books";
			ResultSet rSet = null;
			try {				
				pStatement = connection.prepareStatement(sql);
				rSet = pStatement.executeQuery();
				while(rSet.next()) {
					Book item = new Book(rSet.getString("Name"), rSet.getString("Author"), 
							rSet.getString("SeriesOrStandAlone"), rSet.getString("Number"),rSet.getString("PublicationDate"));
					library.add(item);
				}			
			}
			catch (SQLException e) {
				System.out.println(e.toString());
			}
			finally {
				try {
					rSet.close();
				} 
				catch (SQLException e) {
					System.out.println(e.toString());
				}
				try {
					pStatement.close();					
				} 
				catch (SQLException e) {
					System.out.println(e.toString());
				}
				try {
					connection.close();
				} 
				catch (SQLException e) {
					System.out.println(e.toString());
				}
			}
		}
	}
	
	/* When we press a date at the calendar
	 * We get a list of books that are being published on that date */
	public void getPressedDate(ActionEvent event) {
		LocalDate date = calendar.getValue();
		checkForBooks(date, 0, specificDate);
		setTableView();
	}
	
	/* Find the books that have the same publication dates as the given one */
	public void checkForBooks(LocalDate date, int range, ObservableList<Book> miniLibrary) {
		LocalDate limit = date.plusDays(range);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		if(miniLibrary.isEmpty() == false) {
			miniLibrary.clear();
		}
		
		for(Book book : library) {
			if(book.myPublicationDate().compareTo("") != 0) {
				String tempMyDate = book.myPublicationDate().replace(".", "-");
				LocalDate myDate = LocalDate.parse(tempMyDate,formatter);
				if((myDate.isBefore(limit) && myDate.isAfter(date)) || myDate.compareTo(date) == 0) {
					miniLibrary.add(book);
				}
			}			
		}		
	}
	
	/* initialize values for the second table view */
	public void setTableView() {
		// setting data in the tableView
		name2.setCellValueFactory(new PropertyValueFactory<>("name"));
		name2.getStyleClass().add("textArea");
		author2.setCellValueFactory(new PropertyValueFactory<>("author"));
		author2.getStyleClass().add("textArea");
		seriesStandAlone2.setCellValueFactory(new PropertyValueFactory<>("seriesStandAlone"));
		seriesStandAlone2.getStyleClass().add("textArea");
		index2.setCellValueFactory(new PropertyValueFactory<>("index"));
		index2.getStyleClass().add("textArea");
		booksByDate.setItems(specificDate);
	}
	
	/* Paint all publication dates on the calendar so we wouldn't have to work so hare to find them */
	public void initializeCalendar(LocalDate today) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<LocalDate> dates = new ArrayList<LocalDate>();
		
		for(Book book : library) {
			if(book.myPublicationDate().compareTo("") != 0) {
				String tempMyDate = book.myPublicationDate().replace(".", "-");
				LocalDate myDate = LocalDate.parse(tempMyDate,formatter);
				if(myDate.isAfter(today) || myDate.compareTo(today) == 0) {
					dates.add(myDate);
				}
			}			
		}
		
		calendar.setDayCellFactory(new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(DatePicker param) {
				return new DateCell(){
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						if (!empty && item != null) {
							if(dates.contains(item)) {
								this.getStyleClass().add("selected");
							}
						}
					}
				};
			}
		});
	}
	
	/* Initialize all data for the screen */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		readAll();
		permanent = FXCollections.observableArrayList();
		specificDate = FXCollections.observableArrayList();
		LocalDate date = LocalDate.now();
		initializeCalendar(date);
		checkForBooks(date, 8, permanent);
		
		// setting data in the tableView
		name1.setCellValueFactory(new PropertyValueFactory<>("name"));
		name1.getStyleClass().add("textArea");
		author1.setCellValueFactory(new PropertyValueFactory<>("author"));
		author1.getStyleClass().add("textArea");
		seriesStandAlone1.setCellValueFactory(new PropertyValueFactory<>("seriesStandAlone"));
		seriesStandAlone1.getStyleClass().add("textArea");
		index1.setCellValueFactory(new PropertyValueFactory<>("index"));
		index1.getStyleClass().add("textArea");
		published1.setCellValueFactory(b -> new SimpleStringProperty(b.getValue().myPublicationDate()));
		published1.getStyleClass().add("textArea");
		booksOfTheWeek.setItems(permanent);	
	}
}
