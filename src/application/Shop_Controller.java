package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class Shop_Controller {

	
	@FXML
	private Pane pane_shop;
    @FXML
	private Pane pane_usrname;    
	@FXML
	private Pane pane_home;
	
	@FXML
	private Label lbl_username;
	    
    @FXML
    private Button btn_logout;

    @FXML
    private ImageView img;
    @FXML
    private ImageView img_cart;

    @FXML
    private GridPane shop_gridpane;

    @FXML
    private ScrollPane shop_scrollpane;

    @FXML
    private TextField txt_search;
    
    
    Helper helper = new Helper();
    
    public void initialize() {
		
    	lbl_username.setText(Main.username);
    	
    	displayBookCard();
    		
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
	}
    

    private ObservableList<Book> shopItemList = FXCollections.observableArrayList();

    public ObservableList<Book> getBookList(){

    	ObservableList<Book> bookList = FXCollections.observableArrayList();

    	try {

    		Connection connection = DBConnection.getConnection();
    		Statement statement = connection.createStatement();
    		ResultSet resultSet = statement.executeQuery("SELECT * FROM books");

    		Book book;

    		while (resultSet.next()) {

    			book = new Book(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("author"), resultSet.getString("imgPath"));
    			bookList.add(book);
    		}

    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    	return bookList;
    }
    
    
    public void displayBookCard() {
		
    	shopItemList.clear();
    	shopItemList.addAll(getBookList());
    	
    	int row = 0;
    	int col = 0;
    	
    	shop_gridpane.getRowConstraints().clear();
    	shop_gridpane.getColumnConstraints().clear();
    	
    	for (int i = 0 ; i < shopItemList.size() ; i++ ) {
			
    		try {
				
    			FXMLLoader loader = new FXMLLoader();
    			loader.setLocation(getClass().getResource("bookCard.fxml"));
        		VBox vBox = loader.load();
        		BookCard_Controller bookCtrl = loader.getController();
        		bookCtrl.SetData(shopItemList.get(i));
        		
        		if (col == 4) {
					col = 0;
					row++;
				}
        		
        		shop_gridpane.add(vBox, col++, row);
        		
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
		}
	}

}