package com.alurachallenges.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConvierteDatos implements IConvierteDatos {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public <T> T obtenerDatos(String json, Class<T> classe) {
        try {
            return objectMapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
