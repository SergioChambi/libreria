 package com.library.library.controladores;

import com.library.library.entidades.Autor;
import com.library.library.entidades.Editorial;
import com.library.library.entidades.Libro;
import com.library.library.errores.ErrorServicio;
import com.library.library.servicios.AutorServicio;
import com.library.library.servicios.EditorialServicio;

import com.library.library.servicios.LibroServicio;
import com.library.library.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Sergio Javier
 */
@Controller
@RequestMapping("/")
public class MainControlador {

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialServicio editorialServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index(ModelMap modelo) {
        List<Libro> libroLista = libroServicio.listarLibros();
        modelo.addAttribute("libros", libroLista);

        return "index";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo) {
        List<Libro> libroLista = libroServicio.listarLibros();
        modelo.addAttribute("libros", libroLista);

        return "index_usuario";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Correo o clave incorrectos");
        }
        if (logout != null) {
            modelo.put("logout", "Has salido correctamente de la sesión");
        }
        return "sesion";
    }

    @GetMapping("/registro")
    public String registro() {

        return "registroUsuario";
    }

    @PostMapping("/registrar")
    public String registrar(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2) {
        try {
            usuarioServicio.registrar(nombre, apellido, mail, clave1, clave2);

        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("mail", mail);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave2);

            return "registroUsuario";
        }

        modelo.put("success", "Te has registrado satisfactoriamente");
        return "registroUsuario";

    }

    @GetMapping("/autores")
    public String lista_autores(ModelMap modelo) {
        List<Autor> AutorLista = autorServicio.listarAutores();
        modelo.addAttribute("autores", AutorLista);
        return "catalogo_autor";
    }

    @GetMapping("/editoriales")
    public String lista_editoriales(ModelMap modelo) {
        List<Editorial> EditorialLista = editorialServicio.listarEditoriales();
        modelo.addAttribute("editoriales", EditorialLista);
        return "catalogo_editorial";
    }

    @GetMapping("/libros")
    public String lista_libros(ModelMap modelo) {
        List<Libro> libroLista = libroServicio.listarLibros();
        modelo.addAttribute("libros", libroLista);
        return "catalogo_libros";
    }
}
