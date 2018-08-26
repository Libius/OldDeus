
package od.controlador.dao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import od.modelo.Persona;

/**
 * Clase que permite utilizar los metodos del adaptador
 * @author Agreda Francisco
 * @author Macas Dennis
 * @author Ortega César
 * @version JDK 1.8
 */
public class PersonaDao extends AdaptadorDao<Persona>{
    //inicializacion de una variable privada
    private Persona persona;
    
    /**
     * Constructor para obtener las variables de modelo Persona
     */
    public PersonaDao() {
        super(Persona.class);
    }//Cierre del constructor
    
    /**
     * Permite obtener una nueva persona
     * @return devueve una persona
     */
    public Persona getPersona() {
        if(persona==null)
           persona= new Persona();
        return persona;
    }//Cierre de getPersona
    
    /**
     * Permite modificar una persona
     * @param persona acepta un dato persona de tipo Persona
     */
    public void setPersona(Persona persona) {
        this.persona = persona;
    }//Cierre de setPersona
    
    /**
     * Metodo que permite guardar y modificar persona
     * @return devuelve un valor de tipo booleano
     */
    public boolean guardar (){
        boolean verificar = false;
        try{
            getManager().getTransaction().begin();
            if(persona.getId_persona()!=null){
                modificar(persona);
            }else{
                guardar(persona);
            }
            getManager().getTransaction().commit();
            verificar = true;
        }catch(Exception e){
            System.out.println("No se ha podido guardar o modificar " + e);
        }
        return verificar;
    }//Cierre del metodo guardar
    
    /**
     * Metodo que permite obtener la cedula de un persona dentro de la base de datos
     * @param cedula acepta un dato cedula de tipo String
     * @return devuelve la cedula de la persona de la base de datos 
     */
    public Persona obtenerPersonaCedula(String cedula){
        Persona p = null;
        try {
            Query q = getManager().createQuery("SELECT p FROM Persona p WHERE p.dni = :cedula");
            q.setParameter("cedula", cedula);
            p = (Persona)q.getSingleResult();
        } catch (Exception e) {
        }
        return p;
    }//Cierre del metodo obtenerPersonaCedula
    
    /**
     * Metodo que permite listar personas sin administrador
     * @return devuelve una lista con las personas que se encuentra dentro de la base de datos 
     */
    public List<Persona> listarSinAdministrador(){
        List<Persona> lista = new ArrayList<>();
        try{
            Query q = getManager()
                    .createQuery("SELECT p FROM Persona p WHERE p.rol.nombre != :admin AND p.rol.nombre != :res");
            q.setParameter("admin", "Administrador");
            q.setParameter("res", "Recepcionista");
            lista = q.getResultList();
        }catch(Exception e){
            System.out.println("Meth: listarSinAdministrador()");
        }
        return lista;
    }//Cierre del metodo listarSinAdministrador
    
    /**
     * Metodo para listar personas sin administrador mediante una busqueda 
     * @param texto acepta una dato texto de tipo String
     * @return devuelve una lista de personas buscadas por el texto de la base de datos
     */
    public List<Persona> listarSinAdministradorBusqueda(String texto){
        List<Persona> lista = new ArrayList<>();
        try{
            Query q = getManager()
                    .createQuery("SELECT p FROM Persona p WHERE p.rol.nombre != :admin AND p.rol.nombre != :res "
                            + "AND (LOWER(p.nombres) LIKE CONCAT('%', :texto, '%') "
                            + "OR LOWER(p.apellidos) LIKE CONCAT('%', :texto, '%'))");
            q.setParameter("admin", "Administrador");
            q.setParameter("res", "Recepcionista");
            q.setParameter("texto", texto);
            lista = q.getResultList();
        }catch(Exception e){
            System.out.println("Meth: listarSinAdministrador()");
        }
        return lista;
    }//Cierre del metodo listarSinAdministradorBusqueda
    
    /**
     * Metodo que permite buscar personas mediante el DNI
     */
    public List<Persona> listarSinAdministradorDNIBusqueda(String texto){
        List<Persona> lista = new ArrayList<>();
        try{
            Query q = getManager()
                    .createQuery("SELECT p FROM Persona p WHERE p.rol.nombre != :admin AND p.rol.nombre != :res "
                            + "AND  p.dni LIKE CONCAT('%', :texto, '%')");
            q.setParameter("admin", "Administrador");
            q.setParameter("res", "Recepcionista");
            q.setParameter("texto", texto);
            lista = q.getResultList();
        }catch(Exception e){
            System.out.println("Meth: listarSinAdministrador()");
        }
        return lista;
    }
    
    public List<Persona> listarSinAdministradorGeneroBusqueda(String texto, String genero){
        List<Persona> lista = new ArrayList<>();
        try{
            Query q = getManager()
                    .createQuery("SELECT p FROM Persona p WHERE p.rol.nombre != :admin AND p.rol.nombre != :res "
                            + "AND  p.sexo = :genero "
                            + "AND (LOWER(p.nombres) LIKE CONCAT('%', :texto, '%') "
                            + "OR LOWER(p.apellidos) LIKE CONCAT('%', :texto, '%'))");
            q.setParameter("admin", "Administrador");
            q.setParameter("res", "Recepcionista");
            q.setParameter("texto", texto);
            q.setParameter("genero", genero);
            lista = q.getResultList();
        }catch(Exception e){
            System.out.println("Meth: listarSinAdministrador()");
        }
        return lista;
    }
}
