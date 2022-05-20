package com.library.library.repositorios;

import com.library.library.entidades.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sergio Javier
 */
@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {
    
    @Query("SELECT l FROM Libro l WHERE l.isbn = :isbn")
    public Libro BuscarPorIsbn(@Param("isbn") Long isbn);
}
