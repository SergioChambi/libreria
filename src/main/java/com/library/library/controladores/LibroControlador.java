package com.library.library.controladores;

import com.library.library.entidades.Autor;
import com.library.library.entidades.Editorial;
import com.library.library.entidades.Genero;
import com.library.library.entidades.Libro;
import com.library.library.entidades.Usuario;

import com.library.library.errores.ErrorServicio;
import com.library.library.repositorios.AutorRepositorio;
import com.library.library.repositorios.EditorialRepositorio;
import com.library.library.repositorios.GeneroRepositorio;
import com.library.library.servicios.LibroServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Sergio Javier
 */
@Controller
@PreAuthorize("hasAnyRole('ROLE_USUARIO')")
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Autowired
    private GeneroRepositorio generoRepositorio;

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @GetMapping("/registro")
    public String registro(ModelMap modelo) {
        List<Autor> autores = autorRepositorio.findAll();
        List<Editorial> editoriales = editorialRepositorio.findAll();
        List<Genero> generos = generoRepositorio.findAll();

        modelo.put("autores", autores);
        modelo.put("editoriales", editoriales);
        modelo.put("generos", generos);

        return "crear_libro";
    }

    @PostMapping("/registrar_libro")
    public String guardar(ModelMap modelo, MultipartFile archivo, @RequestParam String idAutor, @RequestParam String idGenero, @RequestParam String idEditorial,
            @RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio) {
        try {
            libroServicio.Registrar(archivo, idAutor, idGenero, idEditorial, isbn, titulo, anio);

            return "crear_libro";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "crear_libro";
        }
    }

    @GetMapping("/modificar_libro/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) throws ErrorServicio {

        modelo.put("libro", libroServicio.BuscarPorId(id));
        List<Autor> autores = autorRepositorio.findAll();
        List<Editorial> editoriales = editorialRepositorio.findAll();
        List<Genero> generos = generoRepositorio.findAll();
        modelo.put("autores", autores);
        modelo.put("editoriales", editoriales);
        modelo.put("generos", generos);
        return "editar_libro";
    }
 
    @PostMapping("/modificar_libro/{id}")
    public String modificar(@PathVariable String id, MultipartFile archivo, @RequestParam String idAutor, @RequestParam String idEditorial, @RequestParam String idGenero, @RequestParam Long isbn, @RequestParam String titulo,
            @RequestParam Integer anio) {
        try {
            libroServicio.modificar(archivo, id, idAutor, idEditorial,idGenero, isbn, titulo, anio);

            return "catalogo_libros";
        } catch (ErrorServicio ex) {
        
            return "editar_libro";
        }
    }

    @GetMapping("/lista")
    public String lista(ModelMap modelo) {
        List<Libro> libroLista = libroServicio.listarLibros();
        modelo.put("libros", libroLista);
        return "catalogoLibroUsuario";
    } 

}
