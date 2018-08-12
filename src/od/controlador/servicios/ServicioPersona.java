/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package od.controlador.servicios;

import java.util.List;
import od.controlador.daos.PersonaDao;
import od.modelo.Persona;

/**
 *
 * @author Dennis
 */
public class ServicioPersona {
    private PersonaDao obj= new PersonaDao();
    public Persona getPersona(){
        return obj.getPersona();
    }
    public boolean guardar(){
        return obj.guardar();
    }
    public List<Persona>todos(){
        return obj.listar();
    }
    public Persona obtener(Long id){
        return obj.obtener(id);
    }
    public void fijarPersona(Persona persona){
        obj.setPersona(persona);
    }
            
            
}
