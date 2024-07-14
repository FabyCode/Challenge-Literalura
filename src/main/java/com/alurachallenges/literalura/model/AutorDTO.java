package com.alurachallenges.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AutorDTO(@JsonAlias("name") String autor,
                       @JsonAlias("birth_year") Integer anoNacimento,
                       @JsonAlias("death_year") Integer anoFallecimento){
    @Override
    public String toString() {
        return "Autor: " + autor;
    }
}
