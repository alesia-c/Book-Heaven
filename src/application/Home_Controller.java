package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;


public class Home_Controller {

	@FXML
    private Pane pane_usrname;   
    @FXML
    private Pane pane_shop;
    @FXML
    private Pane pane_home;
    
    @FXML
    private  Label lbl_username;
    
	@FXML
    private Button btn_shop;
    @FXML
    private Button btn_login;
    @FXML
    private Button btn_logout;
    @FXML
    private Button btn_search;
    @FXML
    private Button btn_signin;

    @FXML
    private ImageView img_cart;

    @FXML
    private TextField txt_search;
    
  
    @FXML
    void initialize() {	
    	
    	lbl_username.setText(Main.username);
     
    	styling();
    	
    	
    	//Lambda per ndryshimin e skenes 
    	
    	//objekt i klases Helper qe do ndihmoje ne ndryshimin e skenave
        Helper helper = new Helper();
        
        Alert alert = new Alert(AlertType.INFORMATION);
        
    	pane_usrname.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
    		if (Main.username.equals("Guest")) {
        		helper.changeScene("Log_In.fxml");
			}
    	});
    	
    	pane_home.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
    		helper.changeScene("Home.fxml");
    	});
    	
    	pane_shop.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
    		helper.changeScene("Shop.fxml");
    	});
    	
    	btn_logout.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {	
    		helper.Log_Out();   	
    	});
    	
        btn_login.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
        	
        if (Main.username.equals("Guest")) {
    		helper.changeScene("Log_In.fxml");
		}
        	 else {
				alert.setContentText("Already logged in.");
				alert.show();
			}
    		    	
        });
    	
        btn_shop.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
    		helper.changeScene("Shop.fxml");    	
    	});
        
        btn_signin.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
        	
        	if (Main.username.equals("Guest")) {
        		helper.changeScene("Sign_In.fxml");
        	} else {
        		alert.setContentText("Can not access this page while logged in.");
				alert.show();
			}
    		    	
    	});
        
    	img_cart.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
    		helper.goToCart();
    	});
    	
    }


	private void styling() {
		btn_login.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
	    	btn_login.setStyle("-fx-background-color: #ca867a; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 20;");
	    	});
	    	btn_login.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
	    		btn_login.setStyle("-fx-background-color: #fda898; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 20;");
	    	});
	    	
	    	
	    	btn_logout.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
	    		btn_logout.setStyle("-fx-background-color: #ca867a; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 18;");
	    	});
	    	btn_logout.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
	    		btn_logout.setStyle("-fx-background-color: #fda898; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 18;");
	    	});
	    	
	    	
	    	btn_signin.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
	    		btn_signin.setStyle("-fx-background-color: #345e61; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 20;");
	    	});
	    	btn_signin.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
	    		btn_signin.setStyle("-fx-background-color: #1d4c4f; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 20;");
	    	});
	    	
	    	
	    	btn_shop.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
	    		btn_shop.setStyle("-fx-background-color: #345e61; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 20;");
	    	});
	    	btn_shop.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
	    		btn_shop.setStyle("-fx-background-color: #1d4c4f; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 20;");
	    	});
		
	}
    
}
