package br.com.neocamp.partidas_futebol.exceptions;

public class EntityBadRequestException extends RuntimeException {
    public EntityBadRequestException(String message) {
        super(message);
    }
}
