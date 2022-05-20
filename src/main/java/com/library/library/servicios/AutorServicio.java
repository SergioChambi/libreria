package com.library.library.servicios;

import com.library.library.entidades.Autor;
import com.library.library.errores.ErrorServicio;
import com.library.library.repositorios.AutorRepositorio;
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
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional
    public void registrar(String nombre, String apellido) throws ErrorServicio {
        validar(nombre, apellido);

        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setApellido(apellido);
        autorRepositorio.save(autor);

    }

    @Transactional
    public void modificar(String id, String nombre, String apellido) throws ErrorServicio {
        validar(nombre, apellido);

        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setNombre(nombre);
            autor.setApellido(apellido);

            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontro el autor solicitado");
        }
    }

    @Transactional
    public void eliminar(String id) throws ErrorServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autorRepositorio.delete(autor);
        } else {
            throw new ErrorServicio("No se encontro el autor solicitado");
        }
    }

    
    public List<Autor> listarAutores() {
        return autorRepositorio.findAll();
    }

    
    public Autor BuscarPorId(String id) throws ErrorServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio("El autor solicitado no existe");
        }

    }

    public void validar(String nombre, String apellido) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no debe ser nulo");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido no debe ser nulo");
        }

    }

}
