package addContent;

public class Book {
	
	private String name, author, seriesStandAlone, index;
	
	public Book(String firstC, String secondC, String thirdC, String fourth) {
		name = firstC;
		author = secondC;
		seriesStandAlone = thirdC;
		index = fourth;
	}

	public String getAuthor() {
		return author;
	}

	public String getName() {
		return name;
	}

	public String getSeriesStandAlone() {
		return seriesStandAlone;
	}

	public String getIndex() {
		return index;
	}	
}
