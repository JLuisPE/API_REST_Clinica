package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    //Atrapar los datos recibidos desde el FronEnd CREATE
    @PostMapping
    @Transactional
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroPaciente datosRegistroPaciente,
                                    UriComponentsBuilder uriComponentsBuilder) {
        Paciente paciente = pacienteRepository.save(new Paciente(datosRegistroPaciente));

        DatosRespuestaPaciente datosRespuestaPaciente = new DatosRespuestaPaciente(paciente.getId(), paciente.getNombre(), paciente.getEmail(),
                paciente.getTelefono(), paciente.getDocumentoidentidad(),
                new DatosDireccion(paciente.getDireccion().getCalle(), paciente.getDireccion().getDistrito(),
                        paciente.getDireccion().getCiudad(), paciente.getDireccion().getNumero(),
                        paciente.getDireccion().getComplemento(), paciente.getDireccion().getCodigopostal(),
                        paciente.getDireccion().getProvincia()));
        URI url = uriComponentsBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaPaciente);
    }

    //Metodo para enviar los datos especificos de medicos al frontend READ
    @GetMapping
    public ResponseEntity<Page<DatosListadoPaciente>> listar(@PageableDefault(size = 10, sort = "nombre") Pageable paginacion) {
        //var page = repository.findAllByAtivoTrue(paginacion).map(DatosListadoPaciente::new);
        return ResponseEntity.ok(pacienteRepository.findByActivoTrue(paginacion).map(DatosListadoPaciente::new));
    }


    //Actualiza registro UPDATE
    @PutMapping
    @Transactional
    public ResponseEntity actualizar(@RequestBody @Valid DatosActualizarPaciente datosActualizarPaciente) {
        Paciente paciente = pacienteRepository.getReferenceById(datosActualizarPaciente.id());
        paciente.actualizarDatos(datosActualizarPaciente);

        return ResponseEntity.ok(new DatosRespuestaPaciente(paciente.getId(), paciente.getNombre(), paciente.getEmail(),
                paciente.getTelefono(), paciente.getDocumentoidentidad(),
                new DatosDireccion(paciente.getDireccion().getCalle(), paciente.getDireccion().getDistrito(),
                        paciente.getDireccion().getCiudad(), paciente.getDireccion().getNumero(),
                        paciente.getDireccion().getComplemento(), paciente.getDireccion().getCodigopostal(),
                        paciente.getDireccion().getProvincia())));
    }

    //Eliminar registro de manera lógica en la base de datos DELETE
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminar(@PathVariable Long id) {
        var paciente = pacienteRepository.getReferenceById(id);
        paciente.eliminar();

        return ResponseEntity.noContent().build();
    }

    //devolver los datos de un paciente
    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaPaciente> retornaDatosPaciente(@PathVariable Long id) {
        Paciente paciente = pacienteRepository.getReferenceById(id);
        var datosPaciente = new DatosRespuestaPaciente(paciente.getId(), paciente.getNombre(), paciente.getEmail(),
                paciente.getTelefono(), paciente.getDocumentoidentidad(),
                new DatosDireccion(paciente.getDireccion().getCalle(), paciente.getDireccion().getDistrito(),
                        paciente.getDireccion().getCiudad(), paciente.getDireccion().getNumero(),
                        paciente.getDireccion().getComplemento(), paciente.getDireccion().getCodigopostal(),
                        paciente.getDireccion().getProvincia()));
        return ResponseEntity.ok(datosPaciente);
    }
}