package com.library.library.servicios;

import com.library.library.entidades.Usuario;
import com.library.library.enumeraciones.Rol;
import com.library.library.errores.ErrorServicio;
import com.library.library.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author Sergio Javier
 */
@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

//    @Autowired
//    private NotificacionServicio notificacionServicio;
    @Transactional
    public void registrar(String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio {
        validar(nombre, apellido, mail, clave, clave2);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        usuario.setRol(Rol.USUARIO);

        String encriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);

        usuarioRepositorio.save(usuario);

        //notificacionServicio.enviar("Bienvenido a la libreria de Sergio", "Libreria Sergio", usuario.getMail());
    }

    @Transactional
    public void modificar(String id, String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio {
        validar(nombre, apellido, mail, clave, clave2);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setMail(mail);
            usuario.setRol(Rol.USUARIO);

            String encriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);

            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado");
        }
    }

    @Transactional
    public void eliminar(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuarioRepositorio.delete(usuario);

        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado");
        }
    }

    public Usuario ObtenerId(String id) {
        return usuarioRepositorio.getOne(id);
    }

    public void validar(String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del usuario no debe ser nulo");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido del usuario no debe ser nulo");
        }
        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El correo del usuario no debe ser nulo");
        }
        Usuario u = usuarioRepositorio.BuscarPorMail(mail);
        if (u != null) {
            throw new ErrorServicio("Ya existe un usuario con ese correo");
        }
        if (clave == null || clave.isEmpty()) {
            throw new ErrorServicio("La clave del usuario no debe ser nula");
        }
        if (clave.length() <= 6) {
            throw new ErrorServicio("La clave debe tener mas de seis dígitos");
        }
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves deben ser iguales");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio.BuscarPorMail(mail);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();

            //Creo una lista de permisos!
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + usuario.getRol());
            permisos.add(p1);

            //Esto me permite guardar el OBJETO USUARIO LOG, para luego ser utilizado
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            User user = new User(usuario.getMail(), usuario.getClave(), permisos);
            return user;
        } else {
            return null;
        }
    }

}
