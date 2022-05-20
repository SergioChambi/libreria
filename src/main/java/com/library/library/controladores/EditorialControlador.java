package com.library.library.controladores;

import com.library.library.entidades.Editorial;
import com.library.library.errores.ErrorServicio;
import com.library.library.servicios.EditorialServicio;
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

/**
 *
 * @author Sergio Javier
 */
@Controller
@PreAuthorize("hasAnyRole('ROLE_USUARIO')")
@RequestMapping("/editorial")
public class EditorialControlador {
    
    @Autowired
    private EditorialServicio editorialServicio;
    
    @GetMapping("/registro")
    public String registro() {
        
        return "crear_editorial";
    }
    
    @PostMapping("/registrar_editorial")
    public String guardar(@RequestParam String nombre) {
        try {
            editorialServicio.registrar(nombre);
            return "crear_editorial";
        } catch (ErrorServicio ex) {
            
            return "crear_editorial";
        }
    }
    
    @GetMapping("/editar_editorial/{id}")
    public String editar(@PathVariable String id, ModelMap modelo) throws ErrorServicio {
        modelo.put("editorial", editorialServicio.BuscarPorId(id));
        return "editar_editorial";
    }
    
    @PostMapping("/editar_editorial/{id}")
    public String editar(@PathVariable String id, @RequestParam String nombre) {
        try {
            editorialServicio.modificar(id, nombre);
            return "catalogo_editorial";
        } catch (ErrorServicio ex) {
            return "editar_editorial";
        }
        
    }
    
    @GetMapping("/lista")
    public String lista(ModelMap modelo) {
        List<Editorial> EditorialLista = editorialServicio.listarEditoriales();
        modelo.addAttribute("editoriales", EditorialLista);
        return "catalogoEditorialUsuario";
    }
    
}
