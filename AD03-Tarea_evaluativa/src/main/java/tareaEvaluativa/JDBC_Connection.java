package tareaEvaluativa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC_Connection {

	public static void muestraErrorSQL(SQLException e) {
		System.err.println("SQL ERROR mensaje: " + e.getMessage());
		System.err.println("SQL Estado: " + e.getSQLState());
		System.err.println("SQL código específico: " + e.getErrorCode());
	}
	
	public static void main(String[] args) {
		String basedatos = "dbeventos";
		String host = "localhost";
		String port = "33306";
		String urlConnection = "jdbc:mysql://" + host + ":" + port + "/" + basedatos; 
		String user = "dbeventos";
		String pwd = "ainigon.birt24";
		
		Connection c = null;
		
		try {
			c = DriverManager.getConnection(urlConnection, user, pwd);
			System.out.println("Conexión realizada.");
		} catch (SQLException e) {
			muestraErrorSQL(e);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		finally {
			try {
				if(c != null) c.close();
			} catch(Exception ex) {
				
			}
		}
	}

}
