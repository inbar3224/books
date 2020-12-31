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

import messages.AlertBox;

public class HttpRequest {
	
	private static HttpURLConnection connection;
	
	public static void request(String input, int searchType) {
		BufferedReader reader;		
		StringBuffer responseContent = new StringBuffer();
		String line;
		String API_KEY = "ARcBJlatuK6FrW32Lf7Peg"; 
		String beginning = "https://www.goodreads.com/search/index.xml?key=" + API_KEY + "&q=";
		String fullNameAddition = "&search_type=books&search[field]=title";
		String finalResponse = "";
		
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
		System.out.println(newString);
		return newString;
	}
	
	public static void parseRequest(String responseBody) {
		String tempTitle = "", title = "", series = "", index = "", author = "";
		JSONArray listOfBooks = new JSONArray(responseBody); 
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
				index = extraction(index, "\\)", 0);
			}
			else {
				title = tempTitle;
				series = "Standalone";
				index = "0";
			}		
			author = indexForAuthor.getString("name");
			
			System.out.println(title + ", " + series  + ", " + index + ", " + author); // print information
		}
	}
	
	public static String extraction(String original, String limit, int index) {
		String[] split = original.split(limit, 2);
		return split[index].toString();
	}
}
