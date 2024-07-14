package com.alurachallenges.literalura.repository;

import com.alurachallenges.literalura.model.Autor;
import com.alurachallenges.literalura.model.Libro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.Year;
import java.util.List;

public interface ILibro extends JpaRepository<Libro, Long>{
    @Query("SELECT l FROM Libro l WHERE LOWER(l.titulo) = LOWER(:titulo)")
    List<Libro> findByTitulo(String titulo);

    @Query("SELECT a FROM Autor a WHERE a.anoNacimento <= :ano AND (a.anoFallecimento IS NULL OR a.anoFallecimento >= :ano)")
    List<Autor> findAutoresVivos(@Param("ano") Year ano);

    @Query("SELECT a FROM Autor a WHERE a.anoNacimento = :ano AND (a.anoFallecimento IS NULL OR a.anoFallecimento >= :ano)")
    List<Autor> findAutoresVivosRefinado(@Param("ano") Year ano);

    @Query("SELECT a FROM Autor a WHERE a.anoNacimento <= :ano AND a.anoFallecimento = :ano")
    List<Autor> findAutoresPorMuerte(@Param("ano") Year ano);

    @Query("SELECT l FROM Libro l WHERE l.idioma LIKE %:idioma%")
    List<Libro> findByIdioma(@Param("idioma") String idioma);
}
