package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;


// Klase e cila na vjen ne ndihme me disa funksione qe perdoren shpesh ne program.

public class Helper {

	void changeScene(String skena) {
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource(skena));
			Scene scene = new Scene(root);
			
			Main.mainStage.setScene(scene);
			
		} catch (IOException e) {
			e.printStackTrace();
		}	 
	}
	
	void Log_Out() {
		
		if ( Main.username != "Guest") {
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
	    	alert.setContentText("Are you sure you want to log out?");
	    	alert.showAndWait().ifPresent(response -> {
	    	     if (response == ButtonType.OK) {
	    	         Main.username = "Guest";
	    	         changeScene("Home.fxml");
	    	     }
	    	 });
		}
	}
	
	void goToCart() {
		
		if ( Main.username != "Guest") {
			changeScene("Cart.fxml");
		}
		else {
			 Alert alert = new Alert(AlertType.ERROR);
          	 alert.setContentText("Log in to continue.");
          	 alert.show();
		}
	}
	
 
}
