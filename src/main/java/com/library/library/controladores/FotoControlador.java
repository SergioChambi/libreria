package com.library.library.controladores;

import com.library.library.entidades.Libro;
import com.library.library.errores.ErrorServicio;
import com.library.library.servicios.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/foto")
public class FotoControlador {
    
    @Autowired
    private LibroServicio libroServicio;
    
    @GetMapping("/libro/{id}")
    public ResponseEntity<byte[]> fotoLibro(@PathVariable String id) throws ErrorServicio {
        
        Libro libro = libroServicio.BuscarPorId(id);
        if(libro.getFoto() == null){
            throw new ErrorServicio("El libro no tiene una foto asignada");
        }
        byte[] foto = libro.getFoto().getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(foto, headers, HttpStatus.OK);
    }
    
}
