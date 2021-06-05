package addContent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

public class HttpRequest {
	
	private HttpURLConnection connection;
	private String finalResponse = "";
	private SearchOutcomeListener listener;
	public static String[][] results;
	
	public void setSearchOutcomeListener(SearchOutcomeListener listener) {
		this.listener = listener;
	}
	
	/* Parse input
	 * Create a connection according to a specific url
	 * Call method to convert response to JSON
	 * We check if we have even have results or not
	 * If we do, we call a method to separate each book and it's details to it's own place in the array
	 * We also have a listener from the SearchScreen who gets a request status after we're done working
	 */
	public void request(String input, int searchType) {
		int requestStatus = 0;
		BufferedReader reader;		
		StringBuffer responseContent = new StringBuffer();
		String line;
		String API_KEY = "ARcBJlatuK6FrW32Lf7Peg"; 
		String beginning = "https://www.goodreads.com/search/index.xml?key=" + API_KEY + "&q=";
		String fullNameAddition = "&search_type=books&search[field]=title";
		
		// Creating customized input for the HTTP request
		String searchString = parseInput(input);
		searchString = beginning + searchString;		
		if(searchType == 1) {
			searchString += fullNameAddition;
		}
		
		// HTTP request		
		try {
			URL url = new URL(searchString); 
			
			connection = (HttpURLConnection) url.openConnection(); 
			
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
			while((line = reader.readLine()) != null) { 
				responseContent.append(line); 
			}
			reader.close();	
			
			// Check if we have results
			boolean results = responseContent.toString().matches(".*\\bwork\\b.*");
			// We have at least one result
			if(results) {
				xmlToJson(responseContent.toString());				
				resultsAnalysis(finalResponse);
				requestStatus = 2;
			}
			// No results - either we have spelling errors or the book just doesn't exist
			else {
				requestStatus = 1;				
			}			
		}
		catch(IOException e) {
			// No Internet
			requestStatus = 0;
			// e.printStackTrace();
		}
		
		// We have to close the request
		finally {
			connection.disconnect();			
		}
		
		if(listener != null) {
			listener.onEvent(requestStatus, results);
		}
		
		// return requestStatus;
	}
	
	/* Oftentimes, the search would contain more than one word.
	 * We have to parse it in a way that would fit the HTTP request
	 */
	public String parseInput(String input) {
		String[] tempString = input.split(" ");
		String newString = "";
		int i;
		
		for(i = 0; i < tempString.length - 1; i++) {
			newString += tempString[i] + "+";
		}
		newString += tempString[i];
		
		return newString;
	}
	
