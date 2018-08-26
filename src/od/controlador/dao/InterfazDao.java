
package od.controlador.dao;

import java.util.List;

/**
 * Interfaz que permite crear metodos abstractos
 * @author Agreda Francisco
 * @author Macas Dennis
 * @author Ortega César
 * @version JDK 1.8
 */
public interface InterfazDao <T>{
    public void guardar (T obj)throws Exception;
    public void modificar (T obj)throws Exception;
    public List<T> listar();
    public T obtener (Long id);
}//Cierre de la interfaz
