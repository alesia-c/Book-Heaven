package application;

public class Cart_Item {

	private String user;
	private String bookTitle;
	private int quantity;
	private double price;
	
	
	public Cart_Item(String bookTitle, int quantity, double price) {
		this.user = Main.username;
		this.bookTitle = bookTitle;
		this.quantity = quantity;
		this.price = price;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getBookTitle() {
		return bookTitle;
	}


	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}
	
	
	
	
}
