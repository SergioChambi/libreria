
package com.library.library.repositorios;

import com.library.library.entidades.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sergio Javier
 */
@Repository
public interface AutorRepositorio extends JpaRepository<Autor, String> {

}
