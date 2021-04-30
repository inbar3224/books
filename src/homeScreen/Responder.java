package homeScreen;

import addContent.AddContent;
import messages.AlertBox;

public class Responder implements ResultListener {
	
	// execute the abstract method - deliver the appropriate response
	@Override
	public void response(int result) {
		if(result == 1) {
			AlertBox.displayM("/messages/NoResults.fxml");
			
			// HomeScreen.presentHome();
		}
		else {
			System.out.println("Inbar");
			AddContent.searchStage.close();
			HomeScreen.presentHome();
		}		
	}
}
