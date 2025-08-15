package br.com.neocamp.partidas_futebol.exceptions;

public class EntityConflictException extends RuntimeException {

    public EntityConflictException(String message) {
        super(message);
    }

}
