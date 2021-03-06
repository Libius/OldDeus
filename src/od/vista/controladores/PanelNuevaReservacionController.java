/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package od.vista.controladores;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import od.Principal;
import od.controlador.servicio.ServicioDetalle;
import od.controlador.servicio.ServicioHabitacion;
import od.controlador.servicio.ServicioPersona;
import od.controlador.servicio.ServicioReservacion;
import od.controlador.servicio.ServicioServicio;
import od.modelo.Habitacion;
import od.modelo.Servicio;
import od.utilidades.Sesiones;
import od.utilidades.Utilidades;
import od.utilidades.Validadores;
import od.vista.utilidades.UtilidadesComponentes;

/**
 * FXML Controller class
 *
 * @author PotatoPower
 */
public class PanelNuevaReservacionController {

    /**
     * Initializes the controller class.
     */
    public void initialize() {
        listaServicios.setItems(FXCollections.observableList(ss.todos()));
        listaServicios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        if (Sesiones.getCuenta().getPersona().getRol().getNombre().equals("Cliente")) {
            campoDNI.setText(Sesiones.getCuenta().getPersona().getDni());
            validarID();
            btnDNIRegreso.setVisible(false);
        }
    }   
    
    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }   

    private void limpiar() {
        sp.fijarPersona(null);
        sh.fijarHabitacion(null);
        sr.fijarReservacion(null);
        sd.fijarDetalle(null);
        ss.fijarServicio(null);
        listaServicios.getSelectionModel().clearSelection();
    }
    
    private void limpiarLabels(){
        lblValorHabitacion.setText("00.00");
        lblValorServicios.setText("00.00");
        lblNoches.setText("00.00");
        lblSubtotal.setText("00.00");
        lblValorIVA.setText("00.00");
        lblTotal.setText("00.00");
        disponibles = 0;
        btnReservar.setDisable(true);
    }
    
    private void limpiarDNI(){
        campoCliente.setText("");
        campoDNI.setText("");
        campoDNI.setDisable(false);
    }
    
    private void limpiarFechas(){
        fijarFechaDeHoy();
        campoNroAdultos.setText("");
        campoNroNinios.setText("");
        panelReserva.setDisable(true);
    }
    
    private void limpiarHabitacion(){
        cbxHabitaciones.setValue(null);
        lblDisponibles.setText("...");
        lblCapacidad.setText("...");
        lblCamas.setText("...");
        lblCamas.setText("...");
        listaServicios.getSelectionModel().clearSelection();
        campoNroHabitaciones.setText("");
        lblDescripcion.setText("");
        panelHabitaciones.setDisable(true);
    }
    
    private void fijarFechaDeHoy(){
        campoFechaEntrada.setValue(LocalDate.now());
        campoFechaSalida.setValue(LocalDate.now().plus(1, ChronoUnit.DAYS));
    }
    
    private Double calcularSubtotal(){
        NumberFormat df = new DecimalFormat("#0.00");
        double servicios = listaServicios.getSelectionModel().getSelectedItems().stream().mapToDouble((selectedItem) -> selectedItem.getPrecio()).sum();
        lblValorServicios.setText("$" + df.format(servicios));
        double habitacion = 0.00;
        if (cbxHabitaciones.getSelectionModel().getSelectedItem() != null) {
            habitacion = h.get(cbxHabitaciones.getSelectionModel().getSelectedIndex()).getPrecio() * ((campoNroHabitaciones.getText().trim().isEmpty()) ? 0:Integer.parseInt(campoNroHabitaciones.getText()));
        }        
        lblValorHabitacion.setText("$" + df.format(habitacion));
        long noches = ChronoUnit.DAYS.between(campoFechaEntrada.getValue(), campoFechaSalida.getValue());
        double subtotal = (habitacion + servicios) * noches;
        lblSubtotal.setText("$" + df.format(subtotal));
        
        return subtotal;
    }
    
    @FXML
    private Double calcularTotal(){
        NumberFormat df = new DecimalFormat("#0.00");
        
        double subtotal = calcularSubtotal();
        
        double iva = subtotal * 0.14;
        lblValorIVA.setText("$" + df.format(iva));
        lblTotal.setText("$" + df.format(subtotal + iva));
        
        return subtotal + iva;
    }

    @FXML
    private boolean validarID() {
        if (Validadores.validarTF(campoDNI)) {
            if (UtilidadesComponentes.validadorDeCedula(campoDNI.getText())) {
                if (sp.ObtenerPersonaCedula(campoDNI.getText()) != null) {
                    sp.fijarPersona(sp.ObtenerPersonaCedula(campoDNI.getText()));
                    campoCliente.setText(sp.getPersona().getNombres() + " " + sp.getPersona().getApellidos());
                    campoDNI.setDisable(true);
                    panelReserva.setDisable(false);                      
                    fijarFechaDeHoy();
                    return true;
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("No encontrado");
                    alert.setHeaderText("");
                    alert.setContentText("La cédula ingresada no existe.");
                    alert.showAndWait();
                    return false;
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Incorrecto");
                alert.setHeaderText("");
                alert.setContentText("La cédula ingresada no es válida.");
                alert.showAndWait();
                return false;
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campo vacío");
            alert.setHeaderText("");
            alert.setContentText("El campo está vacío.");
            alert.showAndWait();
            return false;
        }
    }

    @FXML
    private boolean validarPanel1() {
        if (Validadores.validarDP(campoFechaEntrada)
                && Validadores.validarDP(campoFechaSalida)
                && Validadores.validarTF(campoNroAdultos)
                && Validadores.validarTF(campoNroNinios)) {
            h = FXCollections.observableList(sh.listarDisponibles(Utilidades.extraerFecha(campoFechaEntrada.getValue()), Utilidades.extraerFecha(campoFechaSalida.getValue())));
            
            ObservableList obs = FXCollections.observableArrayList();            
            h.forEach((habitacion) -> {
                obs.add(habitacion.getNombre());
            });
            
            cbxHabitaciones.setItems(obs);
            
            if (cbxHabitaciones.getItems().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Agotado");
                alert.setHeaderText("");
                alert.setContentText("Todas las habitaciones para ese intervalo de fechas se encuentran actualmente agotadas.");
                alert.showAndWait();
                return false;
            }else{
                lblNoches.setText("" + ChronoUnit.DAYS.between(campoFechaEntrada.getValue(), campoFechaSalida.getValue()));
                campoNroHabitaciones.setText("1");
                panelHabitaciones.setDisable(false);
                panelReserva.setDisable(true);
                btnReservar.setDisable(false);
                return true;
            }
            
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campo vacíos");
            alert.setHeaderText("");
            alert.setContentText("Rellene todos los campos vacíos.");
            alert.showAndWait();
            return false;
        }
    }

    @FXML
    private boolean validarPanel2() {
        if (cbxHabitaciones.getValue() != null) {
//            if (listaServicios.getSelectionModel().getSelectedItems().size() > 0) {
                if (Validadores.validarTF(campoNroHabitaciones)) {
                    return true;
                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Campo vacíos");
                    alert.setHeaderText("");
                    alert.setContentText("Rellene todos los campos vacíos.");
                    alert.showAndWait();
                    return false;
                }                
//            }else{
//                Alert alert = new Alert(Alert.AlertType.WARNING);
//                alert.setTitle("Sin Selección");
//                alert.setHeaderText("");
//                alert.setContentText("Seleccione algún servicio de la Lista");
//                alert.showAndWait(); 
//                return false;
//            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sin Selección");
            alert.setHeaderText("");
            alert.setContentText("No hay una habitación seleccionada.");
            alert.showAndWait();
            return false;
        }
    }
    
    @FXML
    private void fijarDescripcion(){
        if (cbxHabitaciones.getValue() != null) {
            disponibles = sh.cantidadDisponibles(Utilidades.extraerFecha(campoFechaEntrada.getValue()), Utilidades.extraerFecha(campoFechaSalida.getValue()), h.get(cbxHabitaciones.getSelectionModel().getSelectedIndex()).getCodigo()).intValue();
            lblDisponibles.setText(disponibles + " Habitacion(es)");
            lblCapacidad.setText(h.get(cbxHabitaciones.getSelectionModel().getSelectedIndex()).getCapacidad() + " Persona(s)");
            lblCamas.setText(h.get(cbxHabitaciones.getSelectionModel().getSelectedIndex()).getNro_camas() + " Cama(s)");
            lblDescripcion.setText(h.get(cbxHabitaciones.getSelectionModel().getSelectedIndex()).getDescripcion());
            if(campoNroHabitaciones.isDisabled()){
                campoNroHabitaciones.setDisable(false);
            }
            calcularTotal();
        }
    }
    
    @FXML
    private void handleReservar(){        
        if (validarPanel1() && validarPanel2()) {
            Alert conf = new Alert(Alert.AlertType.CONFIRMATION);        
            conf.setTitle("Confirmación");
            conf.setHeaderText("¿Está seguro/a de realizar la reserva?");
            conf.setContentText("Presione Aceptar para completar la acción.");
            conf.showAndWait();
            
            if (conf.getResult().getText().equals("Aceptar")){
                sr.getReservacion().setFecha(new Date());            
                sr.getReservacion().setFecha_inicio(Utilidades.extraerFecha(campoFechaEntrada.getValue()));
                sr.getReservacion().setFecha_fin(Utilidades.extraerFecha(campoFechaSalida.getValue()));

                sr.getReservacion().setEstado(Boolean.TRUE);
                sr.getReservacion().setPago_total(calcularTotal());
                sr.getReservacion().setPersona(sp.getPersona());
                sr.getReservacion().setHabitacion(h.get(cbxHabitaciones.getSelectionModel().getSelectedIndex()));

                sd.getDetalle().setAdultos(Integer.parseInt(campoNroAdultos.getText()));
                sd.getDetalle().setMenores(Integer.parseInt(campoNroNinios.getText()));
                sd.getDetalle().setObservaciones(campoObservaciones.getText().trim());
                sd.getDetalle().setPago_subtotal(listaServicios.getSelectionModel().getSelectedItems().stream().mapToDouble((selectedItem) -> selectedItem.getPrecio()).sum());
                sd.getDetalle().setCant_habitaciones(Integer.parseInt(campoNroHabitaciones.getText()));
                sd.getDetalle().setServicios(listaServicios.getSelectionModel().getSelectedItems());
                sd.getDetalle().setReservacion(sr.getReservacion());

                sr.getReservacion().setDetalle(sd.getDetalle());

                if (sr.guardar()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Guardado");
                    alert.setHeaderText("");
                    alert.setContentText("Se ha guardado el registro correctamente.");
                    Utilidades.guardarHistorial("Nueva Reservación", "Una nueva reserva se ha registrado", Sesiones.getCuenta().getPersona());
                    Utilidades.guardarHistorial("Nueva Reservación", "Una nueva reserva se ha registrado", sp.getPersona());
                    alert.showAndWait();
                    
                    handleBorrarTodo();
                    principal.fijarCentroPane("PanelReservaciones");
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("");
                    alert.setContentText("Ha ocurrido un error al guardar.");
                    alert.showAndWait();
                }
            }            
        }
    }
    
    @FXML
    private void handlefechaEntrada(){
        if (campoFechaEntrada.getValue().isBefore(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fecha Incorrecta");
            alert.setHeaderText("");
            alert.setContentText("No se pueden seleccionar fechas anteriores a las de hoy.");
            alert.showAndWait();
            fijarFechaDeHoy();            
        }else if (campoFechaSalida.getValue() != null && campoFechaSalida.getValue() != null) {
            if (campoFechaSalida.getValue().isEqual(campoFechaEntrada.getValue()) ||
                    campoFechaSalida.getValue().isBefore(campoFechaEntrada.getValue())) {
                campoFechaSalida.setValue(campoFechaEntrada.getValue().plus(1, ChronoUnit.DAYS));
            }            
        }
        
    }
    
    @FXML
    private void handleFechaSalida(){
        if (campoFechaSalida.getValue().isBefore(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fecha Incorrecta");
            alert.setHeaderText("");
            alert.setContentText("No se pueden seleccionar fechas anteriores a las de hoy.");
            alert.showAndWait();
            fijarFechaDeHoy();
        }else if (campoFechaEntrada.getValue() != null && campoFechaSalida.getValue() != null) {
            if (campoFechaEntrada.getValue().isEqual(campoFechaSalida.getValue()) ||
                    campoFechaEntrada.getValue().isAfter(campoFechaSalida.getValue())) {
                campoFechaEntrada.setValue(campoFechaSalida.getValue().minus(1, ChronoUnit.DAYS));
            }            
        }
    }
    
    @FXML
    private void handleNroDeHabitaciones(){
        if (Validadores.validarValor(campoNroHabitaciones, 'i')) {
            if (Integer.parseInt(campoNroHabitaciones.getText()) > disponibles) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Valor excedente");
                alert.setHeaderText("");
                alert.setContentText("El valor ingresado supera el numero de habitaciones disponibles.");
                alert.showAndWait();
                campoNroHabitaciones.setText("");
            }
        }else{
            campoNroHabitaciones.setText("");
        }            
        calcularTotal();
    }
    
    @FXML
    private void validarEnterosAdultos() {
        if (!Validadores.validarValor(campoNroAdultos , 'i')) {
            campoNroAdultos.setText("");
        }
    }
    
    @FXML
    private void validarEnterosNinios() {
        if (!Validadores.validarValor(campoNroNinios, 'i')) {
            campoNroNinios.setText("");
        }
    }
    
    @FXML
    private void handleBorrarTodo(){
        limpiar();
        limpiarDNI();
        limpiarFechas();
        limpiarHabitacion();
        limpiarLabels();
        scrollPanel.relocate(0, 0);
    }
    
    @FXML
    private void regresarAFechas(){
        if (!campoCliente.getText().isEmpty()) {
            limpiarHabitacion();
            panelReserva.setDisable(false); 
        }        
    }
    
    @FXML
    private void regresarADNI(){
        handleBorrarTodo();
    }
    
    
    /*
     * Objetos de NUEVA RESERVACION
    */
    @FXML
    private Pane panelReserva;
    @FXML
    private Pane panelHabitaciones;
    @FXML
    private TextField campoDNI;
    @FXML
    private TextField campoCliente;
    @FXML
    private DatePicker campoFechaEntrada;
    @FXML
    private DatePicker campoFechaSalida;
    @FXML
    private TextField campoNroAdultos;
    @FXML
    private TextField campoNroNinios;
    @FXML
    private TextField campoNroHabitaciones;
    @FXML
    private TextArea campoObservaciones;
    @FXML
    private Label lblDisponibles;
    @FXML
    private Label lblCapacidad;
    @FXML
    private Label lblCamas;
    @FXML
    private Label lblDescripcion;
    @FXML
    private Button btnReservar;
    @FXML
    private ComboBox cbxHabitaciones;
    private ObservableList<Habitacion> h;
    @FXML
    private ListView<Servicio> listaServicios;
    
    @FXML
    private ScrollPane scrollPanel;
    @FXML
    private Button btnDNIRegreso;
    
    @FXML
    private Label lblNoches;
    @FXML
    private Label lblValorHabitacion;
    @FXML
    private Label lblValorServicios;
    @FXML
    private Label lblValorIVA;
    @FXML
    private Label lblSubtotal;
    @FXML
    private Label lblTotal;

    private ServicioPersona sp = new ServicioPersona();
    private ServicioHabitacion sh = new ServicioHabitacion();
    private ServicioReservacion sr = new ServicioReservacion();
    private ServicioDetalle sd = new ServicioDetalle();
    private ServicioServicio ss = new ServicioServicio();
    
    private Integer disponibles;
    
    private Principal principal;
}