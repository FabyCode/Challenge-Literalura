package com.alurachallenges.literalura.model;


import jakarta.persistence.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String autor;

    @Column(name = "ano_nacimento")
    private Year anoNacimento;

    @Column(name = "ano_fallecimento")
    private Year anoFallecimento;

    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Year getAnoNacimento() {
        return anoNacimento;
    }

    public void setAnoNacimento(Year anoNascimento) {
        this.anoNacimento = anoNascimento;
    }

    public Year getAnoFallecimento() {
        return anoFallecimento;
    }

    public void setAnoFallecimento(Year anoFalecimento) {
        this.anoFallecimento = anoFalecimento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> livros) {
        this.libros = livros;
    }

    // Construtores
    public Autor() {}

    public static boolean ano(Year ano) {
        return ano != null && !ano.equals(Year.of(0));
    }

    public Autor(AutorDTO autorDTO) {
        this.autor = autorDTO.autor();
        this.anoNacimento = autorDTO.anoNacimento() != null ? Year.of(autorDTO.anoNacimento()) : null;
        this.anoFallecimento = autorDTO.anoFallecimento() != null ? Year.of(autorDTO.anoFallecimento()) : null;
    }


    public Autor(String autor, Year anoNascimento, Year anoFalecimento) {
        this.autor = autor;
        this.anoNacimento = anoNascimento;
        this.anoFallecimento = anoFalecimento;
    }

    @Override
    public String toString() {
        String anoNacimentoStr = anoNacimento != null ? anoNacimento.toString() : "Desconhecido";
        String anoFallecimentoStr = anoFallecimento != null ? anoFallecimento.toString() : "Desconhecido";

        return "Autor: " + autor + " (nacido en " + anoNacimentoStr + ", fallecido en " + anoFallecimentoStr + ")";
    }
}
