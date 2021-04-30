package homeScreen;

import java.util.ArrayList;
import java.util.List;

public class Initiater {
	
	// initiate an array of listeners
	private List<ResultListener> listeners = new ArrayList<ResultListener>();
	
	// add a new listener to the array
	public void addListener(ResultListener current) {
		listeners.add(current);
	}
	
	// get a response for each listener	
	public void getAResponse(int status) {
		for(ResultListener r : listeners) {
			r.response(status);
		}
	}
}
