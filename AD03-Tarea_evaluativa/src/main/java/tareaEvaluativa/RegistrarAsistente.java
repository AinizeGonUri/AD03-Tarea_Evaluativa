package tareaEvaluativa;

import java.sql.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrarAsistente {

    public static void main(String[] args) {
        registrarAsistente();
    }

    public static void muestraErrorSQL(SQLException e) {
        System.err.println("SQL ERROR mensaje: " + e.getMessage());
        System.err.println("SQL Estado: " + e.getSQLState());
        System.err.println("SQL código específico: " + e.getErrorCode());
    }
    
    //Validación DNI
    public static boolean esDniValido(String dni) {
        String regex = "^[0-9]{8}[A-Za-z]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dni);
        return matcher.matches();
    }

    public static void registrarAsistente() {
        String basedatos = "dbeventos";
        String host = "localhost";
        String port = "33306";
        String urlConnection = "jdbc:mysql://" + host + ":" + port + "/" + basedatos;
        String user = "dbeventos";
        String pwd = "ainigon.birt24";
        
        //Conexión base de datos
        Connection c = null;
        Scanner scanner = new Scanner(System.in);

        try {

            c = DriverManager.getConnection(urlConnection, user, pwd);

            System.out.print("Introduce el DNI del asistente: ");
            String dni = scanner.nextLine();
            
            //Comprobacion DNI
            if(esDniValido(dni) == false) {
            	System.out.println("El formato del DNI no es válido");
            	return;
            }

            //Comprobacion existe asistente
            String verificacionDniQuery = "SELECT nombre FROM asistentes WHERE dni = ?";
            PreparedStatement statementDni = c.prepareStatement(verificacionDniQuery);
            statementDni.setString(1, dni);
            ResultSet rsDni = statementDni.executeQuery();
            String nombreAsistente;

            if (rsDni.next()) {
                nombreAsistente = rsDni.getString("nombre");
                System.out.println("Estás realizando la reserva para: " + nombreAsistente);

            } else {
                System.out.println("No se encontró un asistente con el DNI proporcionado.");
                System.out.print("Introduce el nombre del asistente: ");
                nombreAsistente = scanner.nextLine();
                System.out.println("Estás realizando la reserva para: " + nombreAsistente);

                String insertarAsistenteQuery = "INSERT INTO asistentes (dni, nombre) VALUES (?, ?)";
                PreparedStatement statementInsertar = c.prepareStatement(insertarAsistenteQuery);
                statementInsertar.setString(1, dni);
                statementInsertar.setString(2, nombreAsistente);
                statementInsertar.executeUpdate();
            }
            
            //Listado de eventos
            String eventosQuery = "SELECT e.id_evento, e.nombre_evento, u.capacidad " +
                    "FROM eventos e JOIN ubicaciones u ON e.id_ubicacion = u.id_ubicacion";
            PreparedStatement statementEventos = c.prepareStatement(eventosQuery);
            ResultSet rs = statementEventos.executeQuery();

            System.out.println("Lista de eventos:");

            while (rs.next()) {
                int idEvento = rs.getInt("id_evento");
                String nombreEvento = rs.getString("nombre_evento");
                int capacidad = rs.getInt("capacidad");

                String asistentesQuery = "SELECT COUNT(*) AS asistentes FROM asistentes_eventos WHERE id_evento = ?";
                PreparedStatement stmtAsistentes = c.prepareStatement(asistentesQuery);
                stmtAsistentes.setInt(1, idEvento);
                ResultSet rsAsistentes = stmtAsistentes.executeQuery();
                int cantidadAsistentes = 0;
                if (rsAsistentes.next()) {
                    cantidadAsistentes = rsAsistentes.getInt("asistentes");
                }

                int espaciosDisponibles = capacidad - cantidadAsistentes;
                System.out.println(idEvento + ". " + nombreEvento + " - Espacios disponibles: " + espaciosDisponibles);
            }

            //Elección evento
            System.out.print("Elige el número del evento al que quiere asistir: ");
            int eventoSeleccionado = scanner.nextInt();
            scanner.nextLine(); 
            
            String eventoQuery = "SELECT e.nombre_evento, u.capacidad FROM eventos e " +
                    "JOIN ubicaciones u ON e.id_ubicacion = u.id_ubicacion WHERE e.id_evento = ?";
            PreparedStatement statementEvento = c.prepareStatement(eventoQuery);
            statementEvento.setInt(1, eventoSeleccionado);
            ResultSet rsEvento = statementEvento.executeQuery();
            
            if (rsEvento.next()) {
                String nombreEvento = rsEvento.getString("nombre_evento");
                int capacidadUbicacion = rsEvento.getInt("capacidad");

                String asistentesQuery = "SELECT COUNT(*) AS asistentes FROM asistentes_eventos WHERE id_evento = ?";
                PreparedStatement stmtAsistentes = c.prepareStatement(asistentesQuery);
                stmtAsistentes.setInt(1, eventoSeleccionado);
                ResultSet rsAsistentes = stmtAsistentes.executeQuery();
                int cantidadAsistentes = 0;
                if (rsAsistentes.next()) {
                    cantidadAsistentes = rsAsistentes.getInt("asistentes");
                }
                
                //Comprobacion capacidad maxima
                if (cantidadAsistentes >= capacidadUbicacion) {
                    System.out.println("El evento ha alcanzado su capacidad máxima. No se puede registrar.");
                    return;
                }

                //Registro asistente evento
                try {
                	String insertarAsistenteEventoQuery = "INSERT INTO asistentes_eventos (dni, id_evento) VALUES (?, ?)";
                    PreparedStatement statementInsertarEvento = c.prepareStatement(insertarAsistenteEventoQuery);
                    statementInsertarEvento.setString(1, dni);
                    statementInsertarEvento.setInt(2, eventoSeleccionado);
                    statementInsertarEvento.executeUpdate();
                    
                    System.out.println(nombreAsistente + " ha sido registrado para el evento seleccionado.");
                } catch (SQLException e) {
                    if(e.getErrorCode() == 1062) {
                    	System.out.println("El asistente ya está registrado");
                    } else {
                    	throw e;
                    }
                }
            } else {
                System.out.println("El evento seleccionado no existe.");
            }

        } catch (SQLException e) {
            muestraErrorSQL(e);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            try {
                if (c != null) c.close();
                if (scanner != null) scanner.close();
            } catch (Exception ex) {

            }
        }

    }
}