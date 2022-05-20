package com.library.library.servicios;

import com.library.library.entidades.Editorial;
import com.library.library.errores.ErrorServicio;
import com.library.library.repositorios.EditorialRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Sergio Javier
 */
@Service
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public void registrar(String nombre) throws ErrorServicio {
        validar(nombre);

        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);

        editorialRepositorio.save(editorial);

    }

    @Transactional
    public void modificar(String id, String nombre) throws ErrorServicio {
        validar(nombre);

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);

            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontro la editorial solicitada");
        }
    }

    @Transactional
    public void eliminar(String id) throws ErrorServicio {
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorialRepositorio.delete(editorial);
        } else {
            throw new ErrorServicio("No se encontro la editorial solicitada");
        }
    }

    public Editorial BuscarPorId(String id) throws ErrorServicio {
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio("La editorial solicitada no existe");
        }

    }

    public List<Editorial> listarEditoriales() {
        return editorialRepositorio.findAll();
    }

    public void validar(String nombre) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no debe ser nulo");
        }

    }

}
