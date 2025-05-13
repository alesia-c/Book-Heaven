package application;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Admin_Controller {

    @FXML
    private TableView<Book> Inventory;
    @FXML
    private TableColumn<Book, Integer> col_id;    
    @FXML
    private TableColumn<Book, String> col_title;    
    @FXML
    private TableColumn<Book, String> col_author;  
    @FXML
    private TableColumn<Book, String> col_year;
    @FXML
    private TableColumn<Book, Integer> cl_pages;    
    @FXML
    private TableColumn<Book, Integer> col_copies;  
    @FXML
    private TableColumn<Book, Double> col_price;
    @FXML
    private TableColumn<Book, String> col_status;
    
    @FXML
    private TextField txt_title;
    @FXML
    private TextField txt_author;
    @FXML
    private TextField txt_year;
    @FXML
    private TextField txt_pages;
    @FXML
    private TextField txt_copies;
    @FXML
    private TextField txt_price;
    @FXML
    private TextField txt_search;
    @FXML
    private ComboBox<String> cmb_status;

    @FXML
    private ImageView img_cover;

    @FXML
    private Label lbl_username;
    @FXML
    private Label lbl_id;

    @FXML
    private Button btn_logout;
    @FXML
    private Button btn_clear;
    @FXML
    private Button btn_search;
  
    
    private String imageUrl = Main.defaultImage;  // Variabel qe do ruaje path-in/url e imazhit te kopertines se librit.
    
    public void initialize() {
    	
    	//dy kategorite e librave qe shiten
        ObservableList<String> categories = FXCollections.observableArrayList("Classics", "Modern");
        
    	cmb_status.setItems(categories);
    	
    	populate();
    		
    	search_book();
    	
    	btn_logout.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
    		Helper helper = new Helper();
    		helper.Log_Out();
    	});
    	
    	btn_clear.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
    		clear();
    	});
    	
    	styling();
    }
    
    
    //Funksion qe kontrollon nese te dhenat qe do shtoje admini ne daabase jane te vlefshme.
    public  boolean checkFields() {
		
    	if (txt_title.getText().trim().length() <= 0) return false;
    	
    	if (txt_author.getText().trim().length() <= 0 || txt_author.getText().matches(".*\\d.*")) return false;
    	
    	if (txt_year.getText().trim().matches(".*[a-zA-Z].*") || txt_year.getText().trim().length() < 4) return false;
    	
    	if (txt_pages.getText().trim().matches(".*[a-zA-Z].*") || txt_pages.getText().trim().length() <= 0) return false;
    	
    	if (txt_copies.getText().trim().matches(".*[a-zA-Z].*") || txt_copies.getText().trim().length() <= 0) return false;
    	
    	if (txt_price.getText().trim().matches(".*[a-zA-Z].*") || txt_copies.getText().trim().length() <= 0) return false;
    	
    	if (cmb_status.getValue().equals(null) || cmb_status.getValue().equals("")) return false;
    	
    
    	//Funksioni rikthen true nese te gjitha vlerat e futura nga admini jane formatuar sakte.
    	return true;
	}
   
    
    
    // Mbush kolonat e tabeles me te dhenat e ruajtura ne MySQL.
    public void populate() {
    	
    	ObservableList<Book> inventoryItems = FXCollections.observableArrayList();
    	
    	try {
    		
    		Connection connection = DBConnection.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from books");
			
			while (resultSet.next()) {
				
				inventoryItems.add(new Book(resultSet.getInt("id"), 
						                    resultSet.getString("name"), 
						                    resultSet.getString("author"), 
						                    resultSet.getString("year"),
				                            resultSet.getInt("pages"), 
			                            	resultSet.getInt("copies"), 
			                            	resultSet.getDouble("price"), 
			                            	resultSet.getString("status")));
						
			}
			

			col_id.setCellValueFactory(new PropertyValueFactory<Book, Integer>("id"));
	    	col_title.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
	    	col_author.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
	    	col_year.setCellValueFactory(new PropertyValueFactory<Book, String>("year"));
	    	cl_pages.setCellValueFactory(new PropertyValueFactory<Book, Integer>("pages"));
	    	col_copies.setCellValueFactory(new PropertyValueFactory<Book, Integer>("copies"));
	    	col_price.setCellValueFactory(new PropertyValueFactory<Book, Double>("price"));
	    	col_status.setCellValueFactory(new PropertyValueFactory<Book, String>("status"));
			
			Inventory.setItems(inventoryItems);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    
    public void clear() {

    	lbl_id.setText("");
    	txt_title.setText(null);
    	txt_author.setText(null);
    	txt_year.setText(null);
    	txt_pages.setText(null);
    	txt_copies.setText(null);
    	txt_price.setText(null);
    	cmb_status.setValue("Status");
    	
    	Image image = new Image(Main.defaultImage, 160, 200, false, true);
    	img_cover.setImage(image);
    	
    	
	}
    
    
    // Funksioni qe ben kerkimin dhe filtrimin sipas karaktereve qe perdoruesi fut ne textfield.
    void search_book() {

    	ObservableList<Book> data = null;
    	
    	try {

    		Connection connection = DBConnection.getConnection();

    		Statement statement = connection.createStatement();
    		ResultSet resultSet = statement.executeQuery("select * from books");
    		data = FXCollections.observableArrayList();
    		while (resultSet.next()) {
    			data.add(new Book(resultSet.getInt("id"), 
    					resultSet.getString("name"), 
    					resultSet.getString("author"), 
    					resultSet.getString("year"),
    					resultSet.getInt("pages"), 
    					resultSet.getInt("copies"), 
    					resultSet.getDouble("price"), 
    					resultSet.getString("status")));
    		}
    	} catch (Exception e) {}

    	col_id.setCellValueFactory(new PropertyValueFactory<Book, Integer>("id"));
    	col_title.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
    	col_author.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
    	col_year.setCellValueFactory(new PropertyValueFactory<Book, String>("year"));
    	cl_pages.setCellValueFactory(new PropertyValueFactory<Book, Integer>("pages"));
    	col_copies.setCellValueFactory(new PropertyValueFactory<Book, Integer>("copies"));
    	col_price.setCellValueFactory(new PropertyValueFactory<Book, Double>("price"));
    	col_status.setCellValueFactory(new PropertyValueFactory<Book, String>("status"));
    	
    	Inventory.setItems(data);
    	FilteredList<Book> filteredData = new FilteredList<>(data, b -> true);

    	txt_search.textProperty().addListener((observable, oldValue, newValue) -> {
    		filteredData.setPredicate(book -> {
    			if (newValue == null || newValue.isEmpty()) {
    				return true;
    			}

    			String lowerCaseFilter = newValue.toLowerCase();

    			// Me ane te ketij blloku perdoruesi mund te kerkoje libra sipas: titullit, emrit te autorit dhe sipas ststusit modern apo klasik.
    			return book.getTitle().toLowerCase().contains(lowerCaseFilter)
    					|| book.getAuthor().toLowerCase().contains(lowerCaseFilter)
    					|| book.getStatus().toLowerCase().contains(lowerCaseFilter);

    		});
    	});

    	SortedList<Book> sortedData = new SortedList<>(filteredData);
    	sortedData.comparatorProperty().bind(Inventory.comparatorProperty());
    	Inventory.setItems(sortedData);
		
    }
    
    
    
    
    @FXML
    private Button btn_add;
    @FXML
    void add_book(ActionEvent event) {
         
         if ( checkFields() ) {
        	 
        	 try {
     			
            	 Connection connection = DBConnection.getConnection();
            	 
            	 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO books (name, author, year, pages, copies, price, status, imgPath) " 
            			 +"values (?, ?, ?, ?, ?, ?, ?, ?)");
            	 
            	 preparedStatement.setString(1, txt_title.getText());
            	 preparedStatement.setString(2, txt_author.getText());
            	 preparedStatement.setString(3, txt_year.getText());
            	 preparedStatement.setInt(4, Integer.parseInt(txt_pages.getText()));
            	 preparedStatement.setInt(5, Integer.parseInt(txt_copies.getText()));
            	 preparedStatement.setDouble(6, Double.parseDouble(txt_price.getText()));
            	 preparedStatement.setString(7, cmb_status.getValue());
            	 
            	 preparedStatement.setString(8, imageUrl);
            	 
            	 preparedStatement.execute();
            	 
            	 populate();
            	 clear();
            	 search_book();
            	 
            	 
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
        	 
         } else {
        	 
        	 Alert alert = new Alert(AlertType.ERROR);
        	 alert.setContentText("There was an error in one of the input fields.\nCheck that you input the data correctly and try again!");
        	 alert.show();
        	 
         }
         
    }
    
  
    // Funksioni qe ben te mundur upload-imin e imazhit te kopertines.
    @FXML
    private Button btn_import;    
    @FXML
    void import_image(ActionEvent event) {

    	FileChooser fileChooser = new FileChooser();
    	fileChooser.getExtensionFilters().add(new ExtensionFilter("Open File Image", "*.png", "*.jpg", "*.jpeg"));
    	
    	File file = fileChooser.showOpenDialog(null);
    	
    	if (file != null) {
            try {
                imageUrl = file.toURI().toURL().toExternalForm();
                Image image = new Image(imageUrl, 160, 200, false, true);
                img_cover.setImage(image);
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }

    }
   
    
    @FXML
    public void getSelectedData(MouseEvent event) {
		
    	Book selectedBook = Inventory.getSelectionModel().getSelectedItem();
    	
    	lbl_id.setText(String.valueOf(selectedBook.getId()));
    	txt_title.setText(selectedBook.getTitle());
    	txt_author.setText(selectedBook.getAuthor());
    	txt_year.setText(selectedBook.getYear());
    	txt_pages.setText(String.valueOf(selectedBook.getPages()));
    	txt_copies.setText(String.valueOf(selectedBook.getCopies()));
    	txt_price.setText(String.valueOf(selectedBook.getPrice()));
    	cmb_status.setValue(selectedBook.getStatus());
    	
    	try {
    		Connection connection = DBConnection.getConnection();
    		PreparedStatement statement = connection.prepareStatement("SELECT imgPath FROM books WHERE id=?");
    		statement.setString(1, lbl_id.getText());
    		ResultSet rSet = statement.executeQuery();
    		
    		if(rSet.next()) imageUrl = rSet.getString("imgPath");
    		
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	Image image = new Image(imageUrl, 160, 200, false, true);
    	
    	img_cover.setImage(image);
    	
	}
    
    @FXML
    private Button btn_mod;
    @FXML
    void modify_book(ActionEvent event) {

    	if ( checkFields() ) {

    		try {

    			Connection connection = DBConnection.getConnection();

    			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE books SET name=?, "
    					+ "author=?, year=?, pages=?, copies=?, price=?, status=?, imgPath=? WHERE id=?");

    			preparedStatement.setString(1, txt_title.getText());
    			preparedStatement.setString(2, txt_author.getText());
    			preparedStatement.setString(3, txt_year.getText());
    			preparedStatement.setInt(4, Integer.parseInt(txt_pages.getText()));
    			preparedStatement.setInt(5, Integer.parseInt(txt_copies.getText()));
    			preparedStatement.setDouble(6, Double.parseDouble(txt_price.getText()));
    			preparedStatement.setString(7, cmb_status.getValue());
    			preparedStatement.setString(8, imageUrl);
    			preparedStatement.setString(9, lbl_id.getText());

    			preparedStatement.execute(); 
    			populate();
    			search_book();
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}

    	} else {

    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setContentText("There was an error in one of the input fields.\nCheck that you input the data correctly and try again!");
    		alert.show();

    	}

    }

    @FXML
    private Button btn_remove;
    @FXML
    void remove_book(ActionEvent event) {
    	
    	try {
    		
    		Connection connection = DBConnection.getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM books WHERE id = ?");
    		
    		preparedStatement.setString(1, lbl_id.getText());
    		preparedStatement.execute();
			
    		populate();
	       	clear();
	       	search_book();
	       	 
		} catch (SQLException e) {
			e.printStackTrace();
		}

    }

    //Funksion privat per stilimin e butonave.
    private void styling() {
		
    	btn_search.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		btn_search.setStyle("-fx-background-color: #345e61; -fx-background-radius: 10;");
    	});
    	btn_search.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		btn_search.setStyle("-fx-background-color: #1d4c4f; -fx-background-radius: 10;");
    	});
    	
    	btn_add.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		btn_add.setStyle("-fx-background-color: #345e61; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-font-size: 14;");
    	});
    	btn_add.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		btn_add.setStyle("-fx-background-color: #1d4c4f; -fx-text-fill: white; -fx-font-family: Helvetica;  -fx-font-weight: bold; -fx-font-size: 14;");
    	});
    	
    	btn_remove.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		btn_remove.setStyle("-fx-background-color: #345e61; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-font-size: 14;");
    	});
    	btn_remove.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		btn_remove.setStyle("-fx-background-color: #1d4c4f; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-font-size: 14;");
    	});
    	
    	btn_mod.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		btn_mod.setStyle("-fx-background-color: #345e61; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-font-size: 14;");
    	});
    	btn_mod.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		btn_mod.setStyle("-fx-background-color: #1d4c4f; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-font-size: 14;");
    	});
    	
    	btn_import.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		btn_import.setStyle("-fx-background-color: #345e61; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 18;");
    	});
    	btn_import.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		btn_import.setStyle("-fx-background-color: #1d4c4f; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 18;");
    	});
    	
    	btn_clear.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		btn_clear.setStyle("-fx-background-color: #345e61; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 18;");
    	});
    	btn_clear.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		btn_clear.setStyle("-fx-background-color: #1d4c4f; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 18;");
    	});
    	
    	btn_logout.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		btn_logout.setStyle("-fx-background-color: #ca867a; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 18;");
    	});
    	btn_logout.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		btn_logout.setStyle("-fx-background-color: #fda898; -fx-text-fill: white; -fx-font-family: Helvetica; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 18;");
    	});
	}

}

