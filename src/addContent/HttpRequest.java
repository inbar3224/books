package addContent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import homeScreen.HomeScreen;

public class HttpRequest {
	
	private static HttpURLConnection connection;
	public static String[][] results;
	public static String finalResponse = "";
	
	/* parse input
	 * create a connection according to a specific url
	 * call method to analyze response - do we have results or not?
	 * if we do, we call a method to separate each book and it's details to it's own place in the array
	 * we also have a listener from the HomeScreen who get's a response after we're done working
	 */
	public static void request(String input, int searchType) {
		BufferedReader reader;		
		StringBuffer responseContent = new StringBuffer();
		String line;
		String API_KEY = "ARcBJlatuK6FrW32Lf7Peg"; 
		String beginning = "https://www.goodreads.com/search/index.xml?key=" + API_KEY + "&q=";
		String fullNameAddition = "&search_type=books&search[field]=title";
				
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
				resultAnalysis(responseContent.toString());				
				parseRequest(finalResponse);
				HomeScreen.responseStatus = 2;
			}
			else {
				HomeScreen.responseStatus = 1;				
			}			
		}
		catch(IOException e) {
			// add no internet = 0, alert box for it, responsder condition
			e.printStackTrace();
		}
				
		finally {
			connection.disconnect();
			HomeScreen.initiater.getAResponse(HomeScreen.responseStatus);
		}
	}
	
	// oftentimes, the search would contain more than one word.
	// we have to parse it in a way that would fit the HTTP request
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
	
	/* do we only have one book as a result or more?
	 * one book has a different XML file than 2 or more books
	 * in order for the parseRequest method to have one set of tools regardless of the results,
	 * we design the finalRequest string according to the number of books
	 */
	public static void resultAnalysis(String response) {
		JSONObject object = XML.toJSONObject(response);
		
		int isAlone = object.toString().indexOf("[");
		// a single book
		if(isAlone == -1) {
			finalResponse = extraction(object.toString(), "work", 1);
			finalResponse = extraction(finalResponse, "}}},", 0);
			finalResponse = finalResponse.replaceFirst("\":", "[");
			finalResponse += "}}]";
		}
		// 2 - 20 books (20 is the maximum number of books we can get in a single request)
		else {
			finalResponse = extraction(object.toString(), "\\[", 1);
			finalResponse = extraction(finalResponse, "\\]", 0);
			finalResponse = "[" + finalResponse + "]";
		}
	}
	
	/* separate each book from the initial response
	 * getting specific details: name, author's name, series / stand-alone, index, release date
	 * we arrange each book and it's details in an array to be presented later in the SearchResults screen
	 */
	public static void parseRequest(String responseBody) {
		String tempTitle = "", title = "";
		String author = "";
		String series = "", index = ""; 
		String publicationDate = "";
						
		JSONArray listOfBooks = new JSONArray(responseBody);
		
		results = new String[listOfBooks.length()][5];
				
		for(int i = 0; i < listOfBooks.length(); i++) {
			JSONObject singleBook = listOfBooks.getJSONObject(i); // a single object
			
			// full publication date
			publicationDate = getDate(singleBook);		
			
			// author's name
			author = getAuthor(singleBook);
			
			// book's name
			title = getName(singleBook);
			
			// (series / stand-alone) + (index / zero)
			JSONObject indexForTitle = singleBook.getJSONObject("best_book");
			tempTitle = indexForTitle.getString("title");			
			int isSeries = tempTitle.indexOf("(");
			// series
			if(isSeries != -1) {
				series = extraction(tempTitle, "\\(", 1);
				index = extraction(series, "\\#", 1);
				series = extraction(series, "\\,", 0);
				series = extraction(series, "\\#", 0);
				series = series.replaceAll(" $","");
				index = extraction(index, "\\)", 0);
			}
			// stand-alone
			else {
				series = "Standalone";
				index = "0";
			}		
			
			results[i][0] = title;
			results[i][1] = author;
			results[i][2] = series;
			results[i][3] = index;
			results[i][4] = publicationDate;
		}
		printResults();
	}
	
	// get book's publication date
	public static String getDate(JSONObject partialDate) {
		JSONObject temp;
		int num;
		String date = "";
		
		temp = partialDate.getJSONObject("original_publication_day");
		num = temp.getInt("content");
		date += Integer.toString(num) + ".";
		temp = partialDate.getJSONObject("original_publication_month");
		num = temp.getInt("content");
		date += Integer.toString(num) + ".";
		temp = partialDate.getJSONObject("original_publication_year");
		num = temp.getInt("content");
		date += Integer.toString(num);
		return date;
	}
	
	// get author's name
	public static String getAuthor(JSONObject partialData) {
		JSONObject category = partialData.getJSONObject("best_book");
		JSONObject tempName = category.getJSONObject("author");
		String finalName = tempName.getString("name");
		return finalName;
	}
	
	// get book's name
	public static String getName(JSONObject partialData) {
		String title = "";
		
		JSONObject indexForTitle = partialData.getJSONObject("best_book");
		String tempTitle = indexForTitle.getString("title");
		
		int isSeries = tempTitle.indexOf("(");
		// name contains '('
		if(isSeries != -1) {
			title = extraction(tempTitle, "\\(", 0);
			title = title.replaceAll(" $","");
		}
		// name doesn't contains '('
		else {
			title = tempTitle;
		}
		
		return title;
	}
	
	// we have a lot of work to do on the results 
	// we split the string into 2 parts and taking the necessary part according to given index
	public static String extraction(String original, String limit, int index) {
		String[] split = original.split(limit, 2);
		return split[index].toString();
	}
	
	// only for now - once the SearchResults screen is ready we would delete this part.
	public static void printResults() {
		for(int i = 0; i < results.length; i++) {
			for(int j = 0; j < 5; j++) {
				if(results[i][j] != null) {
					System.out.print(results[i][j] + ", ");
				}
			}
			System.out.println();
		}		
	}
}
