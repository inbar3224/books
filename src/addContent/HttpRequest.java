package addContent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import messages.AlertBox;

public class HttpRequest {
	
	private static HttpURLConnection connection;
	public static String[][] results; 
	
	public static void request(String input, int searchType) {
		BufferedReader reader;		
		StringBuffer responseContent = new StringBuffer();
		String line;
		String API_KEY = "ARcBJlatuK6FrW32Lf7Peg"; 
		String beginning = "https://www.goodreads.com/search/index.xml?key=" + API_KEY + "&q=";
		String fullNameAddition = "&search_type=books&search[field]=title";
		String finalResponse = "";
		
		SearchResults.input = input;
		String searchString = parseInput(input);
		searchString = beginning + searchString;		
		if(searchType == 1) {
			searchString += fullNameAddition;
		}
		
		try {
			URL url = new URL(searchString); 
			
			connection = (HttpURLConnection) url.openConnection(); 
			
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
			while((line = reader.readLine()) != null) { 
				responseContent.append(line); 
			}
			reader.close();	
			
			boolean results = responseContent.toString().matches(".*\\bwork\\b.*");
			if(results) {
				JSONObject object = XML.toJSONObject(responseContent.toString());
				int isAlone = object.toString().indexOf("[");
				if(isAlone == -1) {
					finalResponse = extraction(object.toString(), "work", 1);
					finalResponse = extraction(finalResponse, "}}},", 0);
					finalResponse = finalResponse.replaceFirst("\":", "[");
					finalResponse += "}}]";
				}
				else {
					finalResponse = extraction(object.toString(), "\\[", 1);
					finalResponse = extraction(finalResponse, "\\]", 0);
					finalResponse = "[" + finalResponse + "]";
				}
				parseRequest(finalResponse);
				// showResults();
			}
			else {
				AlertBox.displayM("/messages/NoResults.fxml");
			}			
		}
		catch(MalformedURLException e) { 
			e.printStackTrace();
		}
		catch(IOException e) { 
			e.printStackTrace();
		}
		
		finally {
			connection.disconnect();
		}
	}
	
	public static String parseInput(String input) {
		String[] tempString = input.split(" ");
		String newString = "";
		int i;
		
		for(i = 0; i < tempString.length - 1; i++) {
			newString += tempString[i] + "+";
		}
		newString += tempString[i];
		
		return newString;
	}
	
	public static void parseRequest(String responseBody) {
		String tempTitle = "", title = "", series = "", index = "", author = "";
		
		JSONArray listOfBooks = new JSONArray(responseBody);
		
		results = new String[listOfBooks.length()][4];
		
		for(int i = 0; i < listOfBooks.length(); i++) {
			JSONObject singleBook = listOfBooks.getJSONObject(i); // a single object
			JSONObject indexForTitle = singleBook.getJSONObject("best_book");
			JSONObject indexForAuthor = indexForTitle.getJSONObject("author");
			
			// extract information
			tempTitle = indexForTitle.getString("title");			
			int isSeries = tempTitle.indexOf("(");
			if(isSeries != -1) {
				title = extraction(tempTitle, "\\(", 0);
				title = title.replaceAll(" $","");
				series = extraction(tempTitle, "\\(", 1);
				index = extraction(series, "\\#", 1);
				series = extraction(series, "\\,", 0);
				series = extraction(series, "\\#", 0);
				series = series.replaceAll(" $","");
				index = extraction(index, "\\)", 0);
			}
			else {
				title = tempTitle;
				series = "Standalone";
				index = "0";
			}		
			author = indexForAuthor.getString("name");
			results[i][0] = title;
			results[i][1] = author;
			results[i][2] = series;
			results[i][3] = index;
		}
		printResults();
	}
	
	public static String extraction(String original, String limit, int index) {
		String[] split = original.split(limit, 2);
		return split[index].toString();
	}
	
	public static void printResults() {
		for(int i = 0; i < results.length; i++) {
			for(int j = 0; j < 4; j++) {
				if(results[i][j] != null) {
					System.out.print(results[i][j] + ", ");
				}
			}
			System.out.println();
		}		
	}
	
	public static void showResults() {
		try {
			FXMLLoader loader = new FXMLLoader(SearchResults.class.getClass().getResource("/addContent/SearchResults.fxml"));
			SearchResults gg = new SearchResults();
			loader.setController(gg);
			Parent root = (Parent) loader.load();
			
			// Parent root = (Parent) FXMLLoader.load(SearchResults.class.getClass().getResource("/addContent/SearchResults.fxml"));
	        
			// Parent root = (Parent) loader.load();
			
			// SearchResults.searchKey.setText("hi");			
	         
	        Scene scene = new Scene(root);
				
			SearchResults.tableStage = new Stage();
			SearchResults.tableStage.setScene(scene);
			SearchResults.tableStage.setTitle("DreamReading");
			SearchResults.tableStage.initModality(Modality.APPLICATION_MODAL);
			SearchResults.tableStage.show();
	    } 
		catch (Exception e) {
	         e.printStackTrace();
	    }
	}
}
