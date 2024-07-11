package com.alurachallenge.literatura.modelo;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String nombre;
    private Integer yearFallecimiento;
    private Integer yearNacimiento;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor(){}

    public Autor (DatosAutor datosAutor){
        this.nombre = datosAutor.nombre();
        this.yearFallecimiento = datosAutor.yearFallecimiento();
        this.yearNacimiento = datosAutor.yearNacimiento();
    }

    @Override
    public String toString() {
        return  "\nAutor: " + nombre + "\n" +
                "Fecha de nacimiento: " + yearNacimiento + "\n" +
                "Fecha de fallecimiento: " +  yearFallecimiento + "\n" +
                "Libros: " + libros.stream().map(d -> d.getTitulo()).toList();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getYearFallecimiento() {
        return yearFallecimiento;
    }

    public void setYearFallecimiento(Integer yearFallecimiento) {
        this.yearFallecimiento = yearFallecimiento;
    }

    public Integer getYearNacimiento() {
        return yearNacimiento;
    }

    public void setYearNacimiento(Integer yearNacimiento) {
        this.yearNacimiento = yearNacimiento;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        libros.forEach(e -> e.setAutor(this));
        this.libros = libros;
    }
}
