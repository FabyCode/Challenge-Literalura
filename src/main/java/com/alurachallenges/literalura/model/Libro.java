package com.alurachallenges.literalura.model;

import jakarta.persistence.*;

@Entity
@Table
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne(cascade = CascadeType.ALL)
    private Autor autor;

    private String idioma;

    private Integer anoNacimentoAutor;

    private Integer anoFallecimentoAutor;

    private Double numeroDownloads;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getAnoNacimentoAutor() {
        return anoNacimentoAutor;
    }

    public void setAnoNacimentoAutor(Integer anoNascimentoAutor) {
        this.anoNacimentoAutor = anoNascimentoAutor;
    }

    public Integer getAnoFallecimentoAutor() {
        return anoFallecimentoAutor;
    }

    public void setAnoFallecimentoAutor(Integer anoFallecimentoAutor) {
        this.anoFallecimentoAutor = anoFallecimentoAutor;
    }

    public Double getNumeroDownloads() {
        return numeroDownloads;
    }

    public void setNumeroDownloads(Double numeroDownloads) {
        this.numeroDownloads = numeroDownloads;
    }

    public Libro() {}

    public Libro(LibroDTO libroDTO) {
        this.titulo = libroDTO.titulo();
        Autor autor = new Autor(libroDTO.autores().get(0));
        this.autor = autor;
        this.idioma = libroDTO.idioma().get(0);
        this.numeroDownloads = libroDTO.numeroDownload();
    }

    public Libro(Long idApi, String titulo, Autor autor, String idioma, Double numeroDownload) {
        this.titulo = titulo;
        this.autor = autor;
        this.idioma = idioma;
        this.numeroDownloads = numeroDownload;
    }

    @Override
    public String toString() {
        return "Título: " + titulo + "\n" +
                "Autor: " + autor + "\n" +
                "Idioma: " + idioma + "\n" +
                "Downloads: " + numeroDownloads + "\n" +
                "----------------------------------------";
    }
}
