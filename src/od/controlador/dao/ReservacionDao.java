/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package od.controlador.dao;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import od.modelo.Reservacion;

/**
 * Clase que permite utilizar los metodos del adaptador
 * @author Agreda Francisco
 * @author Macas Dennis
 * @author Ortega César
 * @version JDK 1.8
 */
public class ReservacionDao extends AdaptadorDao<Reservacion>{
    //inicializacion de una variable privada
     private Reservacion reservacion;
     /**
     * Constructor para obtener las variables de modelo Reservacion
     */
     public ReservacionDao() {
        super(Reservacion.class);
    }//Cierre del constructor

     /**
     * Permite obtener una nueva reservacion
     * @return devueve una reservacion
     */
    public Reservacion getReservacion() {
        if(reservacion==null)
           reservacion= new Reservacion();
        return reservacion;
    }//Cierre de getReservacion
    
/**
     * Permite modificar una Reservacion
     * @param reservacion acepta un dato reservacion de tipo Reservacion
     */
    public void setReservacion(Reservacion reservacion) {
        this.reservacion = reservacion;
    }//Cierre de setReservacion
    
    /**
     * Metodo que permite guardar y modificar reservacion
     * @return devuelve un valor de tipo booleano
     */
    public boolean guardar (){
        boolean verificar = false;
        try{
            getManager().getTransaction().begin();
            if(reservacion.getId_reservacion()!=null){
                modificar(reservacion);
            }else{
                guardar(reservacion);
            }
            getManager().getTransaction().commit();
            verificar = true;
        }catch(Exception e){
            System.out.println("No se ha podido guardar o modificar " + e);
        }
        return verificar;
    }//Cierre del metodo guardar
    

    /**
     * Metodo que permite listar Reservaciones por tipo
     * @param tipo acepta un dato reservacion de tipo String
     * @return devuelve una lista con las reservaciones
     */
      public List<Reservacion>listarTipo(Boolean tipo){
        List<Reservacion> lista= new ArrayList<>();
        try {
            Query q = getManager().createQuery("SELECT p FROM Reservacion p where p.estado = :tipo");  
            q.setParameter("tipo", tipo);
            lista = q.getResultList();
        } catch (Exception e) {
            System.out.println("error"+e);
        }
        return lista;
    }//cierre del metodo listarTipo

      /**
     * Metodo que permite listar Reservaciones por busqueda
     * @param texto acepta un dato texto de tipo String
     * @return devuelve una lista con las reservaciones que coinciden con la busqueda
     */
    public List<Reservacion>listarBusqueda(String texto){
        List<Reservacion> lista= new ArrayList<>();
        try {
            Query q = getManager()
                    .createQuery("SELECT r FROM Reservacion r where ((LOWER(r.persona.apellidos)LIKE CONCAT(:texto, '%')) OR (LOWER(r.persona.nombres)LIKE CONCAT(:texto, '%')))");//lower es minusculas
            q.setParameter("texto", texto);
            lista = q.getResultList();
        } catch (Exception e) {
        }
        return lista;
    }//Cierre del metodo listarBusqueda
    
     /**
     * Metodo que permite listar Reservaciones por busqueda y tipo
     * @param tipo acepta un dato tipo de tipo String
     * @param texto acepta un dato texto de tipo String
     * @return devuelve una lista con las reservaciones que coinciden con la busqueda
     */
    public List<Reservacion> listarBusquedaTipo(Boolean tipo,String texto){
        List<Reservacion> listado= new ArrayList<>();
        try {
            Query q = getManager().createQuery("SELECT r FROM Reservacion r where ((LOWER(r.persona.apellidos)LIKE CONCAT(:texto, '%')) OR (LOWER(r.persona.nombres)LIKE CONCAT(:texto, '%'))) and r.estado = :tipo");
            q.setParameter("tipo", tipo);
            q.setParameter("texto", texto);
            listado = q.getResultList();
        } catch (Exception e) {
        }
        
        return listado;
    }//cierre del metodo listarBusquedaTipo
    
    /**
     * Metodo que permite listar las Reservaciones en orden ascendente
     * @param orden acepta un dato orden de tipo String
     * @return devuelve una lista con las reservaciones que coinciden con la busqueda
     */
     public List<Reservacion> ordenAscendente(String orden){
        orden = (orden.equals("Fecha")) ? "r.fecha" : "r.persona.nombres";
        List<Reservacion> listado= new ArrayList<>();
        try {
            Query q = getManager().createQuery("SELECT r FROM Reservacion r ORDER BY "+ orden +"  ASC");
            listado = q.getResultList();
        } catch (Exception e) {
        }
        return listado;
    }//cierre del metodo ordenAscendente
}//cierre de la clase ReservacionDao
