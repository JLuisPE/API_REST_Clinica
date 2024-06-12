package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    //Atrapar los datos recibidos desde el FronEnd CREATE
    @PostMapping
    public ResponseEntity<DatosRespuestaMedico> registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico,
                                                                UriComponentsBuilder uriComponentsBuilder){
        //System.out.println(datosRegistroMedico);
        //Tomar y transformar los datos para Medico y Dirección guardar en la DB
        Medico medico = medicoRepository.save(new Medico(datosRegistroMedico));

        DatosRespuestaMedico datosRespuestaMedico = new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(),
                medico.getTelefono(), medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(), medico.getDireccion().getNumero(),
                        medico.getDireccion().getComplemento(), medico.getDireccion().getCodigopostal(),
                        medico.getDireccion().getProvincia()));
        // Return 201 Created
        // URL donde encontrar al medico
        //URI url = "http://localhost:8080/medicos/" + medico.getId();
        URI url = uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaMedico);
    }

    //Metodo para enviar los datos especificos de medicos al frontend READ
    //Paginacion de los datos con Pageable
    //@PageableDefault esta anotación sirve para limitar el numero de registros y el ordenamiento etc.
    @GetMapping
    public ResponseEntity <Page<DatosListadoMedico>> listadoMedicos(@PageableDefault(size = 10, sort = "nombre") Pageable paginacion){
        //return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new); Listar todos los registros
        return ResponseEntity.ok(medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new)); //Lista solo los registros activos
    }

    //Actualiza registro UPDATE
    @PutMapping
    @Transactional
    public ResponseEntity<DatosRespuestaMedico> actualizarMedico(@RequestBody @Valid DatosActualizarMedico datosActualizarMedico){
        Medico medico = medicoRepository.getReferenceById(datosActualizarMedico.id());
        medico.actualizarDatos(datosActualizarMedico);
        return ResponseEntity.ok(new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(),
                medico.getTelefono(), medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(), medico.getDireccion().getNumero(),
                        medico.getDireccion().getComplemento(), medico.getDireccion().getCodigopostal(),
                        medico.getDireccion().getProvincia())));

    }

    //Eliminar registro de manera lógica en la base de datos DELETE
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarMedico(@PathVariable Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
         return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaMedico> retornaDatosMedico(@PathVariable Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        var datosMedico = new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(),
                medico.getTelefono(), medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(), medico.getDireccion().getNumero(),
                        medico.getDireccion().getComplemento(), medico.getDireccion().getCodigopostal(),
                        medico.getDireccion().getProvincia()));
        return ResponseEntity.ok(datosMedico);
    }


    /*
    //Eliminar registro por completo de la base de datos
    @DeleteMapping("/{id}")
    @Transactional
    public void eliminarMedico(@PathVariable Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        medicoRepository.delete(medico);
    }


    //Metodo para enviar los datos especificos de medicos al frontend
    @GetMapping
    public List<DatosListadoMedico> listadoMedicos(){
        return medicoRepository.findAll().stream().map(DatosListadoMedico::new).toList();
    }

    //Metodo para enviar todos los datos de medicos al frontend
    @GetMapping
    public List<Medico> listadoMedicos(){
        return medicoRepository.findAll();
    }
    */

}
