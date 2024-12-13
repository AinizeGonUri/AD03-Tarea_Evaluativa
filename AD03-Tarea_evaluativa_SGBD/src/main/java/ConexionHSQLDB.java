import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionHSQLDB {
    public static void main(String[] args) {

        String url = "jdbc:hsqldb:file:src/main/resources/pruebaDBHSQLDB;shutdown=true";
        String usuario = "prueba";  
        String contraseña = "prueba";  

        try (Connection c = DriverManager.getConnection(url, usuario, contraseña)) {
            if (c.isValid(0)) {
                System.out.println("Conexión válida!");
            } else {
                System.out.println("La conexión no es válida.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
