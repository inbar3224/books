package addContent;

import javafx.collections.ObservableList;

public interface SearchOutcomeListener {	
	public void onEvent(int status, ObservableList<Book> resultsArray);
}
