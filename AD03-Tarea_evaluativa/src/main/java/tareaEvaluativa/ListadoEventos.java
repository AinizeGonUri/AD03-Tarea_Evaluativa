package tareaEvaluativa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ListadoEventos {
	
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
		Statement s = null;
		ResultSet rs = null;
		
		try {
			
			//Conexión a la base de datos
			c = DriverManager.getConnection(urlConnection, user, pwd);
			s = c.createStatement();
			
            boolean hasResultSet = s.execute(
                    "SELECT e.nombre_evento, u.nombre AS nombre_ubicacion, u.direccion, COUNT(ae.dni) AS num_asistentes " +
                    "FROM eventos e " +
                    "JOIN ubicaciones u ON e.id_ubicacion = u.id_ubicacion " +
                    "LEFT JOIN asistentes_eventos ae ON e.id_evento = ae.id_evento " +
                    "GROUP BY e.id_evento, u.id_ubicacion"
                );
            
			if (hasResultSet) {
	               rs = s.getResultSet();
	
	               System.out.println("\n LISTADO DE EVENTOS: \n");
	               System.out.println("-".repeat(126));
	               System.out.printf("| %-38s| %-12s| %-35s| %-32s|\n", "Evento", "Asistentes", "Ubicación", "Dirección");
	               System.out.println("-".repeat(126));
	
	               //Loop para que aparezcan los distintos datos almacenados en la base de datos
	               while (rs.next()) {
	                    String nombreEvento = rs.getString("nombre_evento");
	                    int numAsistentes = rs.getInt("num_asistentes");
	                    String direccion = rs.getString("direccion");
	                    String nombreUbicacion = rs.getString("nombre_ubicacion");

	
	                    System.out.printf("| %-38s| %-12d| %-35s| %-32s|\n", nombreEvento, numAsistentes, nombreUbicacion, direccion);
	               }
	               
	               //Guiones para la tabla (decorativo)
	               System.out.println("-".repeat(126));
	            }
		} catch (SQLException e) {
			muestraErrorSQL(e);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		finally {
			try {
				if(c != null) c.close();
				if (s != null) s.close();
                if (rs != null) rs.close();
			} catch(Exception ex) {
				
			}
		}

	}

}
