package homeScreen;

// import addContent.AddContent;
import messages.AlertBox;

public class Responder implements ResultListener {
	
	// execute the abstract method - deliver the appropriate response
	@Override
	public void response(int result) {
		/* TODO LIST:
		 * http request doesn't work
		 * we have results 
		 */
		if(result == 1) {
			System.out.println("Inbar1");
			//AlertBox.displayM("/messages/NoResults.fxml");
			
			// HomeScreen.presentHome();
		}
		else {
			System.out.println("Inbar2");
			// AddContent.searchStage.close();
			// HomeScreen.presentHome();
		}		
	}
}
