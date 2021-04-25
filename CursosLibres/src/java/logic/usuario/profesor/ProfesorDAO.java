package logic.usuario.profesor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.Database;


public class ProfesorDAO {
    
    
    private ProfesorDAO(){
        db = Database.instance();
    }
    
    public static ProfesorDAO obtenerInstancia(){
        if (instancia == null)
            instancia = new ProfesorDAO();
        return instancia;
    }
       
    public Profesor recuperar(int id) {
        Profesor resultado = null;
        try {
            try (Connection cnx = db.getConnection();
                    PreparedStatement stm = cnx.prepareStatement(ProfesorCRUD.CMD_RECUPERAR)) {
                stm.clearParameters();
                stm.setInt(1, id);
                try (ResultSet rs = stm.executeQuery()) {
                    if (rs.next()) {
                        resultado = new Profesor(
                                 rs.getInt("idProfesor"),
                                rs.getString("nombre"),
                                rs.getString("apellido1"),
                                rs.getString("apellido2"),
                                rs.getString("correo"),
                                rs.getString("telefono"),
                                rs.getString("especialidad"),
                                rs.getString("password")
                        );
                    }
                }
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(ProfesorDAO.class.getName()).log(Level.SEVERE, null, ex);
                return resultado;
            }
        } catch (SQLException ex) {
            System.err.printf("Excepción: '%s'%n", ex.getMessage());
            return resultado;
        }
        return resultado;
    }
    private Database db;
    private static ProfesorDAO instancia;
}