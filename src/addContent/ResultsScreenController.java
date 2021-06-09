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
		Book chosen = tableView.getSelectionModel().getSelectedItem();
		if(chosen != null) {
			readAll();
			boolean exist = alreadyExist.contains(chosen);
			if(exist == false) {
				insert(chosen.getName(), chosen.getAuthor(), chosen.getSeriesStandAlone(), chosen.getIndex(), chosen.getPublicationDate());
				AlertBox inserted = new AlertBox();
				inserted.displayM("/messages/InsertedBook.fxml");
			}
			else {
				AlertBox inserted = new AlertBox();
				inserted.displayM("/messages/BookExists.fxml");
			} 
		}
		else {
			AlertBox noBookChosen = new AlertBox();
			noBookChosen.displayM("/messages/NoBookChosen.fxml");
		}			
	}
	
	@FXML 
	private void addSeries(ActionEvent event) {
				
	}
	
	@FXML
	private void goBack(ActionEvent event) {
		try {
			Stage searchScreenWindow = DreamReading.getPrimaryStage();
			// Settings for stage
			Parent searchScreenParent = FXMLLoader.load(getClass().getResource("/addContent/SearchScreen.fxml"));				
			Scene searchScreenScene = new Scene(searchScreenParent);
			searchScreenWindow.setScene(searchScreenScene);
			// Showing stage
			searchScreenWindow.show();
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
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
			ResultSet rSet = null;
			try {
				String sql = "SELECT * FROM books";
				pStatement = connection.prepareStatement(sql);
				rSet = pStatement.executeQuery();
				while(rSet.next()) {
					Book inserted = new Book(rSet.getString("Name"), rSet.getString("Author"), 
							rSet.getString("SeriesOrStandAlone"), rSet.getString("Number"),rSet.getString("PublicationDate"));
					alreadyExist.add(inserted);
				}			
			}
			catch (SQLException e) {
				AlertBox readingFailed = new AlertBox();
				readingFailed.displayM("/messages/ReadingFailed.fxml");
			}
			finally {
				try {
					rSet.close();
					pStatement.close();
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
			try {
				String sql = "INSERT INTO books(Name, Author, SeriesOrStandAlone, Number, PublicationDate) VALUES(?,?,?,?,?)";
				pStatement = connection.prepareStatement(sql);
				pStatement.setString(1, name);
				pStatement.setString(2, author);
				pStatement.setString(3, seriesSA);
				pStatement.setString(4, index);
				pStatement.setString(5, date);
				pStatement.execute();
			}
			catch (SQLException e) {
				AlertBox insertionFailed = new AlertBox();
				insertionFailed.displayM("/messages/InsertionFailed.fxml");
			}
			finally {
				try {
					pStatement.close();
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
