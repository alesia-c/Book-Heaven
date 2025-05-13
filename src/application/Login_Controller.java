package application;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Login_Controller {


    @FXML
    private Button btn_goBack;

    @FXML
    private Button btn_login;

    @FXML
    private Hyperlink link_signin;

    @FXML
    private PasswordField psw_password;

    @FXML
    private TextField txt_username;
    
    Helper helper = new Helper();
    
    
    @FXML
    void initialize() {
    	
    	btn_login.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		btn_login.setStyle("-fx-background-color: #345e61; -fx-background-radius: 5; -fx-font-family: Helvetica; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
    	});
    	btn_login.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		btn_login.setStyle("-fx-background-color: #1d4c4f; -fx-background-radius: 5; -fx-font-family: Helvetica; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
    	});
    	
    	
    }
    
    @FXML
    void goBack(ActionEvent event) {
    	
    	helper.changeScene("Home.fxml");

    }

    @FXML
    void gotoSignin(ActionEvent event) {

    	helper.changeScene("Sign_In.fxml");
    	
    }

    
    //Funksioni qe kryen verifikimet e nevojshme per logim. Te dergon ne interface-in perkates nese te dhenat jane te sakta, perndryshe jep mesazh gabimi.
    @FXML
    void log_in(ActionEvent event) {
		
    	String user = txt_username.getText();
    	String psw = psw_password.getText();
    	
    	if (user.equals("Admin") && psw.equals("admin12345")) {
    		Main.username = "ADMIN";
    		helper.changeScene("Admin_Interface.fxml");
    	}   
    	else if (user != null && user != "Admin" && psw != null && psw != "Admin12345") {
    		
    		Connection connection = null;
    		Statement statement = null;
    		ResultSet resultSet = null;
    		
    		try {
				
    			connection = DBConnection.getConnection();
    			statement = connection.createStatement();
    			resultSet = statement.executeQuery("SELECT username, password FROM users");
    			
    			while (resultSet.next()) {
					
    				if (resultSet.getString("username").equals(user) && resultSet.getString("password").equals(psw)) {
    					Main.username = user;
    					Alert alert = new Alert(AlertType.CONFIRMATION);
    					alert.setTitle("Success!");
    					alert.setContentText("Log in was successful.\nWelcome Back!");
    					alert.show();
    					helper.changeScene("Home.fxml");
    					return;
    				}
					
				}
    			
    			Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setContentText("The provided credencials are incorrect.");
				alert.show();
    					
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    }
    
}
