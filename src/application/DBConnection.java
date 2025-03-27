package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	private static Connection DB_CONNECTION;
	
    private static final String DB_URL = "jdbc:mysql://localhost:3306/BOOK_STORE";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "p@$$word";

    public static Connection getConnection()  {
    	
        try {
        	
            Class.forName("com.mysql.cj.jdbc.Driver");
            DB_CONNECTION =  DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        
        return DB_CONNECTION;
    }
}
