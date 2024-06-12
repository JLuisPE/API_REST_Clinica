package med.voll.api.domain.paciente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.direccion.Direccion;

@Table(name = "pacientes")
@Entity(name = "Paciente")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String documentoidentidad;
    private String urbanizacion;
    private Boolean activo;
    @Embedded
    private Direccion direccion;

    public Paciente(DatosRegistroPaciente datos) {
        this.activo=true;
        this.nombre = datos.nombre();
        this.email = datos.email();
        this.telefono = datos.telefono();
        this.documentoidentidad = datos.documentoidentidad();
        this.urbanizacion = datos.urbanizacion();
        this.direccion = new Direccion(datos.direccion());
    }

    public void actualizarDatos(DatosActualizarPaciente datosActualizarPaciente) {
        if (datosActualizarPaciente.nombre() != null) {
            this.nombre = datosActualizarPaciente.nombre();
        }
        if (datosActualizarPaciente.documentoIdentidad() != null){
            this.documentoidentidad = datosActualizarPaciente.documentoIdentidad();
        }
        if (datosActualizarPaciente.direccion() != null){
            this.direccion = direccion.actualizarDatos(datosActualizarPaciente.direccion());
        }
    }

    public void eliminar() {
        this.activo = false;
    }
}
