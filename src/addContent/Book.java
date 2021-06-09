package addContent;

public class Book {
	
	private String name, author, seriesStandAlone, index, publicationDate;
	
	public Book(String firstC, String secondC, String thirdC, String fourth, String fifth) {
		name = firstC;
		author = secondC;
		seriesStandAlone = thirdC;
		index = fourth;
		publicationDate = fifth;
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
	
	public String getPublicationDate() {
		return publicationDate;
	}
	
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
