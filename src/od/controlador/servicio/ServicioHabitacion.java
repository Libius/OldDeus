/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package od.controlador.servicio;
import java.util.Date;
import java.util.List;
import od.controlador.dao.HabitacionDao;
import od.modelo.Habitacion;

/**
 *
 * @author Dennis
 */
public class ServicioHabitacion {
    private HabitacionDao obj = new HabitacionDao();
    
    public Habitacion getHabitacion(){
        return obj.getHabitacion();
    }
    
    public boolean guardar(){
        return obj.guardar();
    }
    
    public List<Habitacion>todos(){
        return obj.listar();
    }
    
    public Habitacion obtener(Long id){
        return obj.obtener(id);
    }
    
    public void fijarHabitacion(Habitacion habitacion){
        obj.setHabitacion(habitacion);
    }
    public List<Habitacion> listarTipo(String tipo){
        return obj.listarTipo(tipo);
    }
    public List<Habitacion> listarEstado(Boolean estado){
        return obj.listarEstado(estado);
    }
    public List<Habitacion>listarBusqueda(String texto){
        return obj.listarBusqueda(texto);
    }
    public List<Habitacion> listarBusquedaTipo(String tipo,String texto){
        return obj.listarBusquedaTipo(tipo, texto);
    }
    public List<Habitacion> listarBusquedaEstado(Boolean estado,String texto){
        return obj.listarBusquedaEstado(estado, texto);
    }
    public List<Habitacion> listarDisponibles(Date inicio, Date fin){
        return obj.listarDisponibles(inicio, fin);
    }
    public Long cantidadDisponibles(Date inicio, Date fin, String codigo){
        return obj.cantidadDisponibles(inicio, fin, codigo);
    }
}
