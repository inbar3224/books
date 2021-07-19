package addContent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Book {
	
	private String name, author, seriesStandAlone, index, publicationDate;
	
	/* Constructor */
	public Book(String firstC, String secondC, String thirdC, String fourth, String fifth) {
		name = firstC;
		author = secondC;
		seriesStandAlone = thirdC;
		index = fourth;
		publicationDate = fifth;
	}
	
	/* When we add the books to the TableView, we need to get the data somehow... */
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
	
	public String myPublicationDate() {
		return publicationDate;
	}
	
	/* Specific format of publication date for the "Published" column */	
	public String getPublicationDate() {
		if(publicationDate.compareTo("") == 0) {
			return "No";
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String tempMyDate = publicationDate.replace(".", "-");
		LocalDate today = LocalDate.parse(LocalDate.now().toString(), formatter);
		LocalDate myDate = LocalDate.parse(tempMyDate,formatter);
		
		if(myDate.isAfter(today)) {
			return "No";
		}
		else {
			return publicationDate;
		}		
	}
	
	/* In order to see if we're trying to add items that already exist in our library,
	 * We have to compare each and every new item with what we already have */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}			
		if (obj == null) {
			return false;
		}			
		if (getClass() != obj.getClass()) {
			return false;
		}
			
		Book other = (Book) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}				
		} 
		else if (!name.equals(other.name)) {
			return false;
		}			
		if (author == null) {
			if (other.author != null) {
				return false;
			}				
		} 
		else if (!author.equals(other.author)) {
			return false;
		}			
			
		return true;
	}
}
