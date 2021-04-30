package mainScreen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	// creates a connection between the app and the SQLite database
	public static Connection connect() {
		Connection connection = null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:books.db");
		}
		catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}
