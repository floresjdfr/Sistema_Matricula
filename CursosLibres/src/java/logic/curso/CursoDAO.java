package logic.curso;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import logic.Database;
import logic.usuario.profesor.ProfesorCRUD;


public class CursoDAO {
    
    
    private CursoDAO(){
        db = Database.instance();
    }
    
    public static CursoDAO obtenerInstancia(){
        if (instancia == null)
            instancia = new CursoDAO();
        return instancia;
    }
    
     public void crear(HttpServletRequest request) throws Exception {

        PreparedStatement stm = Database.instance().prepareStatement(CursoCRUD.CMD_AGREGAR);
        
        int b=Integer.parseInt(request.getParameter("oferta"));
       
        stm.setString(1, (String) request.getParameter("nombre"));
        stm.setString(2, (String) request.getParameter("tematica"));
        stm.setString(3, (String) request.getParameter("costo"));
        stm.setInt(4, b);
       
        int count = Database.instance().executeUpdate(stm);
        if (count == 0) {
            throw new Exception("duplicado");
        }
    }
    
    
    public Curso recuperar(int id) {
        Curso resultado = null;
        try {
            try (Connection cnx = db.getConnection();
                    PreparedStatement stm = cnx.prepareStatement(CursoCRUD.CMD_RECUPERAR)) {
                stm.clearParameters();
                stm.setInt(1, id);
                try (ResultSet rs = stm.executeQuery()) {
                    if (rs.next()) {
                        resultado = new Curso(
                                rs.getInt("codigo"),
                                rs.getString("nombre"),
                                rs.getString("tematica"),
                                rs.getString("costo"),
                                rs.getInt("oferta")
                        );
                    }
                }
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
                return resultado;
            }
        } catch (SQLException ex) {
            System.err.printf("Excepción: '%s'%n", ex.getMessage());
            return resultado;
        }
        return resultado;
    }
    
    public Service listarCursos(){
        Service listaCursos = new Service();
        Curso auxCurso;
        try (Connection cnx = db.getConnection(); PreparedStatement stm = cnx.prepareStatement(CursoCRUD.CMD_LISTAR)){
            
            try(ResultSet rs = stm.executeQuery()){
                while (rs.next()){
                    auxCurso = new Curso(
                                rs.getInt("codigo"),
                                rs.getString("nombre"),
                                rs.getString("tematica"),
                                rs.getString("costo"),
                                rs.getInt("oferta")
                    );
                    
                    listaCursos.cursosAdd(auxCurso);
                }
            } catch (Exception ex) {
                Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (URISyntaxException | IOException | SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaCursos;
        
    }
    
    
     public void eliminar(int p) throws Exception {

        PreparedStatement stm = Database.instance().prepareStatement(CursoCRUD.CMD_ELIMINAR);
        stm.setInt(1, p);
 
        int count = Database.instance().executeUpdate(stm);
        if (count == 0) {
            throw new Exception("duplicado");
        }
    }
    
     
      public void actualizar(Curso p) throws Exception {

        PreparedStatement stm = Database.instance().prepareStatement(CursoCRUD.CMD_ACTUALIZAR);
  
        stm.setString(1, p.getNombre());
        stm.setString(2, p.getTematica());
        stm.setString(3, p.getCosto());
        stm.setInt(4, p.getOferta());
        stm.setInt(5, p.getCodigo());
        
        System.out.print(p.getCodigo());
        System.out.print(p.getCosto());
        System.out.print(p.getNombre());
        System.out.print(p.getOferta());
        System.out.print(p.getTematica());
        System.out.print("picha pal culo");
 
         Database.instance().executeQuery(stm);
//        if (count == 0) {
//            throw new Exception("duplicado");
//        }
    }
    
    
    private Database db;
    private static  CursoDAO instancia;
}