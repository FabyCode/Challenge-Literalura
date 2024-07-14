package com.alurachallenges.literalura.principal;

import com.alurachallenges.literalura.model.Autor;
import com.alurachallenges.literalura.model.Libro;
import com.alurachallenges.literalura.model.LibroDTO;
import com.alurachallenges.literalura.repository.ILibro;
import com.alurachallenges.literalura.service.ConsumoAPI;
import com.alurachallenges.literalura.service.ConvierteDatos;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Principal {
    @Autowired
    private ILibro libroRepo;

    @Autowired
    private ConsumoAPI consumoAPI;

    @Autowired
    private ConvierteDatos convierteDatos;

    private final Scanner lectura = new Scanner(System.in);

    @Autowired
    public Principal(ILibro libroRepo, ConsumoAPI consumoAPI, ConvierteDatos convierteDatos) {
        this.libroRepo = libroRepo;
        this.consumoAPI = consumoAPI;
        this.convierteDatos = convierteDatos;
    }

    public void ejecutar() {
        boolean running = true;
        while (running) {
            mostrarMenu();
            var op = lectura.nextInt();
            lectura.nextLine();

            switch (op) {
                case 1 -> buscarPorTitulo();
                case 2 -> listarLibrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivos();
                case 5 -> listarLibrosPorIdioma();
                case 0 -> {
                    System.out.println("Terminando...");
                    running = false;
                }
                default -> System.out.println("Opción no válida");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("""
            -----------------------------------------------------------
                                    LITERALURA
            -----------------------------------------------------------
            
                       [1] Búsqueda de libro por título
                       [2] Lista de todos los libros
                       [3] Lista de autores
                       [4] Listar autores vivos
                       [5] Lista de libros en un idioma
                       [0] Salir
                       
            """);
    }

    private void guardarLibros(List<Libro> libros) {
        libros.forEach(libroRepo::save);
    }

    private void buscarPorTitulo() {
        String baseURL = "https://gutendex.com/books?search=";

        try {
            System.out.println("Escriba el título del libro: ");
            String titulo = lectura.nextLine();
            String direccion = baseURL + titulo.replace(" ", "%20");
            System.out.println("URL de la API: " + direccion);

            String jsonResponse = consumoAPI.obtenerDatos(direccion);
            System.out.println("Respuesta de la API: " + jsonResponse);

            if (jsonResponse.isEmpty()) {
                System.out.println("Respuesta de la API está vacía.");
                return;
            }

            // Extrae la lista de libros de la clave "results"
            JsonNode rootNode = convierteDatos.getObjectMapper().readTree(jsonResponse);
            JsonNode resultsNode = rootNode.path("results");

            if (resultsNode.isEmpty()) {
                System.out.println("No fue posible encontrar el libro buscado.");
                return;
            }

            // Convierte los resultados de la API en objetos LibroDTO
            List<LibroDTO> librosDTO = convierteDatos.getObjectMapper()
                    .readerForListOf(LibroDTO.class)
                    .readValue(resultsNode);

            // Elimina duplicados existentes en la base de datos
            List<Libro> librosExistentes = libroRepo.findByTitulo(titulo);
            if (!librosExistentes.isEmpty()) {
                System.out.println("Eliminando libros duplicados ya existentes en la base de datos...");
                for (Libro libroExistente : librosExistentes) {
                    librosDTO.removeIf(libroDTO -> libroExistente.getTitulo().equals(libroDTO.titulo()));
                }
            }

            // Guarda los nuevos libros en la base de datos
            if (!librosDTO.isEmpty()) {
                System.out.println("Guardando nuevos libros encontrados...");
                List<Libro> nuevosLibros = librosDTO.stream().map(Libro::new).collect(Collectors.toList());
                guardarLibros(nuevosLibros);
                System.out.println("¡Libros guardados con éxito!");
            } else {
                System.out.println("Todos los libros ya están registrados en la base de datos.");
            }

            // Muestra los libros encontrados
            if (!librosDTO.isEmpty()) {
                System.out.println("Libros encontrados:");
                Set<String> titulosExhibidos = new HashSet<>(); // Para controlar títulos ya exhibidos
                for (LibroDTO libro : librosDTO) {
                    if (!titulosExhibidos.contains(libro.titulo())) {
                        System.out.println(libro);
                        titulosExhibidos.add(libro.titulo());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar libros: " + e.getMessage());
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepo.findAll();
        if (libros.isEmpty()) {
            System.out.println("Ningún libro registrado.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        List<Libro> libros = libroRepo.findAll();
        if (libros.isEmpty()) {
            System.out.println("Ningún autor registrado.");
        } else {
            libros.stream()
                    .map(Libro::getAutor)
                    .distinct()
                    .forEach(autor -> System.out.println(autor.getAutor()));
        }
    }

    private void listarAutoresVivos() {
        System.out.println("Escriba el año: ");
        Integer ano = lectura.nextInt();
        lectura.nextLine();

        Year year = Year.of(ano);

        List<Autor> autores = libroRepo.findAutoresVivos(year);
        if (autores.isEmpty()) {
            System.out.println("Ningún autor encontrado.");
        } else {
            System.out.println("Lista de autores vivos en el año " + ano + ":\n");

            autores.forEach(autor -> {
                if(Autor.ano(autor.getAnoNacimento()) && Autor.ano(autor.getAnoFallecimento())){
                    String nomeAutor = autor.getAutor();
                    String anoNacimento = autor.getAnoNacimento().toString();
                    String anoFallecimento = autor.getAnoFallecimento().toString();
                    System.out.println(nomeAutor + " (" + anoNacimento + " - " + anoFallecimento + ")");
                }
            });
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
            Escriba el idioma deseado:
            Inglés (en)
            Portugués (pt)
            Español (es)
            Francés (fr)
            Alemán (de)
            """);
        String idioma = lectura.nextLine();

        List<Libro> libros = libroRepo.findByIdioma(idioma);
        if (libros.isEmpty()) {
            System.out.println("Ningún libro encontrado.");
        } else {
            libros.forEach(libro -> {
                String titulo = libro.getTitulo();
                String autor = libro.getAutor().getAutor();
                String idiomaLibro = libro.getIdioma();

                System.out.println("Título: " + titulo);
                System.out.println("Autor: " + autor);
                System.out.println("Idioma: " + idiomaLibro);
                System.out.println("----------------------------------------");
            });
        }
    }
}
