package myLibrary;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import addContent.Book;
import addContent.SingleOrAllOutcomeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import messages.RemoveSingleOrAllController;

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
		AlertBox message = new AlertBox();
		Book chosen = tableView.getSelectionModel().getSelectedItem();
		// We chose a book
		if(chosen != null) {
			// The book is a stand-alone - we could just add it
			if(chosen.getSeriesStandAlone().compareTo("Standalone") == 0) {
				delete(chosen);
				message.displayM("/messages/BookWasDeleted.fxml");
			}
			// The book is part of a series - should we add just this book or the whole series?
			else {
				SingleOrAllOutcomeListener listener = new SingleOrAllOutcomeListener() {					
					@Override
					public void decision(int status, boolean answer) {
						if(status == 1) {
							if(answer == true) {
								// remove a single book
								delete(chosen);
								message.displayM("/messages/BookWasDeleted.fxml");
							}
							else {
								// Add the whole series
								deleteAll(chosen.getSeriesStandAlone());
								message.displayM("/messages/SeriesWasDeleted.fxml");
							}	
						}	
					}
				};
				RemoveSingleOrAllController removeSingleOrAll = new RemoveSingleOrAllController();
				removeSingleOrAll.setSingleOrAllOutcomeListener(listener);
				removeSingleOrAll.displayQuestion();
			}
		}
		// No book was chosen
		else {
			message.displayM("/messages/NoBookChosen.fxml");
		}		
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
	
	public void delete(Book wanted) {
		DBConnection temp = new DBConnection();
		Connection connection = temp.connect();
		
		if(connection != null) {
			PreparedStatement pStatement = null;
			String sql = "DELETE FROM books WHERE Name = ? AND Author = ?";
			// Setting parameters
			try {				
				pStatement = connection.prepareStatement(sql);
				pStatement.setString(1, wanted.getName());
				pStatement.setString(2, wanted.getAuthor());
				pStatement.execute();
			}
			catch (SQLException e) {
				System.out.println(e.toString());
			}
			finally {
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
			library.remove(wanted);
			tableView.getSelectionModel().clearSelection();			
		} 
		else {
			AlertBox noDataBase = new AlertBox();
			noDataBase.displayM("/messages/NoDatabase.fxml");
		}		
	}
	
	public void deleteAll(String input) {
		for(Book book : library) {
			if(input.compareTo(book.getSeriesStandAlone()) == 0) {
				delete(book);
			}
		}
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
		// getting data from the database
		readAll();
		
		// setting data in the tableView
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
		
		// filter results through search
		FilteredList<Book> filteredData = new FilteredList<>(library, b -> true);
		keyWord.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(book -> {
				// if keyWord is empty, display all books
				if(newValue == null || newValue.isEmpty()) {
					return true;
				}
				// compare by lower case letters
				String lowerCaseFilter = newValue.toLowerCase();
				if(book.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				else if(book.getAuthor().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				else if(book.getSeriesStandAlone().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				else if(book.getIndex().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				else if(book.getPublicationDate().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				else {
					return false;
				}
			});
		});
		
		// Wrap the FilteredList in a SortedList. 
		SortedList<Book> sortedData = new SortedList<>(filteredData);
		// Bind the SortedList comparator to the TableView comparator - so we'll be able to sort the sorted list by column
		sortedData.comparatorProperty().bind(tableView.comparatorProperty());
		// Add sorted (and filtered) data to the table.
		tableView.setItems(sortedData);		
	}
}
