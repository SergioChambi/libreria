
package com.library.library.repositorios;

import com.library.library.entidades.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sergio Javier
 */
@Repository
public interface FotoRepositorio extends JpaRepository<Foto, String>{

    
    
    
}
