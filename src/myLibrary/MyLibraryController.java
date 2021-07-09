package myLibrary;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import addContent.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import mainScreen.DBConnection;
import mainScreen.DreamReading;
import messages.AlertBox;

public class MyLibraryController implements Initializable {
	
	private ObservableList<Book> library;
	
	@FXML private TextField keyWord;
	@FXML private TableView<Book> tableView;
	@FXML private TableColumn<Book, String> name;
	@FXML private TableColumn<Book, String> author;
	@FXML private TableColumn<Book, String> seriesStandAlone;
	@FXML private TableColumn<Book, String> index;
	@FXML private TableColumn<Book, String> published;
	@FXML private Button deleteSeries;
	@FXML private Button deleteBook;
	@FXML private Button goBack;
	
	@FXML
	private void deleteBook(ActionEvent event) {
		
	}
	
	@FXML
	private void deleteSeries(ActionEvent event) {
		
	}
	
	@FXML
	private void goBack(ActionEvent event) {
		Stage homeScreenWindow = DreamReading.getPrimaryStage();		
		// Settings for stage
		try {			
			Parent homeScreenParent = FXMLLoader.load(getClass().getResource("/homeScreen/HomeScreen.fxml"));				
			Scene homeScreenScene = new Scene(homeScreenParent);
			homeScreenWindow.setScene(homeScreenScene);										
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		// Showing stage
		homeScreenWindow.show();
	}
	
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
		else {
			AlertBox noDataBase = new AlertBox();
			noDataBase.displayM("/messages/NoDatabase.fxml");
		}		
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		readAll();
		
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		name.getStyleClass().add("textArea");
		author.setCellValueFactory(new PropertyValueFactory<>("author"));
		author.getStyleClass().add("textArea");
		seriesStandAlone.setCellValueFactory(new PropertyValueFactory<>("seriesStandAlone"));
		seriesStandAlone.getStyleClass().add("textArea");
		index.setCellValueFactory(new PropertyValueFactory<>("index"));
		index.getStyleClass().add("textArea");
		published.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));
		published.getStyleClass().add("textArea");
		tableView.setItems(library);		
	}
}
