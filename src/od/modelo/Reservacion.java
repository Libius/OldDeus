/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package od.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Francis
 */
@Getter
@Setter
@Entity
@Table(name = "reservacion")
public class Reservacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_reservacion;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha_inicio;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha_fin;
    private Boolean estado;
    @Column(length = 10)
    private Double pago_total;
    
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_persona", nullable = false, referencedColumnName = "id_persona")
    private Persona persona;
    
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_habitacion", nullable = false, referencedColumnName = "id_habitacion")
    private Habitacion habitacion;
    
    @OneToOne(mappedBy = "reservacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Detalle detalle;
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id_reservacion != null ? id_reservacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id_reservacion fields are not set
        if (!(object instanceof Reservacion)) {
            return false;
        }
        Reservacion other = (Reservacion) object;
        if ((this.id_reservacion == null && other.id_reservacion != null) || (this.id_reservacion != null && !this.id_reservacion.equals(other.id_reservacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "od.modelo.Reservacion[ id=" + id_reservacion + " ]";
    }
    
}
