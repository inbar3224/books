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
import javafx.scene.control.Label;
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
	private int number = 0;
	
	@FXML private Label numberOfBooks;
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
	
	/* Trying to delete a book from the library */
	@FXML
	private void deleteBook(ActionEvent event) {
		AlertBox message = new AlertBox();
		Book chosen = tableView.getSelectionModel().getSelectedItem();
		/* We chose a book */
		if(chosen != null) {
			/* The book is a stand-alone - we could just remove it */
			if(chosen.getSeriesStandAlone().compareTo("Standalone") == 0) {
				delete(chosen);				
				adjustments();
				message.displayM("/messages/BookWasDeleted.fxml");
			}
			/* The book is part of a series - should we remove just this book or the whole series? */
			else {
				SingleOrAllOutcomeListener listener = new SingleOrAllOutcomeListener() {					
					@Override
					public void decision(int status, boolean answer) {
						if(status == 1) {
							if(answer == true) {
								/* remove a single book */
								delete(chosen);
								adjustments();
								message.displayM("/messages/BookWasDeleted.fxml");
							}
							else {
								/* remove the whole series */
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
		/* No book was chosen */
		else {
			message.displayM("/messages/NoBookChosen.fxml");
		}		
	}
	
	/* Trying to delete a series from the library */
	@FXML
	private void deleteSeries(ActionEvent event) {
		AlertBox message = new AlertBox();
		Book chosen = tableView.getSelectionModel().getSelectedItem();
		/* We chose a series of books */
		if(chosen != null) {
			/* The book is a stand-alone - has to be removed by the right button */ 
			if(chosen.getSeriesStandAlone().compareTo("Standalone") == 0) {
				message.displayM("/messages/RemoveSingleNotSeries.fxml");
			}
			/* remove the whole series */
			else {				
				deleteAll(chosen.getSeriesStandAlone());
				message.displayM("/messages/SeriesWasDeleted.fxml");
			}
		}
		/* Nothing was chosen */
		else {
			message.displayM("/messages/NoBookChosen.fxml");
		}
	}
	
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
	
	/* Delete item from the database-based library */
	public void delete(Book wanted) {
		DBConnection temp = new DBConnection();
		Connection connection = temp.connect();
		
		if(connection != null) {
			PreparedStatement pStatement = null;
			String sql = "DELETE FROM books WHERE Name = ? AND Author = ?";
			/* Setting parameters */
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
		}				
	}
	
	/* Delete a whole series of books */
	public void deleteAll(String input) {
		ObservableList<Book> deleteArray = FXCollections.observableArrayList();
		for(Book book : library) {
			if(input.compareTo(book.getSeriesStandAlone()) == 0) {
				deleteArray.add(book);
			}
		}
		
		for(Book book : deleteArray) {
			delete(book);
		}
		
		reloadScreen();
	}
	
	/* When we delete a series, a lot of items disappear from the screen all at once
	 * Instead of changing the tableView one row at a time, it's best to just reload the screen */
	public void reloadScreen() {
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
	
	/* Read the entire library from the database so we could present it on screen */
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
					number++;
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
	
	/* Change number of books on the screen after every delete */
	public void setNumberOfBooks(int number) {
		String numberToString = Integer.toString(number) + " books";
		numberOfBooks.setText(numberToString);
	}
	
	/* Make necessary adjustment on the screen after deleting a single book */
	public void adjustments() {
		tableView.getSelectionModel().clearSelection();
		number--;
		setNumberOfBooks(number);
	}
	
	/* Initialize all data for the screen */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		/* Getting data from the database */
		readAll();
		setNumberOfBooks(number);
		
		/* Setting data in the tableView */
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
		
		/* Filter results through search */
		FilteredList<Book> filteredData = new FilteredList<>(library, b -> true);
		keyWord.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(book -> {
				/* If keyWord is empty, display all books */
				if(newValue == null || newValue.isEmpty()) {
					return true;
				}
				/* compare by lower case letters */
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
		
		/* "Convert" the filtered list in a sorted list.
		 * Bind it's comparator to the table view comparator - so we'll be able to sort the sorted list by column
		 * Add sorted (and filtered) data to the table. */
		SortedList<Book> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedData);		
	}
}
