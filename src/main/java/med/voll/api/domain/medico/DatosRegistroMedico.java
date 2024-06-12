package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.direccion.DatosDireccion;

public record DatosRegistroMedico(
        //validación de campos
        @NotBlank
        String nombre,
        @NotBlank
        @Email //Validar el email
        String email,
        @NotBlank
        String telefono,
        @NotBlank
        @Pattern(regexp = "\\d{4,14}") //expresion con digitos de 4 a 6
        String documentoidentidad,
        @NotNull
        Especialidad especialidad,
        @NotNull //porque es objeto
        @Valid
        DatosDireccion direccion) {
}
