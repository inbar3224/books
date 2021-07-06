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

// Note to self - we didn't set controller in the fxml file because we set it in the SearchScreenController class

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
	@FXML private Button addSeries;
	@FXML private Button addBook;
	@FXML private Button goBack;
	
	@FXML
	private void addBook(ActionEvent event) {
		AlertBox message = new AlertBox();
		Book chosen = tableView.getSelectionModel().getSelectedItem();
		if(chosen != null) {
			readAll();
			boolean exist = alreadyExist.contains(chosen);
			if(exist == false) {
				if(chosen.getSeriesStandAlone().compareTo("Standalone") == 0) {
					insert(chosen.getName(), chosen.getAuthor(), chosen.getSeriesStandAlone(),
							chosen.getIndex(), chosen.getPublicationDate());
					message.displayM("/messages/InsertedBook.fxml");
				}
				else {
					SingleOrAllController singleOrAll = new SingleOrAllController();
					boolean decision = singleOrAll.displayQuestion();
					if(decision == true) {
						// add as a single book
						insert(chosen.getName(), chosen.getAuthor(), chosen.getSeriesStandAlone(),
								chosen.getIndex(), chosen.getPublicationDate());
						message.displayM("/messages/InsertedBook.fxml");
					}
					else {
						// add series
						System.out.println("series");
					}					
				}				
			}
			else {
				message.displayM("/messages/BookExists.fxml");
			} 
		}
		else {
			message.displayM("/messages/NoBookChosen.fxml");
		}			
	}
	
	@FXML 
	private void addSeries(ActionEvent event) {
		AlertBox message = new AlertBox();
		Book chosen = tableView.getSelectionModel().getSelectedItem();
		if(chosen != null) {		
			if(chosen.getSeriesStandAlone().compareTo("Standalone") == 0) {
				message.displayM("/messages/SingleNotSeries.fxml");
			}
			else {
				
			}
		}
		else {
			message.displayM("/messages/NoBookChosen.fxml");
		}			
	}
	
	@FXML
	private void goBack(ActionEvent event) {
		Stage searchScreenWindow = DreamReading.getPrimaryStage();
		// Settings for stage
		try {			
			Parent searchScreenParent = FXMLLoader.load(getClass().getResource("/addContent/SearchScreen.fxml"));				
			Scene searchScreenScene = new Scene(searchScreenParent);
			searchScreenWindow.setScene(searchScreenScene);			
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		// Showing stage
		searchScreenWindow.show();
	}
	
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
				while(rSet.next()) {
					Book inserted = new Book(rSet.getString("Name"), rSet.getString("Author"), 
							rSet.getString("SeriesOrStandAlone"), rSet.getString("Number"),rSet.getString("PublicationDate"));
					alreadyExist.add(inserted);
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
	
	/* Insert selected books into my library (SQLite database)
	 * In the case of failed connection or failed insertion attempt - you'll be notified */
	public void insert(String name, String author, String seriesSA, String index, String date) {
		DBConnection temp = new DBConnection();
		Connection connection = temp.connect();
		
		if(connection != null) {
			PreparedStatement pStatement = null;
			String sql = "INSERT INTO books(Name, Author, SeriesOrStandAlone, Number, PublicationDate) VALUES(?,?,?,?,?)";
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
		else {
			AlertBox noDataBase = new AlertBox();
			noDataBase.displayM("/messages/NoDatabase.fxml");
		}		
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
		tableView.setItems(resultsList);
		
		addBook.setOnAction(e -> addBook(e));
		addSeries.setOnAction(e -> addSeries(e));
		goBack.setOnAction(e -> goBack(e));				
	}
}
