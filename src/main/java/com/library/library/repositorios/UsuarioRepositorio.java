
package com.library.library.repositorios;

import com.library.library.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sergio Javier
 */
@Repository
public interface UsuarioRepositorio  extends JpaRepository<Usuario, String> {
     
    @Query("SELECT c FROM Usuario c WHERE c.mail = :mail")
    public Usuario BuscarPorMail(@Param("mail") String mail); 
    
    @Query("SELECT c FROM Usuario c WHERE c.id = :id")
    public Usuario BuscarPorId(@Param("id") String id);

}
