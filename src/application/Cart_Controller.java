package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Cart_Controller {
	
	@FXML
	private Pane pane_usrname;
	@FXML
    private Pane pane_home;
	@FXML
    private Pane pane_shop;
	
	
    @FXML
    private Button btn_checkOut;
    @FXML
    private Button btn_clear;
    @FXML
    private Button btn_logout;
    @FXML
    private Button btn_remove;
    @FXML
    private Button btn_update;
  
    
    @FXML
    private TableView<Cart_Item> table_cartProducts;
    @FXML
    private TableColumn<Cart_Item, String> col_title;
    @FXML
    private TableColumn<Cart_Item, Integer> col_quantity;
    @FXML
    private TableColumn<Cart_Item, Double> col_price;

    
    @FXML
    private Label lbl_itemsTotal;
    @FXML
    private Label lbl_totalPrice;
    @FXML
    private Label lbl_username;

    
    @FXML
    private ImageView img_cart;
    
    @FXML
    private Hyperlink link_shop;  

    @FXML
    private Spinner<Integer> spinner_newQuantity;

    @FXML
    private TextField txt_search;
    
    
    public void initialize() {
    	
    	lbl_username.setText(Main.username);
    	
    	spinner_newQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
		
    	populate();
    	
    	calcTotals();
    	
    	Helper helper = new Helper();
    	
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
    	
    	img_cart.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
    		helper.goToCart();
    	});
    	
    	link_shop.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
    		helper.changeScene("Shop.fxml");
    	});
	
    	btn_logout.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {	
    		helper.Log_Out();   	
    	});
    	
    	styling();
    	
	}
    
    
    
    public void populate() {


    	ObservableList<Cart_Item> cartList = FXCollections.observableArrayList();

    	try {

    		Connection connection = DBConnection.getConnection();
    		PreparedStatement statement = connection.prepareStatement("SELECT * FROM cartitems WHERE user=?");
    		statement.setString(1, lbl_username.getText());
    		ResultSet resultSet = statement.executeQuery();

    		while (resultSet.next()) {

    			cartList.add(new Cart_Item(resultSet.getString("item"), 
    					resultSet.getInt("quantity"), 			                            	 
    					resultSet.getDouble("price")));			                           

    		}

    		col_title.setCellValueFactory(new PropertyValueFactory<Cart_Item, String>("bookTitle"));
    		col_quantity.setCellValueFactory(new PropertyValueFactory<Cart_Item, Integer>("quantity"));
    		col_price.setCellValueFactory(new PropertyValueFactory<Cart_Item, Double>("price"));

    		table_cartProducts.setItems(cartList);

    	} catch (SQLException e) {
    		e.printStackTrace();
    	}

    }

    
    private void calcTotals() {


    	ObservableList<Cart_Item> cartList = FXCollections.observableArrayList();

    	try {

    		Connection connection = DBConnection.getConnection();
    		PreparedStatement statement = connection.prepareStatement("SELECT * FROM cartitems WHERE user=?");
    		statement.setString(1, lbl_username.getText());
    		ResultSet resultSet = statement.executeQuery();

    		while (resultSet.next()) {

    			cartList.add(new Cart_Item(resultSet.getString("item"), 
    					resultSet.getInt("quantity"), 			                            	 
    					resultSet.getDouble("price")));			                           

    		}

    	} catch (SQLException e) {
    		e.printStackTrace();
    	}

    	int items = 0;
    	double total = 0;

    	for(int i = 0 ; i < cartList.size() ; i++) {

    		items += cartList.get(i).getQuantity();

    		total += cartList.get(i).getPrice();
    	}

    	lbl_itemsTotal.setText(String.valueOf(items));

    	lbl_totalPrice.setText(String.valueOf(total));

    }

    private void clear() {
    	
    	try {

    		Connection connection = DBConnection.getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM cartitems WHERE user = ?");

    		preparedStatement.setString(1, lbl_username.getText());
    		preparedStatement.execute();

    		populate();
    		calcTotals();

    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
	}
    
    
    @FXML
    void clearCart(ActionEvent event) {
    	
    	clear();

    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setContentText("Cart cleared successfully.");
    	alert.show();
    }
    

    @FXML
    void remove(ActionEvent event) {

    	Cart_Item item = table_cartProducts.getSelectionModel().getSelectedItem();

    	try {

    		Connection connection = DBConnection.getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM cartitems WHERE  user=? AND item=?");

    		preparedStatement.setString(1, lbl_username.getText());
    		preparedStatement.setString(2, item.getBookTitle());

    		preparedStatement.execute();

    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    	populate();
    	calcTotals();
    }


    @FXML
    void updateQuantity(ActionEvent event) {

    	Cart_Item itemObj = table_cartProducts.getSelectionModel().getSelectedItem();

    	try {

    		Connection connection = DBConnection.getConnection();

    		PreparedStatement pS = connection.prepareStatement("SELECT copies FROM books WHERE name=?");
    		pS.setString(1, itemObj.getBookTitle());
    		ResultSet rS = pS.executeQuery();

    		rS.next();

    		if (rS.getInt("copies") >= spinner_newQuantity.getValue()) {

    			PreparedStatement pStatement = connection.prepareStatement("UPDATE cartitems SET quantity=? WHERE user=? AND item=?");
    			pStatement.setInt(1, spinner_newQuantity.getValue());
    			pStatement.setString(2, lbl_username.getText());
    			pStatement.setString(3, itemObj.getBookTitle());

    			pStatement.execute();
    			populate();
    			calcTotals();

    		}  	
    		else {
    			Alert alert = new Alert(AlertType.ERROR);
    			alert.setContentText("Low Stock!");
    			alert.show();
    		}

    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    }

    
    @FXML
    void checkOut(ActionEvent event) {

    	try {
    		Connection connection = DBConnection.getConnection();
    		PreparedStatement pStatement = connection.prepareStatement("SELECT  item, quantity FROM cartitems WHERE user=?");
    		pStatement.setString(1, lbl_username.getText());

    		ResultSet rSet = pStatement.executeQuery();

    		while (rSet.next()) {

    			PreparedStatement pS = connection.prepareStatement("SELECT copies FROM books WHERE name=?");
    			pS.setString(1, rSet.getString("item"));
    			ResultSet rS = pS.executeQuery();

    			if (rS.next()) {
    				int newCopiesNr = rS.getInt("copies") - rSet.getInt("quantity");

    				PreparedStatement upStatement = connection.prepareStatement("UPDATE books SET copies=? WHERE name=?");
    				upStatement.setInt(1, newCopiesNr);
    				upStatement.setString(2, rSet.getString("item"));
    				upStatement.execute();

    			}

    		}

    	} catch (SQLException e) {
    		e.printStackTrace();
    	}

    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setContentText("Thank you for purchasing from us!\nYou will be contacted by your local post office within 48 hours to pick up your package.");
    	alert.show();

    	clear();

    }
    
    
    private void styling() {
    	
    	btn_checkOut.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		btn_checkOut.setStyle("-fx-background-color: #ca867a; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-font-size: 16;");
    	});
    	btn_checkOut.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		btn_checkOut.setStyle("-fx-background-color: #fda898; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-font-size: 16;");
    	});
    	
    	btn_update.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		btn_update.setStyle("-fx-background-color: #345e61; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-font-size: 16;");
    	});
    	btn_update.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		btn_update.setStyle("-fx-background-color: #1d4c4f; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-font-size: 16;");
    	});
    	
    	btn_remove.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		btn_remove.setStyle("-fx-background-color: #345e61; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-font-size: 16;");
    	});
    	btn_remove.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		btn_remove.setStyle("-fx-background-color: #1d4c4f; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-font-size: 16;");
    	});
    	
    	btn_clear.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		btn_clear.setStyle("-fx-background-color: #345e61; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-font-size: 16;");
    	});
    	btn_clear.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		btn_clear.setStyle("-fx-background-color: #1d4c4f; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-font-size: 16;");
    	});

    	btn_logout.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		btn_logout.setStyle("-fx-background-color: #ca867a; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 18;");
    	});
    	btn_logout.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		btn_logout.setStyle("-fx-background-color: #fda898; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 18;");
    	});
    }
}
