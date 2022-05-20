
package com.library.library.repositorios;

import com.library.library.entidades.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sergio Javier
 */
@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, String> {

}
