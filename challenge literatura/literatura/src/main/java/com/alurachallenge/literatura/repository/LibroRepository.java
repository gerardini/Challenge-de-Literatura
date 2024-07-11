package com.alurachallenge.literatura.repository;


import com.alurachallenge.literatura.modelo.CategoriaIdioma;
import com.alurachallenge.literatura.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findLibroBytitulo(String titulo);
    List<Libro> findLibrosByidioma(CategoriaIdioma idioma);


}
