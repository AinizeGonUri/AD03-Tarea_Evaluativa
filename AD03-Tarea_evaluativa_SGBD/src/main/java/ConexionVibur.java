import java.sql.Connection;
import java.sql.SQLException;

import org.vibur.dbcp.ViburDBCPDataSource;
import org.vibur.dbcp.ViburDataSource;

public class ConexionVibur {
    public static void main(String[] args) {

    	 ViburDBCPDataSource dataSource = new ViburDBCPDataSource();
        
        dataSource.setJdbcUrl("jdbc:hsqldb:file:src/main/resources/pruebaDBVibur;shutdown=true");
        
        dataSource.setUsername("prueba");
        dataSource.setPassword("prueba");

        dataSource.start();
        
        try (Connection c = dataSource.getConnection()) {

            if (c.isValid(0)) {
                System.out.println("Conexión válida!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        dataSource.close();

    }
}
