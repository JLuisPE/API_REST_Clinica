package med.voll.api.domain.paciente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.direccion.DatosDireccion;

public record DatosRegistroPaciente(
        @NotBlank
        String nombre,
        @NotBlank @Email
        String email,
        @NotBlank
        String telefono,
        @NotBlank
        @Pattern(regexp = "\\d{4,14}") //expresion con digitos de 4 a 6
        //@Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}\\-?\\d{2}")
        String documentoidentidad,
        @NotBlank
        String urbanizacion,
        @NotNull @Valid
        DatosDireccion direccion
) {
}
