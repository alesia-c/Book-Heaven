package application;

public class Book {
	
	private int id;
	private String title;
	private String author;
	private String year;
	private int pages;
	private int copies;
	private double price;
	private String status;
	private String img_path;
		
	public Book(int id, String title, String author, String year, int pages, int copies, double price, String status, String img) {
	
		this.id = id;
		this.title = title;
		this.author = author;
		this.year = year;
		this.pages = pages;
		this.copies = copies;
		this.price = price;
		this.status = status;
		this.img_path = img;
	}
	
	public Book(int id, String title, String author, String year, int pages, int copies, double price, String status) {
		
		this.id = id;
		this.title = title;
		this.author = author;
		this.year = year;
		this.pages = pages;
		this.copies = copies;
		this.price = price;
		this.status = status;
		
	}
	
	public Book(int id, String title, String author, String imgPath) {
		
		this.id = id;
		this.title = title;
		this.author = author;
		this.img_path = imgPath;
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public int getCopies() {
		return copies;
	}
	public void setCopies(int copies) {
		this.copies = copies;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getImg_path() {
		return img_path;
	}
	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}
     
	
     
}
