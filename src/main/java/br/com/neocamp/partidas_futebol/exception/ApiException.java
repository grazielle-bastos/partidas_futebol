package br.com.neocamp.partidas_futebol.exception;

public class ApiException extends RuntimeException {
        // Construtor que recebe uma mensagem de erro
        public ApiException(String message) {
            //
            super(message);
    }

    // Construtor que recebe uma mensagem de erro e uma causa
    public ApiException(String message, Throwable cause) {
            // Chama o construtor da classe pai (RuntimeException) passando a mensagem e a causa
            super(message,cause);
    }

}


