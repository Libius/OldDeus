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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Francis
 */
@Getter
@Setter
@Entity
@Table(name = "cuenta")
public class Cuenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_cuenta;
    
    @Column(length = 60, unique = true)
    private String usuario;
    private String clave;
    @Column(length = 100, unique = true)
    private String correo;
    private Boolean estado;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;
    @Temporal(TemporalType.TIMESTAMP)
    private Date update_at;
    
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", nullable = false, referencedColumnName = "id_persona")
    private Persona persona;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id_cuenta != null ? id_cuenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id_cuenta fields are not set
        if (!(object instanceof Cuenta)) {
            return false;
        }
        Cuenta other = (Cuenta) object;
        if ((this.id_cuenta == null && other.id_cuenta != null) || (this.id_cuenta != null && !this.id_cuenta.equals(other.id_cuenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "od.modelo.Cuenta[ id=" + id_cuenta + " ]";
    }
    
}
