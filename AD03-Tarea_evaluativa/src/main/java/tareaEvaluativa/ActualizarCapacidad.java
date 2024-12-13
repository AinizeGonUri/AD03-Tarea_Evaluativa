package tareaEvaluativa;

import java.sql.*;
import java.util.Scanner;

public class ActualizarCapacidad {
	
	public static void muestraErrorSQL(SQLException e) {
		System.err.println("SQL ERROR mensaje: " + e.getMessage());
		System.err.println("SQL Estado: " + e.getSQLState());
		System.err.println("SQL código específico: " + e.getErrorCode());
	}
	
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Introduce el nombre de la ubicación: ");
        String nombreUbicacion = scanner.nextLine();
        
        mostrarCapacidadYActualizar(nombreUbicacion);
        
        try {
			if(scanner != null) {
				scanner.close();
			}
		} catch(Exception ex) {
			
		}
    }

	public static void mostrarCapacidadYActualizar(String nombreUbicacion) {
		String basedatos = "dbeventos";
		String host = "localhost";
		String port = "33306";
		String urlConnection = "jdbc:mysql://" + host + ":" + port + "/" + basedatos; 
		String user = "dbeventos";
		String pwd = "ainigon.birt24";
		
		//Conexión a la base de datos
		Connection c = null;
		Scanner scanner = new Scanner(System.in);
	
		try {
			
			c = DriverManager.getConnection(urlConnection, user, pwd);
			
			String consulta = "SELECT capacidad FROM ubicaciones WHERE nombre = ?";
			PreparedStatement statement = c.prepareStatement(consulta);
			statement.setString(1, nombreUbicacion);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                int capacidadActual = rs.getInt("capacidad");
                System.out.println("La capacidad actual de la ubicación '" + nombreUbicacion + "' es: " + capacidadActual);

                System.out.print("Introduce la nueva capacidad máxima: ");
                int nuevaCapacidad = scanner.nextInt();

                //Actualizacion de capacidad
                String updateQuery = "UPDATE ubicaciones SET capacidad = ? WHERE nombre = ?";
                PreparedStatement updateStatement = c.prepareStatement(updateQuery);
                updateStatement.setInt(1, nuevaCapacidad);
                updateStatement.setString(2, nombreUbicacion);
                int filasAfectadas = updateStatement.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("Capacidad actualizada correctamente.");
                } else {
                    System.out.println("No se ha podido actualizar la capacidad.");
                }
            } else {
                System.out.println("La ubicación '" + nombreUbicacion + "' no existe.");
            }
		} catch (SQLException e) {
			muestraErrorSQL(e);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		finally {
			try {
				if(c != null) {
					c.close();
				}
				if(scanner != null) {
					scanner.close();
				}
			} catch(Exception ex) {
				
			}
		}

	}
}