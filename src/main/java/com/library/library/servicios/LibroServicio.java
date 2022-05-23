package com.library.library.servicios;

import com.library.library.entidades.Autor;
import com.library.library.entidades.Editorial;
import com.library.library.entidades.Foto;
import com.library.library.entidades.Genero;
import com.library.library.entidades.Libro;
import com.library.library.errores.ErrorServicio;
import com.library.library.repositorios.AutorRepositorio;
import com.library.library.repositorios.EditorialRepositorio;
import com.library.library.repositorios.GeneroRepositorio;
import com.library.library.repositorios.LibroRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Sergio Javier
 */
@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;

    @Autowired
    private EditorialRepositorio editorialRepositorio;
    @Autowired
    private GeneroRepositorio generoRepositorio;

    @Autowired
    private FotoServicio fotoServicio;

    @Transactional
    public void Registrar(MultipartFile archivo, String idAutor, String idEditorial, String idGenero, Long isbn, String titulo, Integer anio) throws ErrorServicio {
        Genero genero = generoRepositorio.getOne(idGenero);
        Autor autor = autorRepositorio.getOne(idAutor);
        Editorial editorial = editorialRepositorio.getOne(idEditorial);
        validar(isbn, titulo, anio, genero);
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setGenero(genero);
        libro.setAutor(autor);
        libro.setEditorial(editorial);

        Foto foto = fotoServicio.guardar(archivo);
        libro.setFoto(foto);

        libroRepositorio.save(libro);

    }

    @Transactional
    public void modificar(String idLibro, MultipartFile archivo, String idAutor, String idEditorial, String idGenero, Long isbn, String titulo, Integer anio) throws ErrorServicio {

        Autor autor = autorRepositorio.getOne(idAutor);
        Editorial editorial = editorialRepositorio.getOne(idEditorial);
        Genero genero = generoRepositorio.getOne(idGenero);
        validar(isbn, titulo, anio, genero);

        Optional<Libro> respuesta = libroRepositorio.findById(idLibro);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setGenero(genero);
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            if (!archivo.isEmpty()) {
                String idFoto = null;
                if (libro.getFoto() != null) {
                    idFoto = libro.getFoto().getId();
                }
                Foto foto = fotoServicio.actualizarFoto(idFoto, archivo);
                libro.setFoto(foto);
            }

            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se encontro el libro solicitado");
        }

    }

    @Transactional
    public void eliminar(String id) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libroRepositorio.delete(libro);
        } else {
            throw new ErrorServicio("No se encontro el libro solicitado");
        }

    }

    @Transactional(readOnly = true)
    public List<Libro> listarLibros() {
        return libroRepositorio.findAll();
    }

    public Libro BuscarPorId(String id) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio("El libro solicitado no existe");
        }

    }

    public void validar(Long isbn, String titulo, Integer anio, Genero genero) throws ErrorServicio {
        if (isbn == null || isbn <= 0) {
            throw new ErrorServicio("ISBN inválido");
        }
        Libro l = libroRepositorio.BuscarPorIsbn(isbn);
        if (l != null) {
            throw new ErrorServicio("Ya existe ese número de ISBN");
        }
        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("Titulo no debe ser nulo");
        }
        if (anio < 0) {
            throw new ErrorServicio("Año inválido");

        }
        if (genero == null) {
            throw new ErrorServicio("No se encontró el género solicitado");
        }
    }
}
