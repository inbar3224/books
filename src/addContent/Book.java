package addContent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Book {
	
	private String name, author, seriesStandAlone, index, publicationDate;
	
	public Book(String firstC, String secondC, String thirdC, String fourth, String fifth) {
		name = firstC;
		author = secondC;
		seriesStandAlone = thirdC;
		index = fourth;
		publicationDate = fifth;
	}
	
	// When we add the books to the TableView, we need to get the data somehow...
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
	
	// Specific format of publication date for the "Published" column	
	public String getPublicationDate() {
		if(publicationDate.compareTo("") == 0) {
			return "No";
		}
		
		Date mine = new Date();
		DateFormat dFormat = new SimpleDateFormat("yyyy.MM.dd");
		String current = dFormat.format(new Date());
		Date finalCurrent = new Date();
				
		try {
			mine = dFormat.parse(publicationDate);
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			finalCurrent = dFormat.parse(current);
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(mine.after(finalCurrent)) {
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
		
		/*if (index == null) {
			if (other.index != null) {
				return false;
			}				
		} else if (!index.equals(other.index)) {
			return false;
		}			
		
		if (publicationDate == null) {
			if (other.publicationDate != null) {
				return false;
			}				
		} else if (!publicationDate.equals(other.publicationDate)) {
			return false;
		}			
		if (seriesStandAlone == null) {
			if (other.seriesStandAlone != null) {
				return false;
			}				
		} else if (!seriesStandAlone.equals(other.seriesStandAlone)) {
			return false;
		}*/
			
		return true;
	}
}
