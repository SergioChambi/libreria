package com.library.library.controladores;

import com.library.library.entidades.Usuario;
import com.library.library.errores.ErrorServicio;
import com.library.library.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam String id, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado == null || !logueado.getId().equals(id)) {
            return "redirect:/inicio";
        }

        try {
            Usuario usuario = usuarioServicio.ObtenerId(id);
            modelo.addAttribute("perfil", usuario);
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
        }
        return "editarPerfil";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @PostMapping("/actualizar-perfil")
    public String actualizarPerfil(ModelMap modelo, HttpSession session, @RequestParam String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2) throws ErrorServicio {
        Usuario usuario = null;
        try {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            if (logueado == null || !logueado.getId().equals(id)) {
                return "redirect:/inicio";
            }
            usuario = usuarioServicio.ObtenerId(id);
            usuarioServicio.modificar(id, nombre, apellido, mail, clave1, clave2);
            session.setAttribute("usuariosession", usuario);
            return "redirect:/inicio";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("perfil", usuario);

            return "editarPerfil";
        }

    }

}
