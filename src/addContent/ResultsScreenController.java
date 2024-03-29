package addContent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
import messages.SingleOrAllController;

/* Note to self - we didn't set controller in the fxml file because we set it in the SearchScreenController class */

public class ResultsScreenController implements Initializable {
	
	private String searchWord;
	private ObservableList<Book> resultsList;
	private ObservableList<Book> alreadyExist;
	
	@FXML private TextField keyWord;
	@FXML private TableView<Book> tableView;
	@FXML private TableColumn<Book, String> name;
	@FXML private TableColumn<Book, String> author;
	@FXML private TableColumn<Book, String> seriesStandAlone;
	@FXML private TableColumn<Book, String> index;
	@FXML private TableColumn<Book, String> published;
	@FXML private Button addSeries;
	@FXML private Button addBook;
	@FXML private Button goBack;
	
	/* Trying to add a book to the database-based library */
	@FXML
	private void addBook(ActionEvent event) {
		AlertBox message = new AlertBox();
		Book chosen = tableView.getSelectionModel().getSelectedItem();
		/* We chose a book */
		if(chosen != null) {
			readAll();
			boolean exist = alreadyExist.contains(chosen);
			/* The book doesn't exists in our library */
			if(exist == false) {
				/* The book is a stand-alone - we could just add it */
				if(chosen.getSeriesStandAlone().compareTo("Standalone") == 0) {
					insert(chosen.getName(), chosen.getAuthor(), chosen.getSeriesStandAlone(),
							chosen.getIndex(), chosen.myPublicationDate());
					message.displayM("/messages/InsertedBook.fxml");
				}
				/* The book is part of a series - should we add just this book or the whole series? */
				else {
					SingleOrAllOutcomeListener listener = new SingleOrAllOutcomeListener() {						
						@Override
						public void decision(int status, boolean answer) {
							if(status == 1) {
								if(answer == true) {
									/* Add as a single book */
									insert(chosen.getName(), chosen.getAuthor(), chosen.getSeriesStandAlone(),
											chosen.getIndex(), chosen.myPublicationDate());
									message.displayM("/messages/InsertedBook.fxml");
								}
								else {
									/* Add the whole series */
									insertAll(chosen.getSeriesStandAlone());
									message.displayM("/messages/InsertedSeries.fxml");
								}	
							}							
						}
					};					
					SingleOrAllController singleOrAll = new SingleOrAllController();
					singleOrAll.setSingleOrAllOutcomeListener(listener);
					singleOrAll.displayQuestion();									
				}				
			}
			/* The book already exists in our library */
			else {
				message.displayM("/messages/BookExists.fxml");
			} 
		}
		/* No book was chosen */
		else {
			message.displayM("/messages/NoBookChosen.fxml");
		}			
	}
	
	/* Trying to add a series to the database-based library */
	@FXML 
	private void addSeries(ActionEvent event) {
		AlertBox message = new AlertBox();
		Book chosen = tableView.getSelectionModel().getSelectedItem();
		/* We chose a series of books */
		if(chosen != null) {
			/* The book is a stand-alone - has to be added by the right button */
			if(chosen.getSeriesStandAlone().compareTo("Standalone") == 0) {
				message.displayM("/messages/SingleNotSeries.fxml");
			}
			/* Add the whole series */ 
			else {				
				insertAll(chosen.getSeriesStandAlone());
				message.displayM("/messages/InsertedSeries.fxml");
			}
		}
		/* Nothing was chosen */ 
		else {
			message.displayM("/messages/NoBookChosen.fxml");
		}			
	}
	
	/* Return to our search screen */
	@FXML
	private void goBack(ActionEvent event) {
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
	
	/* Constructor */
	public ResultsScreenController(String searchWord, ObservableList<Book> resultsArray) {
		super();
		this.searchWord = searchWord;
		this.resultsList = FXCollections.observableArrayList(resultsArray);
		this.alreadyExist = FXCollections.observableArrayList();
	}
	
	/* Read the entire existing library in order to see if the book / series already exist 
	 * In the case of failed connection or failed reading attempt - you'll be notified */
	public void readAll() {
		DBConnection temp = new DBConnection();
		Connection connection = temp.connect();
		
		if(connection != null) {
			PreparedStatement pStatement = null;
			String sql = "SELECT * FROM books";
			ResultSet rSet = null;
			try {				
				pStatement = connection.prepareStatement(sql);
				rSet = pStatement.executeQuery();
				/* While we still can - create a book instance and add it to our list */
				while(rSet.next()) {
					Book item = new Book(rSet.getString("Name"), rSet.getString("Author"), 
							rSet.getString("SeriesOrStandAlone"), rSet.getString("Number"),rSet.getString("PublicationDate"));
					alreadyExist.add(item);
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
	
	/* Insert selected books into my library (SQLite database)
	 * In the case of failed connection or failed insertion attempt - you'll be notified */
	public void insert(String name, String author, String seriesSA, String index, String date) {
		DBConnection temp = new DBConnection();
		Connection connection = temp.connect();
		
		if(connection != null) {
			PreparedStatement pStatement = null;
			String sql = "INSERT INTO books(Name, Author, SeriesOrStandAlone, Number, PublicationDate) VALUES(?,?,?,?,?)";
			/* Setting parameters */ 
			try {				
				pStatement = connection.prepareStatement(sql);
				pStatement.setString(1, name);
				pStatement.setString(2, author);
				pStatement.setString(3, seriesSA);
				pStatement.setString(4, index);
				pStatement.setString(5, date);
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
		}			
	}
	
	/* Insert the entire series */
	public void insertAll(String input) {
		HttpRequest httpRequest = new HttpRequest();		
		SearchOutcomeListener listener = new SearchOutcomeListener() {			
			@Override
			public void onEvent(int status, ObservableList<Book> resultsArray) {
				/* No Internet */ 
				if(status == 0) {
					AlertBox noInternet = new AlertBox();
					noInternet.displayM("/messages/NoInternet.fxml");
				}
				else if(status == 2) {
					readAll();
					boolean exist;
					for(Book book : resultsArray) {
						if(input.compareTo(book.getSeriesStandAlone()) == 0) {
							exist = alreadyExist.contains(book);
							if(exist == false) {
								insert(book.getName(), book.getAuthor(), book.getSeriesStandAlone(),
										book.getIndex(), book.myPublicationDate());
							}
						}						
					}					
				}
			}
		};
		httpRequest.setSearchOutcomeListener(listener);
		httpRequest.request(input, 1);
	}
	
	/* Initialize screen with all of it's data */ 
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		keyWord.getStyleClass().add("textArea");
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
		published.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));
		published.getStyleClass().add("textArea");
		tableView.setItems(resultsList);
		
		addBook.setOnAction(e -> addBook(e));
		addSeries.setOnAction(e -> addSeries(e));
		goBack.setOnAction(e -> goBack(e));				
	}
}
