package application;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class ProductPage_Controller {

	
	@FXML
    private Pane pane_usrname;
    @FXML
    private Pane pane_home;
    @FXML
    private Pane pane_shop;
    
    
    @FXML
    private Button btn_logout;   
    @FXML
    private Button btn_back;


    @FXML
    private ImageView img_cart;
    @FXML
    private ImageView img_Cover;

    
    @FXML
    private Label lbl_username;
    @FXML
    private Label lbl_bookTitle;
    @FXML
    private Label lbl_authorName;
    @FXML
    private Label lbl_bookStatus;
    @FXML
    private Label lbl_bookPages;   
    @FXML
    private Label lbl_yrRelease; 
    @FXML
    private Label lbl_Price;
    @FXML
    private Label lbl_copiesStock;

   
    @FXML
    private Spinner<Integer> spinner_copies;
    
    Helper helper = new  Helper();
    
    public void initialize() {
    	
		lbl_username.setText(Main.username);
		
		spinner_copies.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
		

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
    	btn_back.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
    		helper.changeScene("Shop.fxml");
    	});
    	
    	img_cart.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
    		helper.goToCart();
    	});	
    	
    	//Lambda qe lidhen me stilimin
    	
    	styling();
	}
   
    
    
    // Inicializon skenen me atributet/te dhenat e librit mbi te cilin klikohet. 
    public void SetData(String bookInfo) {
    	
    	lbl_bookTitle.setText(bookInfo);
    	
    	try {
			
    		Connection connection = DBConnection.getConnection();
    		PreparedStatement statement = connection.prepareStatement("SELECT * FROM books WHERE name=?");
    		statement.setString(1, lbl_bookTitle.getText());
    		ResultSet rSet = statement.executeQuery();
    		
    		if(rSet.next()) {
    		    
    			lbl_authorName.setText(rSet.getString("author"));
    			lbl_bookStatus.setText(rSet.getString("status"));
    			lbl_bookPages.setText(String.valueOf(rSet.getInt("pages")));
    			lbl_yrRelease.setText(rSet.getString("year"));
    			lbl_Price.setText(String.valueOf(rSet.getDouble("price")));
    			lbl_copiesStock.setText(String.valueOf(rSet.getInt("copies")));
    			Image image = new Image(rSet.getString("imgPath"),340, 420, false, true);
    			img_Cover.setImage(image);
    		}
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    @FXML
    private Button btn_addToCart;
    @FXML
    void addToCart(ActionEvent event) {

    	if (lbl_username.getText() != "Guest") {
			
    		try {		
      			
           	    Connection connection = DBConnection.getConnection();
           	 
           	    PreparedStatement pS = connection.prepareStatement("SELECT copies FROM books WHERE name=?");
 			    pS.setString(1, lbl_bookTitle.getText());
 			    ResultSet rS = pS.executeQuery();
 			
 			    rS.next();
 			    
 		    	// Nese numri i kopjeve stok te librit qe perdoruesi do te shtoje ne shporte eshte me i vogel se ai qe kerkon, atehere blloku i meposhtem nuk ekzekutohet.
 			    if (rS.getInt("copies") >= spinner_copies.getValue()) { 		    	                                                    
 				   
 				    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO cartitems (user, item, quantity, price )" +
 			        "values (?, ?, ?, ?)");
 			           	 
 	         	    preparedStatement.setString(1, lbl_username.getText());
 			        preparedStatement.setString(2, lbl_bookTitle.getText());
 			        preparedStatement.setInt(3, spinner_copies.getValue());
 			        preparedStatement.setDouble(4, Double.parseDouble(lbl_Price.getText()));
 			           	 
 			        preparedStatement.execute();	
 			       

 		       	    Alert alert = new Alert(AlertType.INFORMATION);
 		       	    alert.setContentText("Item added to cart.");
 		       	    alert.show();
 				   
 			    }
 			    else {
 				    Alert alert = new Alert(AlertType.ERROR);
 			    	alert.setContentText("Low Stock!");
 			    	alert.show();
 				}
           	 
   		    } catch (SQLException e) {
   			    e.printStackTrace();
   		    }
       	 
		}
    	else {
    		Alert alert = new Alert(AlertType.ERROR);
          	alert.setContentText("Log in to purchase items.");
          	alert.show();
		}
   	 
    }

    
    @FXML
    private Button btn_search;
    @FXML
    private TextField txt_search;
    @FXML
    void search_book(ActionEvent event) {

    }
    
    private void styling() {
		
    	btn_logout.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		btn_logout.setStyle("-fx-background-color: #ca867a; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 18;");
    	});
    	btn_logout.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		btn_logout.setStyle("-fx-background-color: #fda898; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 18;");
    	});
    	btn_addToCart.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		btn_addToCart.setStyle("-fx-background-color: #ca867a; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-font-size: 16;");
    	});
    	btn_addToCart.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		btn_addToCart.setStyle("-fx-background-color: #fda898; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-font-size: 16;");
    	});	
    	btn_back.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		btn_back.setStyle("-fx-background-color: #345e61; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 14;");
    	});  	
    	btn_back.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		btn_back.setStyle("-fx-background-color: #1d4c4f; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 14;");
    	});
    	
	}

}
