package med.voll.api.domain.direccion;

import jakarta.validation.constraints.NotBlank;

public record DatosDireccion(
        @NotBlank
        String calle,
        @NotBlank
        String distrito,
        @NotBlank
        String codigopostal,
        @NotBlank
        String complemento,
        @NotBlank
        String numero,
        @NotBlank
        String provincia,
        @NotBlank
        String ciudad) {
}
