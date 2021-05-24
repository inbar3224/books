package addContent;

import java.util.ArrayList;
import java.util.List;

public class InitiateContainer {
	
	// Array of listeners
	private List<ResultListener> listeners = new ArrayList<ResultListener>();
	
	// Add a new listener to the array
	public void addListener(ResultListener current) {
		listeners.add(current);
	}
	
	// Get a response for each listener	
	public void getAResponse(int status) {
		for(ResultListener r : listeners) {
			r.response(status);
		}
	}

}
