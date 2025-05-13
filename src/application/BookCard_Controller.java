package application;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class BookCard_Controller {

	@FXML
    private VBox bookCard_container;
	
	@FXML
    private Label author;

    @FXML
    private ImageView bookCover;

    @FXML
    private Label bookTitle;
    
    private Book bookInfo;
    
    
    // Funksioni merr argumentin dhe ben inicializimin e faqes fxml/
    public void SetData(Book bookInfo)  {
		
    	this.bookInfo = bookInfo;
    	
    	bookTitle.setText(bookInfo.getTitle());
    	author.setText(bookInfo.getAuthor());
    	
    	Image image = new Image(bookInfo.getImg_path(), 190, 270, false, true);
    	bookCover.setImage(image);
    	
	}

    public Book getBookInfo() {
		return bookInfo;
	}

    Helper helper =  new Helper();
    
    
    // Ne interface-in shop, kur klikohet mbi nje liber, thirret ky funksion i cili inicializon skedarin fxml me te dhenat e librit dhe ben nderrimin e skenes.
	@FXML
    void openProductPage(MouseEvent event) {        
	
		try {	
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Product_Page.fxml"));	
			Parent root = loader.load();
			Scene skena = new Scene(root);
	    	
	    	Main.mainStage.setScene(skena);
	    	Main.mainStage.show();
	    	
	    	ProductPage_Controller controller= loader.getController();
			controller.SetData(bookTitle.getText());
			
			} catch (Exception e) {
			e.printStackTrace();
			}
    }

}
