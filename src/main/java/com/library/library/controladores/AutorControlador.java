package com.library.library.controladores;

import com.library.library.entidades.Autor;
import com.library.library.errores.ErrorServicio;
import com.library.library.servicios.AutorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@PreAuthorize("hasAnyRole('ROLE_USUARIO')")
@RequestMapping("/autor")
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

   
    @GetMapping("/registro")
    public String registro() {

        return "crear_autor";
    }

  
    @PostMapping("/registrar_autor")
    public String guardar(@RequestParam String nombre, @RequestParam String apellido) {
        try {
            autorServicio.registrar(nombre, apellido);
            return "crear_autor";
        } catch (ErrorServicio ex) {

            return "crear_autor";
        }
    }

    
    @GetMapping("/editar_autor/{id}")
    public String editar(@PathVariable String id, ModelMap modelo) throws ErrorServicio {
        modelo.put("autor", autorServicio.BuscarPorId(id));
        return "editar_autor";
    }

    @PostMapping("/editar_autor")
    public String editar(@RequestParam String id, @RequestParam String nombre, @RequestParam String apellido) {
        try {
            autorServicio.modificar(id, nombre, apellido);
            return "catalogo_autor";
        } catch (ErrorServicio ex) {
            return "editar_autor";
        }

    }

    @GetMapping("/lista")
    public String lista(ModelMap modelo) {
        List<Autor> AutorLista = autorServicio.listarAutores();
        modelo.addAttribute("autores", AutorLista);
        return "catalogoAutorUsuario";
    }

}
