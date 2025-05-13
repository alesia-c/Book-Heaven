package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	
	// Disa variabla statike qe ndihmojne ne reduktimin e kodit.  
	
	public static Stage mainStage;
	
    public static String username = "Guest";
    
    public static String defaultImage = "file:/Users/macbook/eclipse-workspace/BOOKSTORE/src/images/default cover .jpg";
	
	@Override
	public void start(Stage primaryStage) {
		
		mainStage = primaryStage;
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
			Scene scene = new Scene(root);
	
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