	/* Do we only have one book as a result or more?
	 * One book has a different XML file than 2 or more books
	 * In order for the resultsAnalysis method to have one set of tools regardless of the amount,
	 * We design the finalRequest string according to the number of books
	 */
	public void xmlToJson(String response) {
		JSONObject object = XML.toJSONObject(response);
		
		int isAlone = object.toString().indexOf("[");
		// A single book
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
	
	/* Separate each book from the initial response
	 * Getting specific details: name, author's name, series / stand-alone, index, release date
	 * We arrange each book and it's details in an array to be presented later in the results screen
	 */
	public void resultsAnalysis(String responseBody) {
		String title = "";
		String author = "";
		String series = "", index = ""; 
		String publicationDate = "";
						
		JSONArray listOfBooks = new JSONArray(responseBody);
		
		results = new String[listOfBooks.length()][5];
				
		for(int i = 0; i < listOfBooks.length(); i++) {
			JSONObject singleBook = listOfBooks.getJSONObject(i); // a single object
			JSONObject bestBook = singleBook.getJSONObject("best_book");
			
			// Book's name
			title = getName(bestBook);
			// Author's name
			author = getAuthor(bestBook);
			// Series / Stand-alone
			series = getSeries(bestBook);
			// Index / zero
			if(series.equals("Standalone")) {
				index = "0";
			}
			else {
				index = getIndex(bestBook);
			}			
			// Full publication date
			publicationDate = getDate(singleBook);		
					
			results[i][0] = title;
			results[i][1] = author;
			results[i][2] = series;
			results[i][3] = index;
			results[i][4] = publicationDate;
		}
		// printResults();
	}
	
	// Get book's name
	public String getName(JSONObject partialData) {
		String title = "";
	
		String tempTitle = partialData.getString("title");
		
		int isSeries = tempTitle.indexOf("(");
		// Book is part of a series - contains '('
		if(isSeries != -1) {
			title = extraction(tempTitle, "\\(", 0);
			char last = title.charAt(title.length() - 1);
			if(last == ' ') {
				title = title.replaceAll(" $","");
			}
		}
		// Book isn't a part of a series
		else {
			title = tempTitle;
		}
		
		return title;
	}
	
	// Get author's name
	public String getAuthor(JSONObject partialData) {
		JSONObject tempAuthor = partialData.getJSONObject("author");
		String author = tempAuthor.getString("name");
		return author;
	}
	
	// Get series / stand-alone
	public String getSeries(JSONObject partialData) {
		String series = "";

		String tempSeries = partialData.getString("title");
		
		int isSeries = tempSeries.indexOf("(");		
		// Series
		if(isSeries != -1) {
			series = extraction(tempSeries, "\\(", 1);
			int hasChar = series.indexOf(",");
			if(hasChar != -1) {
				series = extraction(series, "\\,", 0);
			}
			hasChar = series.indexOf("#");
			if(hasChar != -1) {
				series = extraction(series, "\\#", 0);
			}
			char last = series.charAt(series.length() - 1);
			if(last == ' ') {
				series = series.replaceAll(" $","");
			}
			if(series.equals("Untitled")) {
				series = "Standalone";
			}
		}
		// Stand-alone
		else {
			series = "Standalone";
		}
		
		return series;
	}
	
	// Get index / zero
	public String getIndex(JSONObject partialData) {
		String index = "";
		
		String tempIndex = partialData.getString("title");
		int hasChar = tempIndex.indexOf("#");
		if(hasChar != -1) {
			index = extraction(tempIndex, "\\#", 1);
			index = extraction(index, "\\)", 0);
		}
		else {
			index = "0";
		}		
		return index;		
	}	
	
	// Get book's publication date
	public String getDate(JSONObject partialDate) {
		JSONObject temp;
		int num;
		String date = "";
		boolean dateExist;
		
		// Do we have a day?
		temp = partialDate.getJSONObject("original_publication_day");
		dateExist = temp.has("content");
		if(dateExist) {
			num = temp.getInt("content");
			date += Integer.toString(num) + ".";
		}
		else {
			date = "0.0.0";
			return date;
		}
		// Do we have a month?
		temp = partialDate.getJSONObject("original_publication_month");
		dateExist = temp.has("content");
		if(dateExist) {
			num = temp.getInt("content");
			date += Integer.toString(num) + ".";
		}
		else {
			date = "0.0.0";
			return date;
		}
		// Do we have a year?
		temp = partialDate.getJSONObject("original_publication_year");
		dateExist = temp.has("content");
		if(dateExist) {
			num = temp.getInt("content");
			date += Integer.toString(num);
		}
		else {
			date = "0.0.0";
			return date;
		}
				
		return date;
	}
		
	// We split the string into 2 parts and taking the necessary part according to given index
	public String extraction(String original, String limit, int index) {
		String[] split = original.split(limit, 2);
		return split[index].toString();
	}
	
	/*// Only for now
	public void printResults() {
		for(int i = 0; i < results.length; i++) {
			for(int j = 0; j < 5; j++) {
				if(results[i][j] != null) {
					System.out.print(results[i][j] + ", ");
				}
			}
			System.out.println();
		}		
	}*/
}
