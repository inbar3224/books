package addContent;

import javafx.collections.ObservableList;

public interface SearchOutcomeListener {
	/* Abstract method */
	public void onEvent(int status, ObservableList<Book> resultsArray);
}
