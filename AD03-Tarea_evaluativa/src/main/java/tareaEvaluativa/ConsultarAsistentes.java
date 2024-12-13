package tareaEvaluativa;

import java.sql.*;
import java.util.Scanner;

public class ConsultarAsistentes {

	public static void main(String[] args) {
		 consultarAsistentesEvento();
	}
	
	public static void muestraErrorSQL(SQLException e) {
		System.err.println("SQL ERROR mensaje: " + e.getMessage());
		System.err.println("SQL Estado: " + e.getSQLState());
		System.err.println("SQL código específico: " + e.getErrorCode());
	}
	
	public static void consultarAsistentesEvento() {
		String basedatos = "dbeventos";
		String host = "localhost";
		String port = "33306";
		String urlConnection = "jdbc:mysql://" + host + ":" + port + "/" + basedatos; 
		String user = "dbeventos";
		String pwd = "ainigon.birt24";
		
        Connection c = null;
        Scanner scanner = new Scanner(System.in);

        try {
            c = DriverManager.getConnection(urlConnection, user, pwd);

            String eventosQuery = "SELECT e.id_evento, e.nombre_evento FROM eventos e";
            PreparedStatement statementEventos = c.prepareStatement(eventosQuery);
            ResultSet rsEventos = statementEventos.executeQuery();

            System.out.println("Listado de eventos:");
            while (rsEventos.next()) {
                int idEvento = rsEventos.getInt("id_evento");
                String nombreEvento = rsEventos.getString("nombre_evento");
                System.out.println(idEvento + ". " + nombreEvento);
            }

            System.out.print("Ingrese el número del evento para consultar la cantidad de asistentes: ");
            int eventoSeleccionado = scanner.nextInt();
            scanner.nextLine(); 

            String consultaAsistentesQuery = "{? = CALL obtener_numero_asistentes(?)}";
            CallableStatement statementtAsistentes = c.prepareCall(consultaAsistentesQuery);
            statementtAsistentes.registerOutParameter(1, Types.INTEGER);  
            statementtAsistentes.setInt(2, eventoSeleccionado); 

            statementtAsistentes.execute(); 

            int numAsistentes = statementtAsistentes.getInt(1);
            System.out.println("El número de asistentes al evento seleccionado es: " + numAsistentes);

        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                if (scanner != null) {
                	scanner.close();
                }
            } catch (SQLException e) {
                System.out.println("Error cerrando la conexión: " + e.getMessage());
            }
        }
    }
}

